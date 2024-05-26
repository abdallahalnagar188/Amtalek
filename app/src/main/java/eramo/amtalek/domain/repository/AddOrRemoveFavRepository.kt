package eramo.amtalek.domain.repository

import eramo.amtalek.data.remote.dto.fav.AddOrRemoveFavResponse
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow

interface AddOrRemoveFavRepository {
    suspend fun addOrRemoveFav(propertyId: Int):Flow<Resource<AddOrRemoveFavResponse>>
}