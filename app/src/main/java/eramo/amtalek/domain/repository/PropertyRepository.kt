package eramo.amtalek.domain.repository

import com.google.common.math.IntMath
import eramo.amtalek.data.remote.dto.property.SendToBrokerResponse
import eramo.amtalek.data.remote.dto.property.newResponse.send_offer.SendOfferResponse
import eramo.amtalek.data.remote.dto.property.newResponse.send_prop_comment.SendPropertyCommentResponse
import eramo.amtalek.data.remote.dto.property.newResponse.submit_to_broker.SubmitToBrokerResponse
import eramo.amtalek.domain.model.property.PropertyDetailsModel
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow

interface PropertyRepository {

    suspend fun getPropertyDetails(propertyId:String): Flow<Resource<PropertyDetailsModel>>
    suspend fun sendCommentOnProperty(
        propertyId: String,
        message: String,
        stars: Int,
        name:String,
        phone:String,
        email:String,
    ):Flow<Resource<SendPropertyCommentResponse>>
    suspend fun sendMessageToPropertyOwner(
        propertyId: String,
        message: String,
        vendorId:String,
        name:String,
        phone:String,
        email:String,
        vendorType:String
    ):Flow<Resource<SubmitToBrokerResponse>>
    suspend fun sendPropertyOffer(
        propertyId: String,
        vendorId:String,
        name:String,
        phone:String,
        email:String,
        offer:String,
        offerType:String
    ):Flow<Resource<SendOfferResponse>>

}