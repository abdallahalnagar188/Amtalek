package eramo.amtalek.presentation.viewmodel.navbottom.extension

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.domain.model.property.PropertyDetailsModel
import eramo.amtalek.domain.repository.PropertyRepository
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PropertyDetailsSellAndRentViewModel @Inject constructor(
    private val repository: PropertyRepository
) : ViewModel() {

    private val _propertyDetailsState = MutableStateFlow<UiState<PropertyDetailsModel>>(UiState.Empty())
    val propertyDetailsState: StateFlow<UiState<PropertyDetailsModel>> = _propertyDetailsState

    private var getPropertyDetailsJob: Job? = null

    fun cancelRequest() {
        getPropertyDetailsJob?.cancel()
    }

    fun getPropertyDetails(propertyId: String) {
        getPropertyDetailsJob?.cancel()
        getPropertyDetailsJob = viewModelScope.launch {
            withContext(coroutineContext) {
                repository.getPropertyDetails(propertyId).collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _propertyDetailsState.value = UiState.Success(result.data)
                        }

                        is Resource.Error -> {
                            _propertyDetailsState.value =
                                UiState.Error(result.message!!)
                        }

                        is Resource.Loading -> {
                            _propertyDetailsState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }
}