package eramo.amtalek.presentation.viewmodel.dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.domain.model.ResultModel
import eramo.amtalek.domain.repository.OrderRepository
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CancelDialogViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _orderDetailsState = MutableStateFlow<UiState<ResultModel>>(UiState.Empty())
    val orderDetailsState: StateFlow<UiState<ResultModel>> = _orderDetailsState

    private var orderDetailsJob: Job? = null

    fun cancelRequest() = orderDetailsJob?.cancel()

    fun cancelOrder(order_id: String) {
        orderDetailsJob?.cancel()
        orderDetailsJob = viewModelScope.launch {
            withContext(coroutineContext) {
                orderRepository.cancelProductOrder(order_id).collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let {
                                _orderDetailsState.value = UiState.Success(it)
                            } ?: run { _orderDetailsState.value = UiState.Empty() }
                        }
                        is Resource.Error -> {
                            _orderDetailsState.value = UiState.Error(result.message!!)
                        }
                        is Resource.Loading -> {
                            _orderDetailsState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }
}