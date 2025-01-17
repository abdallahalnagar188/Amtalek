package eramo.amtalek.data.repository.certaria

import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.property.newResponse.property_finishing.PropertyFinishingResponse
import eramo.amtalek.domain.repository.certaria.PropertyFinishingRepository
import eramo.amtalek.util.state.ApiState
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.toResultFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PropertyFinishingRepositoryImpl @Inject constructor(
    val amtalekApi: AmtalekApi
):PropertyFinishingRepository {
    override suspend fun getPropertyFinishing(): Flow<Resource<PropertyFinishingResponse>> {
        return flow {
            val result = toResultFlow { amtalekApi.getPropertyFinishing() }
            result.collect(){
                when(it){
                    is ApiState.Success -> {
                        emit(Resource.Success(it.data))
                    }
                    is ApiState.Error -> {
                        emit(Resource.Error(it.message!!))
                    }
                    is ApiState.Loading -> {
                        emit(Resource.Loading())
                    }
                }
            }
        }
    }
}