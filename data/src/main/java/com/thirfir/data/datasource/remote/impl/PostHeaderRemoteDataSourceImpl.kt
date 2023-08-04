package com.thirfir.data.datasource.remote.impl

import com.thirfir.data.datasource.remote.PostHeaderRemoteDataSource
import com.thirfir.data.datasource.remote.dto.PostHeaderDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import javax.inject.Inject

class PostHeaderRemoteDataSourceImpl @Inject constructor() : PostHeaderRemoteDataSource {

    override fun getPostHeadersDTO(bulletin: Int, page: Int): Flow<List<PostHeaderDTO>> = flow {
        try {
            val url = "https://portal.koreatech.ac.kr/ctt/bb/bulletin?b=${bulletin}&ln=${page}&ls=20"
            val doc = Jsoup.connect(url).get()
            val postList = getPostHeaderWithDoc(doc)

            emit(postList)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }.flowOn(Dispatchers.IO)

    private fun getPostHeaderWithDoc(document: Document): List<PostHeaderDTO> {
        val newsList = mutableListOf<PostHeaderDTO>()
        val newsListDoc = document.select("table#boardTypeList > tbody > tr")

        repeat(newsListDoc.size) {
            newsListDoc[it].apply {
                val pid = select("td.bc-s-post_seq").text().toInt()
                val title = select("td.bc-s-title div span").attr("title")
                val date = select("td.bc-s-cre_dt").text()
                val author = select("td.bc-s-cre_user_name").text()
                val category = select("td.bc-s-prefix").text()
                val views = select("td.bc-s-visit_cnt").text()
                val isTopFixed = attr("data-name").isNotEmpty()

                newsList.add(
                    PostHeaderDTO(
                        pid = pid,
                        title = title,
                        date = date,
                        author = author,
                        category = category,
                        views = views,
                        isTopFixed = isTopFixed
                    )
                )
            }
        }

        return newsList.toList()
    }
}