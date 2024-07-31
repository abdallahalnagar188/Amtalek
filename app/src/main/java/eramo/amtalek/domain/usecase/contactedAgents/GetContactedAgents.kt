package eramo.amtalek.domain.usecase.contactedAgents

import eramo.amtalek.data.remote.dto.contactedAgent.ContactedAgentResponse
import eramo.amtalek.domain.repository.BrokersRepo
import eramo.amtalek.domain.repository.ContactedAgentRepo
import javax.inject.Inject

class GetContactedAgents @Inject constructor(private val contactedAgentRepo: ContactedAgentRepo) {
    suspend operator fun invoke() = contactedAgentRepo.getContactedAgents()

}