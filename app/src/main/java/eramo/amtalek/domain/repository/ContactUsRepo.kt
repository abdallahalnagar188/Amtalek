package eramo.amtalek.domain.repository

import eramo.amtalek.data.remote.dto.contactBrokerDetails.ContactUsResponseInProperty
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow

interface ContactUsRepo {
    suspend fun contactUs(
        propertyId: Int,
        brokerId: Int,
        brokerType: String,
        transactionType: String
    ): Flow<Resource<ContactUsResponseInProperty>>

}