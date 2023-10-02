package eramo.amtalek.presentation.viewmodel.drawer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.data.remote.dto.drawer.AppInfoResponse
import eramo.amtalek.domain.usecase.drawer.GetAppInfoUseCase
import eramo.amtalek.util.Constants
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AboutUsViewModel @Inject constructor(
    private val getAppInfoUseCase: GetAppInfoUseCase
) : ViewModel() {

    private val _getAppInfoState = MutableStateFlow<UiState<AppInfoResponse>>(UiState.Empty())
    val getAppInfoState: StateFlow<UiState<AppInfoResponse>> = _getAppInfoState

    private var getAppInfoJob: Job? = null

    init {
        getAppInfo()
    }

    fun cancelRequest() = getAppInfoJob?.cancel()

    private fun getAppInfo() {
        getAppInfoJob?.cancel()
        getAppInfoJob = viewModelScope.launch {
            delay(Constants.ANIMATION_DELAY)
            withContext(coroutineContext) {
                getAppInfoUseCase().collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let {
                                _getAppInfoState.value = UiState.Success(it)
                            } ?: run { _getAppInfoState.value = UiState.Empty() }
                        }
                        is Resource.Error -> {
                            _getAppInfoState.value = UiState.Error(result.message!!)
                        }
                        is Resource.Loading -> {
                            _getAppInfoState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }
}