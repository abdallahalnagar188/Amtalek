package eramo.amtalek.domain.repository

import androidx.paging.PagingSource
import eramo.amtalek.data.remote.dto.brokersProperties.newBrokerProps.AllBrokersPropertysResponse
import eramo.amtalek.data.remote.dto.brokersProperties.newBrokerProps.DataX
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow

interface BrokersPropertiesRepo {
    suspend fun getBrokersPropertiesFromRemote(id:Int): Flow<Resource<AllBrokersPropertysResponse>>
    fun getAllBrokersPropertiesFromRemote(id: Int): PagingSource<Int, DataX>


}