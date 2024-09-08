package eramo.amtalek.domain.repository

import eramo.amtalek.data.remote.dto.myHome.news.newsDetails.NewsDetailsResponseX
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow

interface NewsDetailsRepo {
    suspend fun getNewsDetails(id: String): Flow<Resource<NewsDetailsResponseX>>
}