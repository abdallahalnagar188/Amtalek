package eramo.amtalek.data.repository

import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.contactBrokerDetails.ContactUsResponseInProperty
import eramo.amtalek.domain.repository.ContactUsRepo
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.state.ApiState
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.toResultFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ContactRepositoryImpl @Inject constructor(private val apiService: AmtalekApi) : ContactUsRepo {

    override suspend fun contactUs(
        propertyId: Int,
        brokerId: Int,
        transactionType: String,
        brokerType: String
    ): Flow<Resource<ContactUsResponseInProperty>> {

        return flow {
            val result = toResultFlow {
                apiService.sendContactRequest(
                    UserUtil.getUserToken(),
                    propertyId.toString(),
                    brokerId.toString(),
                    brokerType = brokerType,
                    transactionType = transactionType

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
