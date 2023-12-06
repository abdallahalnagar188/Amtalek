package eramo.amtalek.domain.repository

import eramo.amtalek.domain.model.property.PropertyDetailsModel
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow

interface PropertyRepository {

    suspend fun getPropertyDetails(propertyId:String): Flow<Resource<PropertyDetailsModel>>

}