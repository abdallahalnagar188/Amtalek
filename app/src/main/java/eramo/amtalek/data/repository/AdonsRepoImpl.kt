package eramo.amtalek.data.repository

import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.adons.AdonsResponse
import eramo.amtalek.domain.repository.AdonsRepo
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.state.ApiState
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.toResultFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AdonsRepoImpl(private val apiService: AmtalekApi): AdonsRepo {
    override suspend fun getAdons(): Flow<Resource<AdonsResponse>> {

        return flow {
            val result = toResultFlow {
             apiService.getAdons(UserUtil.getUserToken())
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