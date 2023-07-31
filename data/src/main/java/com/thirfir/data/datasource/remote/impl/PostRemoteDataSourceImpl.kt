package com.thirfir.data.datasource.remote.impl

import com.thirfir.data.datasource.remote.PostRemoteDataSource
import com.thirfir.data.datasource.remote.dto.PostDTO
import com.thirfir.data.datasource.remote.dto.PostHeaderDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import javax.inject.Inject

class PostRemoteDataSourceImpl @Inject constructor() : PostRemoteDataSource {
    override fun getPostDTO(bulletin: Int, pid: Int) : Flow<PostDTO> = flow {
        try {
            val baseUrl = "https://portal.koreatech.ac.kr/ctt/bb/bulletin?b=${bulletin}&p=${pid}"
            val doc = Jsoup.connect(baseUrl).get()
            val post = getPostWithDoc(doc)

            emit(post)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }.flowOn(Dispatchers.IO)

    private fun getPostWithDoc(document: Document): PostDTO {
        val postDoc = document.selectFirst("div.bc-s-post-ctnt-area")

        // TODO("텍스트 색상, 표, 줄간격, 텍스트 사이즈, ... 처리 어떻게 할지")
        return postDoc!!.run {
            PostDTO(
                headerDTO = PostHeaderDTO(
                    30949,
                    "[대학혁신사업단] Chat GPT를 활용한 생성형 AI 교육(모집:~8.3.(목), 운영: 8.7.(월)~8.9.(수)., 제2차 신기술교육 캠프)",
                    "교내활동",
                    "대학혁신사업단",
                    "2023-07-28",
                    "279",
                    true
                ),
                body = listOf("body"),
                file = listOf("files")
            )
        }
    }
}