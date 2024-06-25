package eramo.amtalek.domain.repository.certaria

import eramo.amtalek.data.remote.dto.property.newResponse.amenities.AmenitiesResponse
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow

interface PropertyAmenitiesRepository {
    suspend fun getAmenities():Flow<Resource<AmenitiesResponse>>
}