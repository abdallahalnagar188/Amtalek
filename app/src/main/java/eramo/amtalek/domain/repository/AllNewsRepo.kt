package eramo.amtalek.domain.repository

import eramo.amtalek.data.remote.dto.myHome.news.HomeNewsResponse
import eramo.amtalek.data.remote.dto.myHome.news.allnews.AllNewsResponse
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow

interface AllNewsRepo {
    suspend fun getAllNews(): Flow<Resource<AllNewsResponse>>
}