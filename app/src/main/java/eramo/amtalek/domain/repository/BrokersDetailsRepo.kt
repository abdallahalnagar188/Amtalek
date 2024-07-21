package eramo.amtalek.domain.repository

import eramo.amtalek.data.remote.dto.brokersDetails.BrokersDetailsResponse

interface BrokersDetailsRepo {
    suspend fun getBrokersDetailsFromRemote(id:Int): BrokersDetailsResponse
}