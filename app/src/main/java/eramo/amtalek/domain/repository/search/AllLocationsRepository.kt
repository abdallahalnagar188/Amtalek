package eramo.amtalek.domain.repository.search

import eramo.amtalek.data.remote.dto.search.alllocations.AllLocationsResponse
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow

interface AllLocationsRepository {
    suspend fun getAllLocations():Flow<Resource<AllLocationsResponse>>
}