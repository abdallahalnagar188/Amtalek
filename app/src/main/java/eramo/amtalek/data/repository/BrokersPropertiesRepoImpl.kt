package eramo.amtalek.data.repository

import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.brokersProperties.BrokersPropertyResponse
import eramo.amtalek.domain.repository.BrokersPropertiesRepo
import eramo.amtalek.util.state.ApiState
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.toResultFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BrokersPropertiesRepoImpl(private val api: AmtalekApi) : BrokersPropertiesRepo {
    override suspend fun getBrokersPropertiesFromRemote(id: Int): Flow<Resource<BrokersPropertyResponse>> {
        return flow {
            val result = toResultFlow {
                api.getBrokersProperties(id)
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