package eramo.amtalek.data.repository

import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.myHome.news.NewsDetailsResponse
import eramo.amtalek.domain.repository.NewsDetailsRepo
import eramo.amtalek.util.state.ApiState
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.toResultFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NewsDetailsRepoImpl(private val api: AmtalekApi): NewsDetailsRepo {
    override suspend fun getNewsDetails(id: String): Flow<Resource<NewsDetailsResponse>> {
        return flow {
            val result = toResultFlow {
                api.getNewsDetails(id)
            }
            result.collect {
                when (it) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(it.message!!))
                    is ApiState.Success -> {
                        val model = it.data
                        emit(Resource.Success(model))
                    }
                }
            }
        }
    }
}