package eramo.amtalek.domain.repository.certaria

import eramo.amtalek.data.remote.dto.property.newResponse.property_finishing.PropertyFinishingResponse
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow

interface PropertyFinishingRepository {
    suspend fun getPropertyFinishing():Flow<Resource<PropertyFinishingResponse>>
}