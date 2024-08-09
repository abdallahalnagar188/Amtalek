package eramo.amtalek.presentation.ui.drawer.messaging.addons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.data.remote.dto.adons.AddonsResponse
import eramo.amtalek.domain.repository.AddonsRepo
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddonsViewModel @Inject constructor(
    private val getAddonsUseCase: AddonsRepo
) : ViewModel() {

    private val _addons = MutableStateFlow<Resource<AddonsResponse>>(Resource.Loading())
    val addons: StateFlow<Resource<AddonsResponse>> = _addons

    private var getAddonsJob: Job? = null

    fun getAddons() {
        getAddonsJob?.cancel()
        getAddonsJob = viewModelScope.launch {
            getAddonsUseCase.getAddons().collect { resource ->
                _addons.value = resource
            }
        }
    }
}
