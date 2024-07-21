package eramo.amtalek.domain.repository

import eramo.amtalek.data.remote.dto.broker.entity.BrokersResponse

interface BrokersRepo {
    suspend fun getBrokersFromRemote(): BrokersResponse
}