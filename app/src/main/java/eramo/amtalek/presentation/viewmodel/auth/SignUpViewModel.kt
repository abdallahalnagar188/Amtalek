package eramo.amtalek.presentation.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.domain.model.ResultModel
import eramo.amtalek.domain.model.auth.CityModel
import eramo.amtalek.domain.model.auth.CountryModel
import eramo.amtalek.domain.repository.AuthRepository
import eramo.amtalek.domain.usecase.auth.RegisterUseCase
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {


    private val _countriesState = MutableStateFlow<UiState<List<CountryModel>>>(UiState.Empty())
    val countriesState: StateFlow<UiState<List<CountryModel>>> = _countriesState

    private val _citiesState = MutableStateFlow<UiState<List<CityModel>>>(UiState.Empty())
    val citiesState: StateFlow<UiState<List<CityModel>>> = _citiesState

    private val _registerState = MutableStateFlow<UiState<ResultModel>>(UiState.Empty())
    val registerState: StateFlow<UiState<ResultModel>> = _registerState

    private val _sendVerificationCodeEmailState = MutableStateFlow<UiState<ResultModel>>(UiState.Empty())
    val sendVerificationCodeEmailState: StateFlow<UiState<ResultModel>> = _sendVerificationCodeEmailState

     var registeredEmail: String? = null

    private var getCountriesJob: Job? = null
    private var getCitiesJob: Job? = null
    private var registerJob: Job? = null
    private var sendVerificationCodeEmailJob: Job? = null

    fun cancelRequest() {
        getCountriesJob?.cancel()
        getCitiesJob?.cancel()
        registerJob?.cancel()
        sendVerificationCodeEmailJob?.cancel()
    }

    fun register(
        firstName: String,
        lastName: String,
        phone: String,
        email: String,
        password: String,
        confirmPassword: String,
        gender: String,
        countryId: String,
        cityId: String
    ) {
        registerJob?.cancel()
        registerJob = viewModelScope.launch {
            withContext(coroutineContext) {

                registeredEmail = email

                registerUseCase(
                    firstName, lastName, phone, email, password, confirmPassword, gender, countryId, cityId,
                ).collect {
                    when (it) {
                        is Resource.Success -> {
                            _registerState.value = UiState.Success(it.data)
                        }

                        is Resource.Error -> {
                            _registerState.value = UiState.Error(it.message!!)
                        }

                        is Resource.Loading -> {
                            _registerState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }

    fun sendVerificationCodeEmail() {
        sendVerificationCodeEmailJob?.cancel()
        sendVerificationCodeEmailJob = viewModelScope.launch {
            withContext(coroutineContext) {
                registeredEmail?.let { email ->
                    authRepository.sendVerificationCodeEmail(email).collect {
                        when (it) {
                            is Resource.Success -> {
                                _sendVerificationCodeEmailState.value = UiState.Success(it.data)
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
    }

    fun getCountries() {
        getCountriesJob?.cancel()
        getCountriesJob = viewModelScope.launch {
            withContext(coroutineContext) {
                authRepository.getCountries().collect {
                    when (it) {
                        is Resource.Success -> {
                            _countriesState.value = UiState.Success(it.data)
                        }

                        is Resource.Error -> {
                            _countriesState.value = UiState.Error(it.message!!)
                        }

                        is Resource.Loading -> {
                            _countriesState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }

    fun getCities(countryId: String) {
        getCitiesJob?.cancel()
        getCitiesJob = viewModelScope.launch {
            withContext(coroutineContext) {
                authRepository.getCities(countryId).collect {
                    when (it) {
                        is Resource.Success -> {
                            _citiesState.value = UiState.Success(it.data)
                        }

                        is Resource.Error -> {
                            _citiesState.value = UiState.Error(it.message!!)
                        }

                        is Resource.Loading -> {
                            _citiesState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }

}