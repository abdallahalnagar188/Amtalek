package eramo.amtalek.domain.repository.certaria

import eramo.amtalek.data.remote.dto.property.newResponse.property_finishing.PropertyFinishingResponse
import eramo.amtalek.data.remote.dto.property.newResponse.property_floor_finishing.PropertyFloorFinishingResponse
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow

interface PropertyFloorFinishingRepository {
    suspend fun getPropertyFloorFinishing():Flow<Resource<PropertyFloorFinishingResponse>>
}