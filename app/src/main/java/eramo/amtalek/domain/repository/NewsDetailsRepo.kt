package eramo.amtalek.domain.repository

import eramo.amtalek.data.remote.dto.myHome.news.NewsDetailsResponse
import eramo.amtalek.util.state.Resource
import io.kashier.sdk.Core.model.Response.GenericResponse.Response
import kotlinx.coroutines.flow.Flow

interface NewsDetailsRepo {
    suspend fun getNewsDetails(id: String): Flow<Resource<NewsDetailsResponse>>
}