package eramo.amtalek.presentation.ui.drawer.messaging.addons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.data.remote.dto.adons.AddonsResponse
import eramo.amtalek.data.remote.dto.adons.BuyAddonsResponse
import eramo.amtalek.domain.repository.AddonsRepo
import eramo.amtalek.domain.repository.BuyAddonsRepo
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
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
                when (resource){
                    is Resource.Success ->{
                        _addons.value = resource
                    }
                    is Resource.Loading ->{
                        LoadingDialog.showDialog()
                    } is Resource.Error->{
                    LoadingDialog.dismissDialog()
                }
                }

            }
        }
    }

    fun buyAddons(list: CardReqoest) {
        buyAddonsJob?.cancel()
        buyAddonsJob = viewModelScope.launch {
            buyAddonsUseCase.buyAddons(list = list).collect { resource ->
                _buyAddons.value = resource
            }
        }
    }
}
