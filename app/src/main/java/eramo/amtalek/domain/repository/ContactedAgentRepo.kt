package eramo.amtalek.domain.repository

import eramo.amtalek.data.remote.dto.contactedAgent.ContactedAgentResponse
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow

interface ContactedAgentRepo {
    suspend fun getContactedAgents(): ContactedAgentResponse
}