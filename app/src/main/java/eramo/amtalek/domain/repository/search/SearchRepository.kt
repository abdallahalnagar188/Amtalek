package eramo.amtalek.domain.repository.search

import androidx.paging.PagingData
import eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel
import eramo.amtalek.domain.search.SearchResponseModel
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun search(
        keyword: String?,
        city: String?,
        country: String?,
        currency: Int?,
        finishing: String?,
        maxArea: String?,
        minArea: String?,
        maxPrice: String?,
        minPrice: String?,
        minBathes: String?,
        minBeds: String?,
        priceArrangeKeys: String?,
        propertyType: String?,
        purpose: String?,
        region: String?,
        subRegion: String?,
        amenities: String?,
    ): Flow<PagingData<PropertyModel>>
}