package eramo.amtalek.presentation.ui.search.searchform

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.domain.repository.search.AllLocationsRepository
import eramo.amtalek.domain.search.LocationModel
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchFormViewModel @Inject constructor(
    private val allLocationsRepository: AllLocationsRepository
) : ViewModel() {

    private var _allLocationsState = MutableStateFlow<UiState<List<LocationModel>>>(UiState.Empty())
    val allLocationState: StateFlow<UiState<List<LocationModel>>> = _allLocationsState


    private var allLocationJob: Job? = null

    fun getAllLocations() {
        allLocationJob?.cancel()
        allLocationJob = viewModelScope.launch {
            allLocationsRepository.getAllLocations().collect {
                when (it) {
                    is Resource.Loading -> {
                        _allLocationsState.value = UiState.Loading()
                    }

                    is Resource.Success -> {
                        _allLocationsState.value = UiState.Success(it.data?.mapLocations())
                    }
                    is Resource.Error -> {
                        _allLocationsState.value = UiState.Error(it.message!!)
                    }
                }
            }
        }

    }
}