package eramo.amtalek.presentation.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.data.remote.dto.splash.SplashResponse
import eramo.amtalek.data.remote.dto.splash.splashV2.OnBordingResponse
import eramo.amtalek.domain.model.auth.OnBoardingModel
import eramo.amtalek.domain.repository.AuthRepository
import eramo.amtalek.domain.repository.SplashRepo
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val repository: SplashRepo
) : ViewModel() {

    private val _onBoardingStateState = MutableStateFlow<Resource<OnBordingResponse>>(Resource.Loading())
    val onBoardingStateStateDto: StateFlow<Resource<OnBordingResponse>> = _onBoardingStateState

    private var onBoardingStateJob: Job? = null

    fun cancelRequest() = onBoardingStateJob?.cancel()

    fun getOnBoarding() {
        onBoardingStateJob?.cancel()
        onBoardingStateJob = viewModelScope.launch {
            withContext(coroutineContext) {
                repository.getOnBoardingSlider().collect { result ->

                    when
                            (result) {
                        is Resource.Success -> {
                            _onBoardingStateState.value = Resource.Success(result.data)
                        }

                        is Resource.Error -> {}
                        is Resource.Loading -> {}
                    }
                }
            }
        }
    }
}