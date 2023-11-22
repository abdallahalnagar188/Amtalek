package eramo.amtalek.presentation.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.domain.model.ResultModel
import eramo.amtalek.domain.model.auth.ContactUsInfoModel
import eramo.amtalek.domain.usecase.auth.ContactUsUseCase
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class ContactUsAuthViewModel @Inject constructor(
    private val contactUsUseCase: ContactUsUseCase
) : ViewModel() {

    private val _getContactUsInfoState = MutableStateFlow<UiState<ContactUsInfoModel>>(UiState.Empty())
    val getContactUsInfoState: StateFlow<UiState<ContactUsInfoModel>> = _getContactUsInfoState

    private val _sendContactUsMessageState = MutableStateFlow<UiState<ResultModel>>(UiState.Empty())
    val sendContactUsMessageState: StateFlow<UiState<ResultModel>> = _sendContactUsMessageState

    private var getContactUsInfoJob: Job? = null
    private var sendContactUsMessageJob: Job? = null

    fun cancelRequest() {
        getContactUsInfoJob?.cancel()
        sendContactUsMessageJob?.cancel()
    }

    private fun getContactUsInfo() {
        getContactUsInfoJob?.cancel()
        getContactUsInfoJob = viewModelScope.launch {
            withContext(coroutineContext) {
                contactUsUseCase.getContactUsInfo().collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _getContactUsInfoState.value = UiState.Success(result.data)
                        }

                        is Resource.Error -> {
                            _getContactUsInfoState.value =
                                UiState.Error(result.message!!)
                        }

                        is Resource.Loading -> {
                            _getContactUsInfoState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }

     fun sendContactUsMessage(
        name: String,
        mobileNumber: String,
        email: String,
        message: String
    ) {
        sendContactUsMessageJob?.cancel()
        sendContactUsMessageJob = viewModelScope.launch {
            withContext(coroutineContext) {
                contactUsUseCase.sendContactUsMessage(name, mobileNumber, email, message).collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _sendContactUsMessageState.value = UiState.Success(result.data)
                        }

                        is Resource.Error -> {
                            _sendContactUsMessageState.value =
                                UiState.Error(result.message!!)
                        }

                        is Resource.Loading -> {
                            _sendContactUsMessageState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }

    init {
        getContactUsInfo()
    }
}