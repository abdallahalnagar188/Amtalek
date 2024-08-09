package eramo.amtalek.domain.repository

import eramo.amtalek.data.remote.dto.property.SendToBrokerResponse
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow

interface SendToBrokerRepository {
    suspend fun sendToBroker(vendorId: String?,name: String?,email: String?,phone: String?,message: String?,vendorType: String?): Flow<Resource<SendToBrokerResponse>>
}