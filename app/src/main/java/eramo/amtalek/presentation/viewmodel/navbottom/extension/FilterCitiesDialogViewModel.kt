package eramo.amtalek.presentation.viewmodel.navbottom.extension

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.domain.model.auth.CityModel
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

    private var getCitiesJob: Job? = null

    fun cancelRequest() {
        getCitiesJob?.cancel()
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