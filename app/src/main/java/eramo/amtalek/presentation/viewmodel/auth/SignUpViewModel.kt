package eramo.amtalek.presentation.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.domain.model.ResultModel
import eramo.amtalek.domain.model.auth.CitiesModel
import eramo.amtalek.domain.model.auth.CountriesModel
import eramo.amtalek.domain.model.auth.CountryModel
import eramo.amtalek.domain.model.auth.RegionsModel
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

    private val _registerState = MutableStateFlow<UiState<ResultModel>>(UiState.Empty())
    val registerState: StateFlow<UiState<ResultModel>> = _registerState

    private val _countriesState = MutableStateFlow<UiState<List<CountryModel>>>(UiState.Empty())
    val countriesState: StateFlow<UiState<List<CountryModel>>> = _countriesState

//    private val _countriesState = MutableStateFlow<UiState<List<CountriesModel>>>(UiState.Empty())
//    val countriesState: StateFlow<UiState<List<CountriesModel>>> = _countriesState

    private val _cityState = MutableStateFlow<UiState<List<CitiesModel>>>(UiState.Empty())
    val cityState: StateFlow<UiState<List<CitiesModel>>> = _cityState

    private val _regionState = MutableStateFlow<UiState<List<RegionsModel>>>(UiState.Empty())
    val regionState: StateFlow<UiState<List<RegionsModel>>> = _regionState

    private var getCountriesJob: Job? = null
    private var registerJob: Job? = null
    private var cityJob: Job? = null
    private var regionJob: Job? = null

    fun cancelRequest() {
        getCountriesJob?.cancel()
        registerJob?.cancel()
        cityJob?.cancel()
        regionJob?.cancel()
    }

    fun registerApp(
        fullName: String,
        email: String,
        phone: String,
        password: String,
        address: String,
        countryId: String,
        cityId: String,
        regionId: String,
        gender: String
    ) {
        registerJob?.cancel()
        registerJob = viewModelScope.launch {
            withContext(coroutineContext) {
                registerUseCase(
                    fullName,
                    email,
                    phone,
                    password,
                    address,
                    countryId,
                    cityId,
                    regionId,
                    gender
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

    fun cities(countryId: Int) {
        cityJob?.cancel()
        cityJob = viewModelScope.launch {
            withContext(coroutineContext) {
                authRepository.allCities(countryId.toString()).collect {
                    when (it) {
                        is Resource.Success -> {
                            _cityState.value = UiState.Success(it.data)
                        }
                        is Resource.Error -> {
                            _cityState.value = UiState.Error(it.message!!)
                        }
                        is Resource.Loading -> {
                            _cityState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }

    fun regions(cityId: Int) {
        regionJob?.cancel()
        regionJob = viewModelScope.launch {
            withContext(coroutineContext) {
                authRepository.allRegions(cityId.toString()).collect {
                    when (it) {
                        is Resource.Success -> {
                            _regionState.value = UiState.Success(it.data)
                        }
                        is Resource.Error -> {
                            _regionState.value = UiState.Error(it.message!!)
                        }
                        is Resource.Loading -> {
                            _regionState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }
}