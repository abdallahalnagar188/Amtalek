package eramo.amtalek.data.repository

import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.brokersProperties.BrokersPropertyResponse
import eramo.amtalek.domain.repository.BrokersPropertiesRepo

class BrokersPropertiesRepoImpl(private val api: AmtalekApi) : BrokersPropertiesRepo {

    override suspend fun getBrokersPropertiesFromRemote(id: Int): BrokersPropertyResponse =
        api.getBrokersProperties(id)

}