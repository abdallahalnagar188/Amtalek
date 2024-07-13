package eramo.amtalek.data.repository.search

import androidx.paging.Pager
import androidx.paging.PagingData
import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.paging.PagingSearch
import eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel
import eramo.amtalek.domain.repository.search.SearchRepository
import eramo.amtalek.domain.search.SearchResponseModel
import eramo.amtalek.util.pagingConfig
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    val amtalekApi: AmtalekApi
): SearchRepository {
    override suspend fun search(
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
        subRegion: String?
    ): Flow<PagingData<PropertyModel>> {
        return Pager(
            config = pagingConfig(),
            pagingSourceFactory = {
                PagingSearch(
                    amtalekApi = amtalekApi,
                    keyword = keyword?:"",
                    city = city?:"",
                    country = country?:"",
                    currency = currency?:0,
                    finishing = finishing?:"",
                    maxArea = maxArea?:"",
                    minArea = minArea?:"",
                    maxPrice = maxPrice?:"",
                    minPrice = minPrice?:"",
                    minBathes = minBathes?:"",
                    minBeds = minBeds?:"",
                    priceArrangeKeys = priceArrangeKeys?:"",
                    propertyType = propertyType?:"",
                    purpose = purpose?:"",
                    region = region?:"",
                    subRegion = subRegion?:"",
                    )
            }
        ).flow
    }

}