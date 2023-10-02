package eramo.amtalek.presentation.viewmodel.drawer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.domain.model.ResultModel
import eramo.amtalek.domain.repository.RequestRepository
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class QuestionDetailsViewModel @Inject constructor(
    private val requestRepository: RequestRepository
) : ViewModel() {

    private val _requestState = MutableStateFlow<UiState<ResultModel>>(UiState.Empty())
    val requestState: StateFlow<UiState<ResultModel>> = _requestState

    private var requestJob: Job? = null

    fun cancelRequest() {
        requestJob?.cancel()
    }

    fun postRequest(
        user_name: String,
        user_email: String,
        user_phone: String,
        message: String
    ) {
        requestJob?.cancel()
        requestJob = viewModelScope.launch {
            withContext(coroutineContext) {
                requestRepository.questionsRequest(
                    user_name,
                    user_email,
                    user_phone,
                    message
                ).collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let {
                                _requestState.value = UiState.Success(it)
                            } ?: run { _requestState.value = UiState.Empty() }
                        }
                        is Resource.Error -> {
                            _requestState.value =
                                UiState.Error(result.message!!)
                        }
                        is Resource.Loading -> {
                            _requestState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }
}