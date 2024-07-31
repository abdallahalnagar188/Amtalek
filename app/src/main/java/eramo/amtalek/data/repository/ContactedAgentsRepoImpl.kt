package eramo.amtalek.data.repository

import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.contactedAgent.ContactedAgentResponse
import eramo.amtalek.domain.repository.ContactedAgentRepo
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.state.ApiState
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.toResultFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ContactedAgentsRepoImpl(private val amtalekApi: AmtalekApi) : ContactedAgentRepo {
    override suspend fun getContactedAgents(): ContactedAgentResponse {
        return amtalekApi.getContactedAgents(UserUtil.getUserToken())
    }
}