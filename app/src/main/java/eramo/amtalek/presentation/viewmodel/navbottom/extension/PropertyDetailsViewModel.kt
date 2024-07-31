package eramo.amtalek.presentation.viewmodel.navbottom.extension

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.data.remote.dto.fav.AddOrRemoveFavResponse
import eramo.amtalek.data.remote.dto.property.newResponse.send_offer.SendOfferResponse
import eramo.amtalek.data.remote.dto.property.newResponse.send_prop_comment.SendPropertyCommentResponse
import eramo.amtalek.data.remote.dto.property.newResponse.submit_to_broker.SubmitToBrokerResponse
import eramo.amtalek.data.remote.dto.spceProperty.BrokerDetailsItem
import eramo.amtalek.domain.model.property.PropertyDetailsModel
import eramo.amtalek.domain.repository.AddOrRemoveFavRepository
import eramo.amtalek.domain.repository.PropertyRepository
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PropertyDetailsViewModel @Inject constructor(
    private val propertyRepository: PropertyRepository,
    private val addOrRemoveFavRepository: AddOrRemoveFavRepository
    ) : ViewModel() {

    private val _propertyDetailsState = MutableStateFlow<UiState<PropertyDetailsModel>>(UiState.Empty())
    val propertyDetailsState: StateFlow<UiState<PropertyDetailsModel>> = _propertyDetailsState


    private val _sendPropertyOfferState = MutableStateFlow<UiState<SendOfferResponse>>(UiState.Empty())
    val sendPropertyOfferState: StateFlow<UiState<SendOfferResponse>> = _sendPropertyOfferState

    private val _sendMessageToPropertyOwnerState = MutableStateFlow<UiState<SubmitToBrokerResponse>>(UiState.Empty())
    val sendMessageToPropertyOwnerState: StateFlow<UiState<SubmitToBrokerResponse>> = _sendMessageToPropertyOwnerState

    private val _sendCommentOnPropertyState = MutableStateFlow<UiState<SendPropertyCommentResponse>>(UiState.Empty())
    val sendCommentOnPropertyState: StateFlow<UiState<SendPropertyCommentResponse>> = _sendCommentOnPropertyState


    private val _favState = MutableStateFlow<UiState<AddOrRemoveFavResponse>>(UiState.Empty())
    val favState: StateFlow<UiState<AddOrRemoveFavResponse>> = _favState

    private val _loginData = MutableStateFlow<UiState<BrokerDetailsItem>>(UiState.Empty())
    val loginData: StateFlow<UiState<BrokerDetailsItem>> = _loginData


    fun performLogin() {
        viewModelScope.launch {
            _loginData.value = UiState.Loading()
            try {
                val brokerDetails = fetchBrokerDetails() // Replace with actual data fetching
                _loginData.value = UiState.Success(brokerDetails)
            } catch (e: Exception) {
            }
        }
    }

    private suspend fun fetchBrokerDetails(): BrokerDetailsItem {
        // Replace this with actual implementation to fetch broker details
        return BrokerDetailsItem(brokerType = "broker", hasPackage = "yes")
    }




    private var getPropertyDetailsJob: Job? = null
    private var sendCommentOnPropertyJob: Job? = null
    private var sendPropertyOfferJob: Job? = null
    private var sendMessageToPropertyOwnerJob: Job? = null

    fun cancelRequest() {
        getPropertyDetailsJob?.cancel()
    }
    fun addOrRemoveFav(propertyId: Int) {
        viewModelScope.launch {
            withContext(coroutineContext){
                addOrRemoveFavRepository.addOrRemoveFav(propertyId).collect(){result->
                    when(result){
                        is Resource.Success->{
                            _favState.value = UiState.Success(result.data)
                        }
                        is Resource.Error->{
                            _favState.value = UiState.Error(result.message!!)
                        }
                        is Resource.Loading->{
                            _favState.value = UiState.Loading()
                        }
                    }
                }

            }
        }

    }
    fun getPropertyDetails(propertyId:String) {
        getPropertyDetailsJob?.cancel()
        getPropertyDetailsJob = viewModelScope.launch {
            withContext(coroutineContext) {
                propertyRepository.getPropertyDetails(propertyId).collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _propertyDetailsState.value = UiState.Success(result.data)
                        }

                        is Resource.Error -> {
                            _propertyDetailsState.value =
                                UiState.Error(result.message!!)
                        }

                        is Resource.Loading -> {
                            _propertyDetailsState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }

    fun sendCommentOnProperty(
        propertyId: String,
        message: String,
        stars: Int,
        name: String,
        phone: String,
        email: String
    ){
        sendCommentOnPropertyJob?.cancel()
        sendCommentOnPropertyJob = viewModelScope.launch {
            withContext(coroutineContext){
                propertyRepository.sendCommentOnProperty(
                    propertyId = propertyId,
                    message = message,
                    stars = stars,
                    name = name,
                    phone = phone,
                    email = email
                ).collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _sendCommentOnPropertyState.value = UiState.Success(result.data)
                        }

                        is Resource.Error -> {
                            _sendCommentOnPropertyState.value =
                                UiState.Error(result.message!!)
                        }

                        is Resource.Loading -> {
                            _sendCommentOnPropertyState.value = UiState.Loading()
                        }
                    }
                }

            }
        }
    }

    fun sendMessageToPropertyOwner(
        propertyId: String,
        message: String,
        vendorId:String,
        name:String,
        phone:String,
        email:String,
        vendorType:String
    ){
        sendMessageToPropertyOwnerJob?.cancel()
        sendMessageToPropertyOwnerJob = viewModelScope.launch {
            withContext(coroutineContext){
                propertyRepository.sendMessageToPropertyOwner(
                    propertyId = propertyId,
                    message = message,
                    vendorId = vendorId,
                    name = name,
                    phone = phone,
                    email = email,
                    vendorType = vendorType
                ).collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _sendMessageToPropertyOwnerState.value = UiState.Success(result.data)
                        }

                        is Resource.Error -> {
                            _sendMessageToPropertyOwnerState.value =
                                UiState.Error(result.message!!)
                        }

                        is Resource.Loading -> {
                            _sendMessageToPropertyOwnerState.value = UiState.Loading()
                        }
                    }
                }

            }
        }
    }

    fun sendPropertyOffer(
        propertyId: String,
        vendorId:String,
        name:String,
        phone:String,
        email:String,
        offer:String,
        offerType:String
    ){
        sendPropertyOfferJob?.cancel()
        sendPropertyOfferJob = viewModelScope.launch {
            withContext(coroutineContext){
                propertyRepository.sendPropertyOffer(
                    propertyId = propertyId,
                    vendorId = vendorId,
                    name = name,
                    phone = phone,
                    email = email,
                    offer = offer,
                    offerType = offerType
                ).collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _sendPropertyOfferState.value = UiState.Success(result.data)
                        }

                        is Resource.Error -> {
                            _sendPropertyOfferState.value =
                                UiState.Error(result.message!!)
                        }

                        is Resource.Loading -> {
                            _sendPropertyOfferState.value = UiState.Loading()
                        }
                    }
                }

            }
        }
    }


}