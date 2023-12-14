package eramo.amtalek.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.domain.model.ResultModel
import eramo.amtalek.domain.model.auth.UserModel
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
class SharedViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    val openDrawer = MutableLiveData<Boolean>()
    val profileData = MutableStateFlow<UiState<UserModel>>(UiState.Empty())
    val dateString = MutableLiveData<String?>(null)

    //____________________________________________________________________________________________//

    private val _logoutState = MutableStateFlow<UiState<ResultModel>>(UiState.Empty())
    val logoutState: StateFlow<UiState<ResultModel>> = _logoutState

    private var logoutJob: Job? = null

    fun cancelRequest() {
        logoutJob?.cancel()
    }

    fun logout() {
        logoutJob?.cancel()
        logoutJob = viewModelScope.launch {
            withContext(coroutineContext) {
                authRepository.logout().collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            UserUtil.clearUserInfo()
                            _logoutState.value = UiState.Success(result.data)
                            profileData.value = UiState.Empty()
                        }

                        is Resource.Error -> {
                            UserUtil.clearUserInfo()
                            _logoutState.value = UiState.Error(result.message!!)
                        }

                        is Resource.Loading -> {
                            _logoutState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }

}