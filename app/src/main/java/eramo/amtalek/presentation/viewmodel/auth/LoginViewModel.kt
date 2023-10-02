package eramo.amtalek.presentation.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.domain.model.auth.LoginModel
import eramo.amtalek.domain.repository.CartRepository
import eramo.amtalek.domain.usecase.auth.LoginUseCase
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiState
import eramo.amtalek.util.UserUtil
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val repository: CartRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<UiState<LoginModel>>(UiState.Empty())
    val loginState: StateFlow<UiState<LoginModel>> = _loginState

    private var loginJob: Job? = null

    fun cancelRequest() = loginJob?.cancel()

    fun loginApp(user_phone: String, user_pass: String, isRemember: Boolean) {
        loginJob?.cancel()
        loginJob = viewModelScope.launch {
            withContext(coroutineContext) {
                loginUseCase(user_phone, user_pass).collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let {
                                saveUserInfo(it, user_pass, isRemember)
                                _loginState.value = UiState.Success(it)
                            } ?: run { _loginState.value = UiState.Empty() }
                        }
                        is Resource.Error -> {
                            _loginState.value = UiState.Error(result.message!!)
                        }
                        is Resource.Loading -> {
                            _loginState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }

    suspend fun switchLocalCartToRemote() = withContext(viewModelScope.coroutineContext) {
        repository.switchLocalCartToRemote()
    }

    private fun saveUserInfo(body: LoginModel, password: String, isRemember: Boolean) {
        UserUtil.saveUserInfo(
            isRemember,
            body.member?.userId!!,
            body.token!!,
            body.member?.userName!!,
            password,
            body.member?.userAddress ?: "",
            body.member?.country_id ?: "",
            body.member?.city_id ?: "",
            body.member?.region_id ?: "",
            body.member?.userPhone!!,
            body.member?.userEmail!!,
            body.member?.mImage!!,
        )
    }
}