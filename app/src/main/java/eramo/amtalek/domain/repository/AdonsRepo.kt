package eramo.amtalek.domain.repository

import eramo.amtalek.data.remote.dto.adons.AdonsResponse
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow

interface AdonsRepo {
    suspend fun getAdons(): Flow<Resource<AdonsResponse>>
}