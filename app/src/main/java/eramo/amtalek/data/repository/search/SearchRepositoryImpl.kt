package eramo.amtalek.data.repository.search

import androidx.paging.Pager
import androidx.paging.PagingData
import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.myHome.sliders.HomeSlidersResponse
import eramo.amtalek.data.remote.paging.PagingSearch
import eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel
import eramo.amtalek.domain.repository.search.SearchRepository
import eramo.amtalek.domain.search.SearchResponseModel
import eramo.amtalek.util.pagingConfig
import eramo.amtalek.util.state.ApiState
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.toResultFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.RequestBody
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    val amtalekApi: AmtalekApi
): SearchRepository {
    override suspend fun search(
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
        amenities:RequestBody?
    ): Flow<PagingData<PropertyModel>> {
        return Pager(
            config = pagingConfig(),
            pagingSourceFactory = {
                PagingSearch(
                    amtalekApi = amtalekApi,
                    keyword = keyword,
                    city = city,
                    country = country,
                    currency = currency,
                    finishing = finishing,
                    maxArea = maxArea,
                    minArea = minArea,
                    maxPrice = maxPrice,
                    minPrice = minPrice,
                    minBathes = minBathes,
                    minBeds = minBeds,
                    priceArrangeKeys = priceArrangeKeys,
                    propertyType = propertyType,
                    purpose = purpose,
                    region = region,
                    subRegion = subRegion,
                    amenities = amenities
                    )
            }
        ).flow
    }

    override suspend fun getSearchResultSlider(): Flow<Resource<HomeSlidersResponse>> {
        return flow {
            val result =
                toResultFlow { amtalekApi.getResultsSlider() }
            result.collect() {
                when (it) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(it.message!!))
                    is ApiState.Success -> {
                        val model = it.data
                        emit(Resource.Success(model))
                    }
                }
            }
        }
    }

}