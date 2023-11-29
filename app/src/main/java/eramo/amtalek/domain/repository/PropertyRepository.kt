package eramo.amtalek.domain.repository

import eramo.amtalek.data.remote.dto.property.PropertyDetailsResponse
import eramo.amtalek.domain.model.auth.OnBoardingModel
import eramo.amtalek.domain.model.property.PropertyDetailsModel
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow

interface PropertyRepository {

    suspend fun getPropertyDetails(): Flow<Resource<PropertyDetailsModel>>

}