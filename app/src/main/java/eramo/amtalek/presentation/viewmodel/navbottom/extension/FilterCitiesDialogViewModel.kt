package eramo.amtalek.presentation.viewmodel.navbottom.extension

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.domain.model.auth.CityModel
import eramo.amtalek.domain.model.auth.CountryModel
import eramo.amtalek.domain.usecase.auth.CountriesAndCitiesUseCase
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class FilterCitiesDialogViewModel @Inject constructor(
    private val countriesAndCitiesUseCase: CountriesAndCitiesUseCase
) : ViewModel() {

    private val _citiesState = MutableStateFlow<UiState<List<CityModel>>>(UiState.Empty())
    val citiesState: StateFlow<UiState<List<CityModel>>> = _citiesState

    private val _countriesState = MutableStateFlow<UiState<List<CountryModel>>>(UiState.Empty())
    val countriesState: StateFlow<UiState<List<CountryModel>>> = _countriesState

    private var getCitiesJob: Job? = null
    private var getCountriesJob: Job? = null

    fun cancelRequest() {
        getCitiesJob?.cancel()
        getCountriesJob?.cancel()
    }
    fun getCountries(){
        getCountriesJob?.cancel()
        getCountriesJob = viewModelScope.launch {
            withContext(coroutineContext){
                countriesAndCitiesUseCase.getCountries().collect {
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
                countriesAndCitiesUseCase.getCities(countryId).collect {
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