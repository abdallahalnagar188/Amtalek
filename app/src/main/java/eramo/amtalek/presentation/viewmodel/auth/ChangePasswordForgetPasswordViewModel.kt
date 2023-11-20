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
class ChangePasswordForgetPasswordViewModel @Inject constructor(
    private val forgotPassUseCase: ForgotPassUseCase
) : ViewModel() {

    private val _changePasswordState = MutableStateFlow<UiState<ResultModel>>(UiState.Empty())
    val changePasswordState: StateFlow<UiState<ResultModel>> = _changePasswordState

    private var changePasswordJob: Job? = null

    fun cancelRequest() {
        changePasswordJob?.cancel()
    }

    fun changePassword(
        email: String,
        code: String,
        newPassword: String,
        rePassword: String
    ) {
        changePasswordJob?.cancel()
        changePasswordJob = viewModelScope.launch {
            withContext(coroutineContext) {
                forgotPassUseCase.changePasswordForgetPassword(email, code, newPassword, rePassword).collect {
                    when (it) {
                        is Resource.Success -> {
                            _changePasswordState.value = UiState.Success(it.data)
                        }

                        is Resource.Error -> {
                            _changePasswordState.value = UiState.Error(it.message!!)
                        }

                        is Resource.Loading -> {
                            _changePasswordState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }

}