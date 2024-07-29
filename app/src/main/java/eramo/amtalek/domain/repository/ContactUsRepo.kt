package eramo.amtalek.domain.repository

import eramo.amtalek.data.remote.dto.contactBrokerDetails.ContactUsResponse
import eramo.amtalek.data.remote.dto.fav.AddOrRemoveFavResponse
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow

interface ContactUsRepo {
    suspend fun contactUs(
        propertyId: Int,
        brokerId: Int,
        transactionType: String
    ): Flow<Resource<ContactUsResponse>>

}