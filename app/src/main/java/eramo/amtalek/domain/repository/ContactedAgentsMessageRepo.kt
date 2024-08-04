package eramo.amtalek.domain.repository

import eramo.amtalek.data.remote.dto.contactedAgent.message.ContactAgentsMessageResponse
import eramo.amtalek.data.remote.dto.contactedAgent.message.Message
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow

interface ContactedAgentsMessageRepo {
    suspend fun getContactedAgentsMessage(
        agentId: String
    ): Flow<Resource<ContactAgentsMessageResponse>>
}