package eramo.amtalek.presentation.viewmodel.dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.domain.model.ResultModel
import eramo.amtalek.domain.repository.AuthRepository
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class SuspendDialogViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _suspendAccountState = MutableStateFlow<UiState<ResultModel>>(UiState.Empty())
    val suspendAccountState: StateFlow<UiState<ResultModel>> = _suspendAccountState

    private var suspendAccountJob: Job? = null

    fun cancelRequest() = suspendAccountJob?.cancel()

    fun suspendAccount() {
        suspendAccountJob?.cancel()
        suspendAccountJob = viewModelScope.launch {
            withContext(coroutineContext) {
                authRepository.suspendAccount().collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _suspendAccountState.value = UiState.Success(result.data)
                            UserUtil.clearUserInfo()
                        }

                        is Resource.Error -> {
                            _suspendAccountState.value = UiState.Error(result.message!!)
                            UserUtil.clearUserInfo()
                        }

                        is Resource.Loading -> {
                            _suspendAccountState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }
}