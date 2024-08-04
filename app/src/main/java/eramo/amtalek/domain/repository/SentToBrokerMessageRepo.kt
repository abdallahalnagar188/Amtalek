package eramo.amtalek.domain.repository

import eramo.amtalek.data.remote.dto.contactedAgent.SentToBrokerMessageResponse
import eramo.amtalek.data.remote.dto.contactedAgent.message.ContactAgentsMessageResponse
import eramo.amtalek.data.remote.dto.contactedAgent.message.Message
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow

interface SentToBrokerMessageRepo {
    suspend fun sendToBrokerInChat(
        vendorId: String?,
        name: String?,
        email: String?,
        phone: String?,
        message: String?,
        vendorType: String
    ): Flow<Resource<SentToBrokerMessageResponse>>

}