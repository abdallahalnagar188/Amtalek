package eramo.amtalek.data.repository

import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.brokersDetails.BrokersDetailsResponse
import eramo.amtalek.domain.repository.BrokersDetailsRepo
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.state.ApiState
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.toResultFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BrokersDetailsRepoImpl(private val apiService: AmtalekApi) : BrokersDetailsRepo {
    override suspend fun getBrokersDetailsFromRemote(id: Int): Flow<Resource<BrokersDetailsResponse>> {
        return flow {
            val result = toResultFlow {
                apiService.getBrokersDetails(id)
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