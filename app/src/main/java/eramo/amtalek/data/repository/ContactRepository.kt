package eramo.amtalek.data.repository

import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.contactBrokerDetails.ContactBrokerDetailsInPropertyDetails
import eramo.amtalek.data.remote.dto.general.ResultDto
import retrofit2.Response
import javax.inject.Inject

class ContactRepository @Inject constructor(private val apiService: AmtalekApi) {
    suspend fun sendContactRequest(
        propertyId: Int,
        brokerId: Int,
        transactionType: String
    ): Response<ResultDto> {
        val request = ContactBrokerDetailsInPropertyDetails(
            property_id = propertyId,
            broker_id = brokerId,
            transaction_type = transactionType
        )
        return apiService.sendContactRequest(request)
    }
}
