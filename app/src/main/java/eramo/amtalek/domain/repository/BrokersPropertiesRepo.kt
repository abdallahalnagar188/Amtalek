package eramo.amtalek.domain.repository

import eramo.amtalek.data.remote.dto.brokersProperties.BrokersPropertyResponse
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow

interface BrokersPropertiesRepo {
    suspend fun getBrokersPropertiesFromRemote(id:Int): Flow<Resource<BrokersPropertyResponse>>

}