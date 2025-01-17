package eramo.amtalek.data.repository

import android.util.Log
import com.google.gson.GsonBuilder
import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.myHome.sliders.HomeSlidersResponse
import eramo.amtalek.data.remote.dto.property.newResponse.send_offer.SendOfferResponse
import eramo.amtalek.data.remote.dto.property.newResponse.send_prop_comment.SendPropertyCommentResponse
import eramo.amtalek.data.remote.dto.property.newResponse.submit_to_broker.SubmitToBrokerResponse
import eramo.amtalek.domain.model.property.PropertyDetailsModel
import eramo.amtalek.domain.repository.PropertyRepository
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.state.ApiState
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.toResultFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PropertyRepositoryImpl(private val amtalekApi: AmtalekApi) : PropertyRepository {

    override suspend fun getPropertyDetails(propertyId:String): Flow<Resource<PropertyDetailsModel>> {
        return flow {
            val result = toResultFlow {
                amtalekApi.getPropertyDetails(
                    if (UserUtil.isUserLogin()) UserUtil.getUserToken() else null,
                    propertyId = propertyId)
            }
            result.collect {
                when (it) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(it.message!!))
                    is ApiState.Success -> {
                        val model = it.data?.toPropertyDetailsModel()
                        emit(Resource.Success(model))
                    }
                }
            }
        }
    }

    override suspend fun sendCommentOnProperty(
        propertyId: String,
        message: String,
        stars: Int,
        name: String,
        phone: String,
        email: String
    ): Flow<Resource<SendPropertyCommentResponse>> {
        return flow {
            val result = toResultFlow {
                amtalekApi.sendPropertyComment(
                    userToken = (if (UserUtil.isUserLogin())UserUtil.getUserToken() else null).toString(),
                    propertyId = propertyId,
                    message = message,
                    stars = stars,
                    name = name,
                    phone = phone,
                    email = email
                )
            }
            result.collect {
                when (it) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(it.message!!))
                    is ApiState.Success -> { emit(Resource.Success(it.data)) }
                }
            }
        }
    }

    override suspend fun sendMessageToPropertyOwner(
        propertyId: String,
        message: String,
        vendorId: String,
        name: String,
        phone: String,
        email: String,
        vendorType: String
    ): Flow<Resource<SubmitToBrokerResponse>> {
        return flow {
            val result = toResultFlow {
                amtalekApi.sendMessageToPropertyOwner(
                    userToken =  if (UserUtil.isUserLogin()) UserUtil.getUserToken() else null,
                    propertyId = propertyId,
                    message = message,
                    vendorId = vendorId,
                    name = name,
                    phone = phone,
                    email = email,
                    vendorType = vendorType
                )
            }
            result.collect {
                when (it) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> {
                        Log.e("aaaa", it.message.toString())
                        emit(Resource.Error(it.message!!))
                    }
                    is ApiState.Success -> {
                        Log.e("bbbb", it.message.toString())
                        emit(Resource.Success(it.data))
                    }
                }
            }
        }
    }

    override suspend fun sendPropertyOffer(
        propertyId: String,
        vendorId: String,
        name: String,
        phone: String,
        email: String,
        offer: String,
        offerType: String
    ): Flow<Resource<SendOfferResponse>> {
        return flow {
            val result = toResultFlow {
                amtalekApi.sendPropertyOffer(
                    if (UserUtil.isUserLogin()) UserUtil.getUserToken() else null,
                    propertyId = propertyId,
                    vendorId = vendorId,
                    name = name,
                    phone = phone,
                    email = email,
                    offer = offer,
                    offerType = offerType
                )
            }
            result.collect {
                when (it) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> {
                        emit(Resource.Error(it.message!!))}
                    is ApiState.Success -> { emit(Resource.Success(it.data)) }
                }
            }
        }
    }

    override suspend fun getPropertySlider(): Flow<Resource<HomeSlidersResponse>> {
        return flow {
            val result =
                toResultFlow { amtalekApi.getPropertySlider() }
            result.collect() {
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