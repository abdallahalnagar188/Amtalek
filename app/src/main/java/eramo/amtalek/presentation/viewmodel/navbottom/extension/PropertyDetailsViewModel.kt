package eramo.amtalek.presentation.viewmodel.navbottom.extension

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.data.remote.dto.property.newResponse.send_prop_comment.SendPropertyCommentResponse
import eramo.amtalek.domain.model.property.PropertyDetailsModel
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
    private val repository: PropertyRepository
) : ViewModel() {

    private val _propertyDetailsState = MutableStateFlow<UiState<PropertyDetailsModel>>(UiState.Empty())
    val propertyDetailsState: StateFlow<UiState<PropertyDetailsModel>> = _propertyDetailsState


    private val _sendCommentOnPropertyState = MutableStateFlow<UiState<SendPropertyCommentResponse>>(UiState.Empty())
    val sendCommentOnPropertyState: StateFlow<UiState<SendPropertyCommentResponse>> = _sendCommentOnPropertyState

    private var getPropertyDetailsJob: Job? = null
    private var sendCommentOnPropertyJob: Job? = null

    fun cancelRequest() {
        getPropertyDetailsJob?.cancel()
    }

    fun getPropertyDetails(propertyId:String) {
        getPropertyDetailsJob?.cancel()
        getPropertyDetailsJob = viewModelScope.launch {
            withContext(coroutineContext) {
                repository.getPropertyDetails(propertyId).collect { result ->
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
                repository.sendCommentOnProperty(
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
}