package eramo.amtalek.presentation.viewmodel.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.domain.model.ResultModel
import eramo.amtalek.domain.model.products.PaymentTypesModel
import eramo.amtalek.domain.model.request.OrderRequest
import eramo.amtalek.domain.repository.CartRepository
import eramo.amtalek.domain.repository.OrderRepository
import eramo.amtalek.util.ANIMATION_DELAY
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
class CheckoutStepThreeViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _paymentState = MutableStateFlow<UiState<List<PaymentTypesModel>>>(UiState.Empty())
    val paymentState: StateFlow<UiState<List<PaymentTypesModel>>> = _paymentState

    private val _orderState = MutableStateFlow<UiState<ResultModel>>(UiState.Empty())
    val orderState: StateFlow<UiState<ResultModel>> = _orderState

    private var paymentJob: Job? = null
    private var orderJob: Job? = null

    fun cancelRequest() {
        paymentJob?.cancel()
        orderJob?.cancel()
    }

    fun paymentTypes() {
        orderJob?.cancel()
        orderJob = viewModelScope.launch {
            delay(ANIMATION_DELAY)
            withContext(coroutineContext) {
                orderRepository.paymentTypes().collect { result ->
                    when (result) {
                        is Resource.Success -> _paymentState.value = UiState.Success(result.data)
                        is Resource.Error -> {
                            _paymentState.value = UiState.Error(result.message!!)
                        }
                        is Resource.Loading -> {
                            _paymentState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }

    fun saveOrderRequest(orderRequest: OrderRequest) {
        orderJob?.cancel()
        orderJob = viewModelScope.launch {
            withContext(coroutineContext) {
                orderRepository.saveOrderRequest(orderRequest).collect { result ->
                    when (result) {
                        is Resource.Success -> removeAllCart()
                        is Resource.Error -> {
                            _orderState.value = UiState.Error(result.message!!)
                        }
                        is Resource.Loading -> {
                            _orderState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }

    private fun removeAllCart() {
        orderJob?.cancel()
        orderJob = viewModelScope.launch {
            withContext(coroutineContext) {
                cartRepository.removeAllCartApi().collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let {
                                _orderState.value = UiState.Success(it)
                            } ?: run { _orderState.value = UiState.Empty() }
                        }
                        is Resource.Error -> {
                            _orderState.value = UiState.Error(result.message!!)
                        }
                        is Resource.Loading -> {
                            _orderState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }
}