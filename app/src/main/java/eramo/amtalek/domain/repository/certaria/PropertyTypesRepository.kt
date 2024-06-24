package eramo.amtalek.domain.repository.certaria

import eramo.amtalek.data.remote.dto.property.newResponse.poperty_types.PropertyTypesResponse
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow

interface PropertyTypesRepository {
    suspend fun getPropertyType():Flow<Resource<PropertyTypesResponse>>
}