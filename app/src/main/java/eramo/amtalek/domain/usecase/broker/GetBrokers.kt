package eramo.amtalek.domain.usecase.broker

import eramo.amtalek.domain.repository.BrokersRepo
import javax.inject.Inject

class GetBrokers @Inject constructor(private val brokersRepo: BrokersRepo) {
    suspend operator fun invoke() = brokersRepo.getBrokersFromRemote()

}