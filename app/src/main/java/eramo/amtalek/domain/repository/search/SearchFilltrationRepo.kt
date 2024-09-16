package eramo.amtalek.domain.repository.search

import eramo.amtalek.data.remote.dto.search.searchform.SearchFilterationResponse
import eramo.amtalek.util.state.Resource
import io.kashier.sdk.Core.model.Response.GenericResponse.Response
import kotlinx.coroutines.flow.Flow

interface SearchFilltrationRepo {
    suspend fun getSearchFilteration(
        propertyType: String? = "",
        purpose: String? = "",
        finishing: String? = "",
        currency: String? = "",
        amenities: List<Int?> = emptyList(), // List of integers for amenities
        minPrice: String? = "",
        maxPrice: String? = "",
        minArea: String? = "",
        maxArea: String? = "",
        minBedrooms: String ?= "",
        minBathrooms: String ?= "",
        city: String? = ""
    ): Flow<Resource<SearchFilterationResponse>>
}
