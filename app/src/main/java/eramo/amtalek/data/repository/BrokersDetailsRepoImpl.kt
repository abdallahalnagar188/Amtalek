package eramo.amtalek.data.repository

import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.brokersDetails.BrokersDetailsResponse
import eramo.amtalek.domain.repository.BrokersDetailsRepo

class BrokersDetailsRepoImpl(private val apiService: AmtalekApi) : BrokersDetailsRepo {
    override suspend fun getBrokersDetailsFromRemote(id: Int): BrokersDetailsResponse =
        apiService.getBrokersDetails(id)

}