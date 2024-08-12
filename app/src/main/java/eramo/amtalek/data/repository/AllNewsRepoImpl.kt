package eramo.amtalek.data.repository

import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.myHome.news.allnews.AllNewsResponse
import eramo.amtalek.domain.repository.AllNewsRepo
import eramo.amtalek.util.state.ApiState
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.toResultFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AllNewsRepoImpl(private val apiService: AmtalekApi): AllNewsRepo {
    override suspend fun getAllNews(): Flow<Resource<AllNewsResponse>> {

        return flow {
            val result = toResultFlow {
                apiService.getAllNews()
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