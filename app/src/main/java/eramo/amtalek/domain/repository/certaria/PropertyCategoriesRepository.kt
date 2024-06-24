package eramo.amtalek.domain.repository.certaria

import eramo.amtalek.data.remote.dto.property.newResponse.property_categories.PropertyCategoriesResponse
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow

interface PropertyCategoriesRepository {
    suspend fun getPropertyCategories():Flow<Resource<PropertyCategoriesResponse>>
}