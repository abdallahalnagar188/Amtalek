package eramo.amtalek.domain.repository

import eramo.amtalek.data.remote.dto.brokersProperties.BrokersPropertyResponse

interface BrokersPropertiesRepo {
    suspend fun getBrokersPropertiesFromRemote(id:Int): BrokersPropertyResponse

}