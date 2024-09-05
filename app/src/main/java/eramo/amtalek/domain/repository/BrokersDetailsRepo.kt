package eramo.amtalek.domain.repository

import eramo.amtalek.data.remote.dto.brokersDetails.BrokersDetailsResponse
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow

interface BrokersDetailsRepo {
    suspend fun getBrokersDetailsFromRemote(id:Int): Flow<Resource<BrokersDetailsResponse>>
}