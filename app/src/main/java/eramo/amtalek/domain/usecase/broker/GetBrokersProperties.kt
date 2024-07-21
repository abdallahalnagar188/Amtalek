package eramo.amtalek.domain.usecase.broker

import eramo.amtalek.domain.repository.BrokersPropertiesRepo
import javax.inject.Inject

class GetBrokersProperties @Inject constructor(private val brokersProperties: BrokersPropertiesRepo){
    suspend operator fun invoke(id:Int) = brokersProperties.getBrokersPropertiesFromRemote(id)


}