package eramo.amtalek.presentation.viewmodel.drawer.myaccount.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.domain.model.products.orders.OrderModel
import eramo.amtalek.domain.repository.OrderRepository
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
class OrderDetailsViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _ordersState = MutableStateFlow<UiState<OrderModel>>(UiState.Empty())
    val ordersState: StateFlow<UiState<OrderModel>> = _ordersState

    private var ordersJob: Job? = null

    fun cancelRequest() = ordersJob?.cancel()

    fun getOrderById(orderId: String?) {
        if (orderId == null) return
        ordersJob?.cancel()
        ordersJob = viewModelScope.launch {
            delay(Constants.ANIMATION_DELAY)
            withContext(coroutineContext) {
                orderRepository.getOrderById(orderId).collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let {
                                _ordersState.value = UiState.Success(it[0])
                            } ?: run { _ordersState.value = UiState.Empty() }
                        }
                        is Resource.Error -> {
                            _ordersState.value = UiState.Error(result.message!!)
                        }
                        is Resource.Loading -> {
                            _ordersState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }
}