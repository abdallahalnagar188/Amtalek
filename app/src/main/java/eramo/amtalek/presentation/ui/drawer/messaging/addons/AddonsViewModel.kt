package eramo.amtalek.presentation.ui.drawer.messaging.addons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.data.remote.dto.adons.AddonsResponse
import eramo.amtalek.data.remote.dto.adons.BuyAddonsRequest
import eramo.amtalek.data.remote.dto.adons.BuyAddonsResponse
import eramo.amtalek.domain.repository.AddonsRepo
import eramo.amtalek.domain.repository.BuyAddonsRepo
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddonsViewModel @Inject constructor(
    private val getAddonsUseCase: AddonsRepo,
    private val buyAddonsUseCase: BuyAddonsRepo
) : ViewModel() {

    private val _addons = MutableStateFlow<Resource<AddonsResponse>>(Resource.Loading())
    val addons: StateFlow<Resource<AddonsResponse>> = _addons

    private val _buyAddons = MutableStateFlow<Resource<BuyAddonsResponse>>(Resource.Loading())
    val buyAddons: StateFlow<Resource<BuyAddonsResponse>> = _buyAddons

    private var getAddonsJob: Job? = null
    private var buyAddonsJob: Job? = null

    fun getAddons() {
        getAddonsJob?.cancel()
        getAddonsJob = viewModelScope.launch {
            getAddonsUseCase.getAddons().collect { resource ->
                _addons.value = resource
            }
        }
    }

    fun buyAddons(list: ItemCard) {
        buyAddonsJob?.cancel()
        buyAddonsJob = viewModelScope.launch {
            buyAddonsUseCase.buyAddons(list = list).collect { resource ->
                _buyAddons.value = resource
            }
        }
    }
}
