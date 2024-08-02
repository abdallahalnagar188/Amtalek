package eramo.amtalek.data.repository

import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.contactedAgent.ContactedAgentResponse
import eramo.amtalek.data.remote.dto.property.newResponse.send_offer.SendOfferResponse
import eramo.amtalek.data.remote.dto.property.newResponse.send_prop_comment.SendPropertyCommentResponse
import eramo.amtalek.data.remote.dto.property.newResponse.submit_to_broker.SubmitToBrokerResponse
import eramo.amtalek.domain.model.property.PropertyDetailsModel
import eramo.amtalek.domain.repository.ContactedAgentRepo
import eramo.amtalek.domain.repository.PropertyRepository
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.state.ApiState
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.toResultFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ContactedAgentsRepositoryImpl(private val amtalekApi: AmtalekApi) : ContactedAgentRepo {

    override suspend fun getContactedAgents(): ContactedAgentResponse{
//        return flow {
//            val result = toResultFlow {
//                amtalekApi.getContactedAgents(
//                    if (UserUtil.isUserLogin()) UserUtil.getUserToken() else null
//                )
//            }
//            result.collect {
//                when (it) {
//                    is ApiState.Loading -> emit(Resource.Loading())
//                    is ApiState.Error -> emit(Resource.Error(it.message!!))
//                    is ApiState.Success -> {
//                        val model = it.data
//                        emit(Resource.Success(model))
//                    }
//                }
//            }
//        }
        return amtalekApi.getContactedAgents(UserUtil.getUserToken())
    }
}