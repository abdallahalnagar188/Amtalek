package eramo.amtalek.data.repository.search

import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.search.searchform.SearchFilterationResponse
import eramo.amtalek.domain.repository.search.SearchFilltrationRepo
import eramo.amtalek.util.state.ApiState
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.toResultFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchFilltrationRepoImpl @Inject constructor(val amtalekApi: AmtalekApi) : SearchFilltrationRepo {

    override suspend fun getSearchFilteration(
        propertyType: String?,
        purpose: String?,
        finishing: String?,
        currency: String?,
        amenities: List<String>,
        minPrice: String?,
        maxPrice: String?,
        minArea: String?,
        maxArea: String?,
        minBedrooms: String?,
        minBathrooms: String?
    ): Flow<Resource<SearchFilterationResponse>> {
        return flow {
            val result = toResultFlow {
                amtalekApi.getSearchFilteration(
                    propertyType = propertyType?:"",
                    purpose = purpose?:"",
                    finishing = finishing?:"",
                    currency = currency?:"",
                    amenities = amenities,
                    minPrice = minPrice?:"",
                    maxPrice = maxPrice?:"",
                    minArea = minArea?:"",
                    maxArea = maxArea?:"",
                    minBeds = minBedrooms?:"",
                    minBathes = minBathrooms?:""
                )
            }
            result.collect {
                when (it) {
                    is ApiState.Success -> emit(Resource.Success(it.data))
                    is ApiState.Error -> emit(Resource.Error(it.message!!))
                    is ApiState.Loading -> emit(Resource.Loading())
                }
            }
        }
    }

}

