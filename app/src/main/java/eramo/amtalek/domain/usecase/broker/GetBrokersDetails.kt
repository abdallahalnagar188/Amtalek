package eramo.amtalek.domain.usecase.broker

import eramo.amtalek.domain.repository.BrokersDetailsRepo
import javax.inject.Inject

class GetBrokersDetails @Inject constructor(private val brokersDetailsRepo: BrokersDetailsRepo) {
    suspend operator fun invoke(id:Int) = brokersDetailsRepo.getBrokersDetailsFromRemote(id)

}