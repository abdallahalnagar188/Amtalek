package eramo.amtalek.data.repository.certaria

import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.property.newResponse.property_categories.PropertyCategoriesResponse
import eramo.amtalek.domain.repository.certaria.PropertyCategoriesRepository
import eramo.amtalek.util.state.ApiState
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.toResultFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PropertyCategoriesRepositoryImpl @Inject constructor(
    val amtalekApi: AmtalekApi
):PropertyCategoriesRepository {
    override suspend fun getPropertyCategories(): Flow<Resource<PropertyCategoriesResponse>> {
        return flow {
            val result = toResultFlow {
                amtalekApi.getPropertyCategories()
            }
            result.collect(){
                when(it){
                    is ApiState.Success->{
                        val model = it.data
                        emit(Resource.Success(model))
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