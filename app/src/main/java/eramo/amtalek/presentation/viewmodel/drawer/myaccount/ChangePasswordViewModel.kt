package eramo.amtalek.presentation.viewmodel.drawer.myaccount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.domain.model.ResultModel
import eramo.amtalek.domain.usecase.auth.UpdatePassUseCase
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val updatePassUseCase: UpdatePassUseCase
) : ViewModel() {

    private val _updatePasswordState = MutableStateFlow<UiState<ResultModel>>(UiState.Empty())
    val updatePasswordState: StateFlow<UiState<ResultModel>> = _updatePasswordState

    private var updatePasswordJob: Job? = null

    fun cancelRequest() = updatePasswordJob?.cancel()

    fun updatePassword(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String
    ) {
        updatePasswordJob?.cancel()
        updatePasswordJob = viewModelScope.launch {
            withContext(coroutineContext) {
                updatePassUseCase(currentPassword, newPassword, confirmPassword).collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _updatePasswordState.value = UiState.Success(result.data)
                        }

                        is Resource.Error -> {
                            _updatePasswordState.value = UiState.Error(result.message!!)
                        }

                        is Resource.Loading -> {
                            _updatePasswordState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }
}