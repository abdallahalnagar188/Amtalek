package eramo.amtalek.data.repository

import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.contactedAgent.message.ContactAgentsMessageResponse
import eramo.amtalek.domain.repository.ContactedAgentsMessageRepo
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.state.ApiState
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.toResultFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ContactedAgentsMessageRepositoryImpl @Inject constructor(private val apiService: AmtalekApi) : ContactedAgentsMessageRepo {

    override suspend fun getContactedAgentsMessage(agentId: String): Flow<Resource<ContactAgentsMessageResponse>> {
        return flow {
            val result = toResultFlow {
                apiService.getContactedAgentsMessage(
                    userToken = UserUtil.getUserToken(),
                    agentId = agentId
                )
            }
            result.collect {
                when (it) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(it.message!!))
                    is ApiState.Success -> {
                        val model = it.data
                        emit(Resource.Success(model))
                    }
                }
            }
        }
    }
}
