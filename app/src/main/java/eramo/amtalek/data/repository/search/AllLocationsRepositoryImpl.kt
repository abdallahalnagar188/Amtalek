package eramo.amtalek.data.repository.search

import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.search.alllocations.AllLocationsResponse
import eramo.amtalek.domain.repository.search.AllLocationsRepository
import eramo.amtalek.util.state.ApiState
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.toResultFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AllLocationsRepositoryImpl @Inject constructor(
    val amtalekApi: AmtalekApi
):AllLocationsRepository {
    override suspend fun getAllLocations(): Flow<Resource<AllLocationsResponse>> {
        return flow {
            val response = toResultFlow { amtalekApi.getAllLocations() }
            response.collect(){
                when(it){
                    is ApiState.Success -> emit(Resource.Success(it.data))
                    is ApiState.Error -> emit(Resource.Error(it.message!!))
                    is ApiState.Loading -> emit(Resource.Loading())
                }
            }
        }
    }
}