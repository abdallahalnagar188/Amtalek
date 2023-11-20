package eramo.amtalek.presentation.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.domain.model.ResultModel
import eramo.amtalek.domain.usecase.auth.ForgotPassUseCase
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ForgotPassViewModel @Inject constructor(
    private val forgotPassUseCase: ForgotPassUseCase
) : ViewModel() {

    private val _sendForgotPasswordMailState = MutableStateFlow<UiState<ResultModel>>(UiState.Empty())
    val sendForgotPasswordMailState: StateFlow<UiState<ResultModel>> = _sendForgotPasswordMailState

    private var sendForgotPassMailJob: Job? = null

    var registeredEmail: String? = null

    fun cancelRequest() {
        sendForgotPassMailJob?.cancel()
    }

    fun sendForgotPasswordMail(email: String) {
        sendForgotPassMailJob?.cancel()
        sendForgotPassMailJob = viewModelScope.launch {
            withContext(coroutineContext) {

                registeredEmail = email

                forgotPassUseCase.sendForgetPasswordCodeEmail(email).collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _sendForgotPasswordMailState.value = UiState.Success(result.data)
                        }

                        is Resource.Error -> {
                            _sendForgotPasswordMailState.value =
                                UiState.Error(result.message!!)
                        }

                        is Resource.Loading -> {
                            _sendForgotPasswordMailState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }
}