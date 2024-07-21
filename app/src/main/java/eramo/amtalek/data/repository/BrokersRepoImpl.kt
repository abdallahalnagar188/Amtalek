package eramo.amtalek.data.repository

import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.broker.entity.BrokersResponse
import eramo.amtalek.domain.repository.BrokersRepo


class BrokersRepoImpl(private val apiService: AmtalekApi) : BrokersRepo {
    override suspend fun getBrokersFromRemote(): BrokersResponse = apiService.getBrokers()
}