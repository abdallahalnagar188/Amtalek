package eramo.amtalek.presentation.viewmodel.navbottom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.data.remote.dto.products.orders.CartCountResponse
import eramo.amtalek.domain.model.ResultModel
import eramo.amtalek.domain.model.products.orders.CartDataModel
import eramo.amtalek.domain.repository.CartRepository
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: CartRepository
) : ViewModel() {

    private val _cartDataDtoState = MutableStateFlow<UiState<Map<String, Any>>>(UiState.Empty())
    val cartDataModelState: StateFlow<UiState<Map<String, Any>>> = _cartDataDtoState

    private val _updateCartState = MutableSharedFlow<UiState<ResultModel>>()
    val updateCartState: SharedFlow<UiState<ResultModel>> = _updateCartState

    private val _removeCartItemState = MutableSharedFlow<UiState<ResultModel>>()
    val removeCartItemState: SharedFlow<UiState<ResultModel>> = _removeCartItemState

    private val _cartCountState = MutableStateFlow<UiState<CartCountResponse>>(UiState.Empty())
    val cartCountState: StateFlow<UiState<CartCountResponse>> = _cartCountState

    private var cartDataJob: Job? = null
    private var updateCartJob: Job? = null
    private var removeCartItemJob: Job? = null
    private var cartCountJob: Job? = null

    fun cancelRequest() {
        cartDataJob?.cancel()
        updateCartJob?.cancel()
        removeCartItemJob?.cancel()
        cartCountJob?.cancel()
    }

    fun cartData() {
        cartDataJob?.cancel()
        cartDataJob = viewModelScope.launch {
            withContext(coroutineContext) {
                val result = if (UserUtil.isUserLogin())
                    repository.getCartDataApi()
                else
                    repository.getCartDataDB()

                result.collect {
                    when (it) {
                        is Resource.Success -> {
                            val map = it.data as Map<String, Any>
                            val list = map["list"] as List<CartDataModel>
                            if (list.isEmpty()) _cartDataDtoState.value = UiState.Empty()
                            else _cartDataDtoState.value = UiState.Success(map)
                            getCartCount()
                        }
                        is Resource.Error -> {
                            _cartDataDtoState.value = UiState.Error(it.message!!)
                        }
                        is Resource.Loading -> {
                            _cartDataDtoState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }

    fun updateCartItem(
        main_id: String,
        product_id: String,
        product_qty: String,
        product_price: String,
        with_installation: String
    ) {
        updateCartJob?.cancel()
        updateCartJob = viewModelScope.launch {
            withContext(coroutineContext) {
                val result = if (UserUtil.isUserLogin())
                    repository.updateCartItemApi(
                        main_id,
                        product_id,
                        product_qty,
                        product_price,
                        with_installation
                    )
                else
                    repository.updateCartItemDB(
                        main_id,
                        product_id,
                        product_qty,
                        product_price,
                        with_installation
                    )

                result.collect {
                    when (it) {
                        is Resource.Success -> cartData()
                        is Resource.Error -> {
                            _updateCartState.emit(
                                UiState.Error(it.message!!)
                            )
                        }
                        is Resource.Loading -> {
                            _updateCartState.emit(UiState.Loading())
                        }
                    }
                }
            }
        }
    }

    fun removeCartItem(main_id: String) {
        removeCartItemJob?.cancel()
        removeCartItemJob = viewModelScope.launch {
            withContext(coroutineContext) {
                val result = if (UserUtil.isUserLogin())
                    repository.removeCartItemApi(main_id)
                else
                    repository.removeCartItemDB(main_id)

                result.collect {
                    when (it) {
                        is Resource.Success -> cartData()
                        is Resource.Error -> {
                            _removeCartItemState.emit(
                                UiState.Error(it.message!!)
                            )
                        }
                        is Resource.Loading -> {
                            _removeCartItemState.emit(UiState.Loading())
                        }
                    }
                }
            }
        }
    }

    private fun getCartCount() {
        cartCountJob?.cancel()
        cartCountJob = viewModelScope.launch {
            withContext(coroutineContext) {
                val result = if (UserUtil.isUserLogin())
                    repository.getCartCountApi()
                else
                    repository.getCartCountDB()

                result.collect {
                    when (it) {
                        is Resource.Success -> _cartCountState.value = UiState.Success(it.data)
                        is Resource.Error -> {
                            _cartCountState.value =
                                UiState.Error(it.message!!)
                        }
                        is Resource.Loading -> {
                            _cartCountState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }
}