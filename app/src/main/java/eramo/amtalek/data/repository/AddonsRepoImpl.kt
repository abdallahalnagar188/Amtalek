package eramo.amtalek.data.repository

import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.adons.AddonsResponse
import eramo.amtalek.domain.repository.AddonsRepo
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.state.ApiState
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.toResultFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AddonsRepoImpl(private val apiService: AmtalekApi): AddonsRepo {
    override suspend fun getAddons(): Flow<Resource<AddonsResponse>> {

        return flow {
            val result = toResultFlow {
             apiService.getAddons(UserUtil.getUserToken())
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