package eramo.amtalek.data.repository

import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.contactBrokerDetails.ContactUsResponse
import eramo.amtalek.domain.repository.ContactUsRepo
import eramo.amtalek.util.state.ApiState
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.toResultFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ContactRepository @Inject constructor(private val apiService: AmtalekApi) : ContactUsRepo {

    //    override suspend fun contactUs(
//        propertyId: Int,
//        brokerId: Int,
//        transactionType: String
//    ): Flow<Resource<ContactUsResponse>> {
//        return flow {
//            val flow = toResultFlow {
//                return@toResultFlow apiService.sendContactRequest(
//                    propertyId,
//                    brokerId,
//                    transactionType
//                )
//            }
//            flow.collect {
//                when (it) {
//                    is ApiState.Success -> {
//                        emit(Resource.Success(it.data))
//                    }
//
//                    is ApiState.Error -> {
//                        emit(Resource.Error(it.message!!))
//                    }
//
//                    is ApiState.Loading -> {
//                        emit(Resource.Loading())
//                    }
//
//                }
//            }
//        }
//    }
//
    override suspend fun contactUs(
        propertyId: Int,
        brokerId: Int,
        transactionType: String
    ): Flow<Resource<ContactUsResponse>> {
        TODO("Not yet implemented")
    }
}