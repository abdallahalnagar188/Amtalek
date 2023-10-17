package eramo.amtalek.presentation.viewmodel.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.domain.model.products.orders.AllProductExtrasModel
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
class CheckoutStepTwoViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _extraState = MutableStateFlow<UiState<List<AllProductExtrasModel>>>(UiState.Empty())
    val extraState: StateFlow<UiState<List<AllProductExtrasModel>>> = _extraState

    private var extraJob: Job? = null

    fun cancelRequest() = extraJob?.cancel()

    fun extra() {
        extraJob?.cancel()
        extraJob = viewModelScope.launch {
            delay(ANIMATION_DELAY)
            withContext(coroutineContext) {
                orderRepository.productExtras("").collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let {
                                _extraState.value = UiState.Success(it)
                            } ?: run { _extraState.value = UiState.Empty() }
                        }
                        is Resource.Error -> {
                            _extraState.value = UiState.Error(result.message!!)
                        }
                        is Resource.Loading -> {
                            _extraState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }
}