package eramo.amtalek.domain.repository

import androidx.paging.PagingSource
import eramo.amtalek.data.remote.dto.broker.entity.BrokersResponse
import eramo.amtalek.data.remote.dto.broker.entity.DataX

interface BrokersRepo {
    suspend fun getBrokersFromRemote(): BrokersResponse
    fun getBrokersPagingSource(): PagingSource<Int, DataX>

}