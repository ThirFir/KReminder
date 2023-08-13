package com.thirfir.data.datasource.remote.impl

import com.thirfir.data.datasource.remote.PostHeaderRemoteDataSource
import com.thirfir.data.datasource.remote.dto.PostHeaderDTO
import com.thirfir.domain.BASE_URL
import com.thirfir.domain.IoDispatcher
import com.thirfir.domain.addQueryString
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import javax.inject.Inject

class PostHeaderRemoteDataSourceImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): PostHeaderRemoteDataSource {

    override fun getPostHeadersDTO(bulletin: Int, page: Int): Flow<List<PostHeaderDTO>> = flow {
        try {
            val url = BASE_URL.addQueryString("b", bulletin).addQueryString("ln", page).addQueryString("ls", 20)
            val doc = Jsoup.connect(url).get()
            val postList = getPostHeaderWithDoc(doc)

            emit(postList)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }.flowOn(ioDispatcher)

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