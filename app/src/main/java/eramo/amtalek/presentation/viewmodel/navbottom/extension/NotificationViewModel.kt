package eramo.amtalek.presentation.viewmodel.navbottom.extension

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.data.remote.dto.NotificationDto
import eramo.amtalek.domain.repository.ProductsRepository
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
class NotificationViewModel @Inject constructor(
    private val productsRepository: ProductsRepository
) : ViewModel() {

    private val _notificationsState = MutableStateFlow<UiState<List<NotificationDto>>>(UiState.Empty())
    val notificationsState: StateFlow<UiState<List<NotificationDto>>> = _notificationsState

    private var notificationsJob: Job? = null

    fun cancelRequest() {
        notificationsJob?.cancel()
    }

    fun getNotification() {
        notificationsJob?.cancel()
        notificationsJob = viewModelScope.launch {
            delay(Constants.ANIMATION_DELAY)
            withContext(coroutineContext) {
                productsRepository.getUserNotifications().collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { data ->
                                _notificationsState.value = UiState.Success(data)
                            } ?: run { _notificationsState.value = UiState.Empty() }
                        }
                        is Resource.Error -> {
                            _notificationsState.value =
                                UiState.Error(result.message!!)
                        }
                        is Resource.Loading -> {
                            _notificationsState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }
}