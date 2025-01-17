package eramo.amtalek.presentation.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.domain.model.ResultModel
import eramo.amtalek.domain.model.auth.UserModel
import eramo.amtalek.domain.usecase.auth.LoginUseCase
import eramo.amtalek.domain.usecase.auth.RegisterUseCase
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
) : ViewModel() {

    private val _loginState = MutableStateFlow<UiState<UserModel>>(UiState.Empty())
    val loginState: StateFlow<UiState<UserModel>> = _loginState

    private val _sendVerificationCodeEmailState = MutableStateFlow<UiState<ResultModel>>(UiState.Empty())
    val sendVerificationCodeEmailState = _sendVerificationCodeEmailState

    private var loginJob: Job? = null
    private var sendVerificationCodeEmailJob: Job? = null

    fun cancelRequest() {
        loginJob?.cancel()
        sendVerificationCodeEmailJob?.cancel()

    }


    fun login(
        email: String,
        password: String,
        firebaseToken: String, isRemember: Boolean
    ) {
        loginJob?.cancel()
        loginJob = viewModelScope.launch {
            withContext(coroutineContext) {
                loginUseCase(email, password, firebaseToken).collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { userModel ->
                                saveUserInfo(userModel.toUserModel(), isRemember)
                                _loginState.value = UiState.Success(userModel.toUserModel())

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

    fun sendVerificationCodeEmail(email: String) {
        sendVerificationCodeEmailJob?.cancel()
        sendVerificationCodeEmailJob = viewModelScope.launch {
            withContext(coroutineContext) {

                registerUseCase.sendVerificationCodeEmail(email).collect {
                    when (it) {
                        is Resource.Success -> {
                            _sendVerificationCodeEmailState.value = UiState.Success(it.data)

//                            delay(1000)
                            _sendVerificationCodeEmailState.value = UiState.Empty()
                        }

                        is Resource.Error -> {
                            _sendVerificationCodeEmailState.value = UiState.Error(it.message!!)
                        }

                        is Resource.Loading -> {
                            _sendVerificationCodeEmailState.value = UiState.Loading()
                        }
                    }
                }

            }
        }
    }



    private fun saveUserInfo(userModel: UserModel, isRemember: Boolean) {
        UserUtil.saveUserInfo(
            isRemember = isRemember,
             userToken =  "Bearer ${userModel.token}",
            userID = userModel.id.toString(),
            firstName = userModel.firstName,
            lastName = userModel.lastName,
           phone =  userModel.phone,
            email = userModel.email,
            countryId = userModel.country.toString(),
            countryName = userModel.countryName,
            cityId = userModel.city.toString(),
            cityName = userModel.cityName,
            userBio = userModel.bio,
           profileImageUrl =  userModel.userImage,
            userType = userModel.actorType,
            hasPackage = userModel.hasPackage,
            brokerName = userModel.name,
            dashboardLink = userModel.dashboardLink?:"",
            packageId = userModel.packageInfo?.packageId.toString()
        )
    }
}