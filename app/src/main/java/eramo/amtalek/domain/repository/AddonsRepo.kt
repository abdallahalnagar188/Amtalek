package eramo.amtalek.domain.repository

import eramo.amtalek.data.remote.dto.adons.AddonsResponse
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow

interface AddonsRepo {
    suspend fun getAddons(): Flow<Resource<AddonsResponse>>
}