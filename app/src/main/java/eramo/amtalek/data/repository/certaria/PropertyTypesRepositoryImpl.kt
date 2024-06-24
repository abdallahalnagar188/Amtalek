package eramo.amtalek.data.repository.certaria

import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.property.newResponse.poperty_types.PropertyTypesResponse
import eramo.amtalek.domain.repository.certaria.PropertyTypesRepository
import eramo.amtalek.util.state.ApiState
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.toResultFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PropertyTypesRepositoryImpl @Inject constructor(
    val amtalekApi: AmtalekApi
):PropertyTypesRepository{
    override suspend fun getPropertyType(): Flow<Resource<PropertyTypesResponse>> {
        return flow {
            val result = toResultFlow {
                amtalekApi.getPropertyTypes()
            }
            result.collect(){
                when(it){
                    is ApiState.Success->{
                        emit(Resource.Success(it.data))
                    }
                    is ApiState.Error->{
                        emit(Resource.Error(it.message!!))
                    }
                    is ApiState.Loading->{
                        emit(Resource.Loading())
                    }
                }
            }
        }
    }
}