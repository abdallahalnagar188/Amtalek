package eramo.amtalek.domain.repository.certaria

import eramo.amtalek.data.remote.dto.property.newResponse.property_purpose.PropertyPurposeResponse
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow

interface PropertyPurposeRepository {
    suspend fun getPropertyPurpose():Flow<Resource<PropertyPurposeResponse>>
}