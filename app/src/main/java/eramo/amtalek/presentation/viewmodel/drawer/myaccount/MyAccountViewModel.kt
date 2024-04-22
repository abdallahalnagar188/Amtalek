package eramo.amtalek.presentation.viewmodel.drawer.myaccount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.data.remote.dto.general.Member
import eramo.amtalek.domain.usecase.drawer.GetProfileUseCase
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MyAccountViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase
) : ViewModel() {

    private val _getProfileState = MutableStateFlow<UiState<Member>>(UiState.Empty())
    val getProfileState: StateFlow<UiState<Member>> = _getProfileState

    private var getProfileJob: Job? = null

    fun cancelRequest() = getProfileJob?.cancel()

    fun getProfile(type:String,id:String) {
        getProfileJob?.cancel()
        getProfileJob = viewModelScope.launch {
            withContext(coroutineContext) {
                getProfileUseCase(type, id).collect { result ->
                    when (result) {
                        is Resource.Success -> {
//                            result.data?.let {
//                                _getProfileState.value = UiState.Success(it)
//                            } ?: run { _getProfileState.value = UiState.Empty() }
                        }
                        is Resource.Error -> {
                            _getProfileState.value = UiState.Error(result.message!!)
                        }
                        is Resource.Loading -> {
                            _getProfileState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }
}