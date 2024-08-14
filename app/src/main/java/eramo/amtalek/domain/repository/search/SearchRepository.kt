package eramo.amtalek.domain.repository.search

import androidx.paging.PagingData
import eramo.amtalek.data.remote.dto.myHome.sliders.HomeSlidersResponse
import eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel
import eramo.amtalek.domain.search.SearchResponseModel
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody

interface SearchRepository {
    suspend fun search(
        keyword: RequestBody?,
        city: RequestBody?,
        country: RequestBody?,
        currency: RequestBody?,
        finishing: RequestBody?,
        maxArea: RequestBody?,
        minArea: RequestBody?,
        maxPrice: RequestBody?,
        minPrice: RequestBody?,
        minBathes: RequestBody?,
        minBeds: RequestBody?,
        priceArrangeKeys: RequestBody?,
        propertyType: RequestBody?,
        purpose: RequestBody?,
        region: RequestBody?,
        subRegion: RequestBody?,
        amenities: RequestBody?,
    ): Flow<PagingData<PropertyModel>>

    suspend fun getSearchResultSlider():Flow<Resource<HomeSlidersResponse>>

}