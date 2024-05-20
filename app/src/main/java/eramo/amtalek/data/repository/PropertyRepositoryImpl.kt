package eramo.amtalek.data.repository

import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.domain.model.property.PropertyDetailsModel
import eramo.amtalek.domain.repository.PropertyRepository
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.state.ApiState
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.toResultFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PropertyRepositoryImpl(private val amtalekApi: AmtalekApi) : PropertyRepository {

    override suspend fun getPropertyDetails(propertyId:String): Flow<Resource<PropertyDetailsModel>> {
        return flow {
            val result = toResultFlow {
                amtalekApi.getPropertyDetails(
                    if (UserUtil.isUserLogin()) UserUtil.getUserToken() else null,
                    propertyId = propertyId)
            }
            result.collect {
                when (it) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(it.message!!))
                    is ApiState.Success -> {
                        val model = it.data?.toPropertyDetailsModel()
                        emit(Resource.Success(model))
                    }
                }
            }
        }
    }
}