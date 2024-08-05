package eramo.amtalek.data.repository

import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.contactedAgent.SentToBrokerMessageResponse
import eramo.amtalek.data.remote.dto.contactedAgent.message.Message
import eramo.amtalek.domain.repository.SendToBrokerRepository
import eramo.amtalek.domain.repository.SentToBrokerMessageRepo
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.state.ApiState
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.toResultFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SentToBrokerMessageRepoImpl @Inject constructor(
    val amtalekApi: AmtalekApi
) : SentToBrokerMessageRepo {
    override suspend fun sendToBrokerInChat(
        vendorId: String?,
        name: String?,
        email: String?,
        phone: String?,
        message: String?,
        vendorType: String
    ): Flow<Resource<SentToBrokerMessageResponse>> {
        return flow {
            val result = toResultFlow {
                amtalekApi.sendToBrokerInChat(
                    if (UserUtil.isUserLogin()) UserUtil.getUserToken() else null,
                    vendorId = vendorId,
                    name = name,
                    email = email,
                    phone = phone,
                    message = message,
                    vendorType = vendorType

                )
            }
            result.collect() {
                when (it) {
                    is ApiState.Success -> {
                        emit(Resource.Success(it.data))
                    }

                    is ApiState.Error -> {
                        emit(Resource.Error(it.message!!))
                    }

                    is ApiState.Loading -> {
                        emit(Resource.Loading())
                    }
                }
            }
        }
    }

}