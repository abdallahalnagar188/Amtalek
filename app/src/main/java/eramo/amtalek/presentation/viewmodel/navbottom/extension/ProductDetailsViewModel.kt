package eramo.amtalek.presentation.viewmodel.navbottom.extension

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.data.remote.dto.products.orders.CartCountResponse
import eramo.amtalek.domain.model.ResultModel
import eramo.amtalek.domain.model.products.ProductModel
import eramo.amtalek.domain.repository.CartRepository
import eramo.amtalek.domain.usecase.product.AddFavouriteUseCase
import eramo.amtalek.domain.usecase.product.GetProductByIdUseCase
import eramo.amtalek.domain.usecase.product.RemoveFavouriteUseCase
import eramo.amtalek.util.ANIMATION_DELAY
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val addFavouriteUseCase: AddFavouriteUseCase,
    private val removeFavouriteUseCase: RemoveFavouriteUseCase,
    private val repository: CartRepository
) : ViewModel() {

    private val _productState = MutableStateFlow<UiState<ProductModel>>(UiState.Empty())
    val productState: StateFlow<UiState<ProductModel>> = _productState

    private val _addFavouriteState = MutableSharedFlow<UiState<ResultModel>>()
    val addFavouriteState: SharedFlow<UiState<ResultModel>> = _addFavouriteState

    private val _removeFavouriteState = MutableSharedFlow<UiState<ResultModel>>()
    val removeFavouriteState: SharedFlow<UiState<ResultModel>> = _removeFavouriteState

    private val _addToCartState = MutableSharedFlow<UiState<ResultModel>>()
    val addToCartState: SharedFlow<UiState<ResultModel>> = _addToCartState

    private val _cartCountState = MutableStateFlow<UiState<CartCountResponse>>(UiState.Empty())
    val cartCountState: StateFlow<UiState<CartCountResponse>> = _cartCountState

    private var productJob: Job? = null
    private var addFavouriteJob: Job? = null
    private var removeFavouriteJob: Job? = null
    private var questJob: Job? = null
    private var addToCartJob: Job? = null
    private var cartCountJob: Job? = null

    fun cancelRequest() {
        productJob?.cancel()
        addFavouriteJob?.cancel()
        removeFavouriteJob?.cancel()
        questJob?.cancel()
        addToCartJob?.cancel()
        cartCountJob?.cancel()
    }

    fun getProduct(productId: String) {
        productJob?.cancel()
        productJob = viewModelScope.launch {
            withContext(coroutineContext) {
                delay(ANIMATION_DELAY)
                getProductByIdUseCase(productId).collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let {
                                _productState.emit(UiState.Success(it))
                            } ?: run { _productState.emit(UiState.Empty()) }
                        }

                        is Resource.Error -> {
                            _productState.emit(
                                UiState.Error(result.message!!)
                            )
                        }

                        is Resource.Loading -> {
                            _productState.emit(UiState.Loading())
                        }
                    }
                }
            }
        }
    }

    fun addFavourite(property_id: String) {
        addFavouriteJob?.cancel()
        addFavouriteJob = viewModelScope.launch {
            withContext(coroutineContext) {
                addFavouriteUseCase(property_id).collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let {
                                _addFavouriteState.emit(UiState.Success(it))
                            } ?: run { _addFavouriteState.emit(UiState.Empty()) }
                        }

                        is Resource.Error -> {
                            _addFavouriteState.emit(
                                UiState.Error(result.message!!)
                            )
                        }

                        is Resource.Loading -> {
                            _addFavouriteState.emit(UiState.Loading())
                        }
                    }
                }
            }
        }
    }

    fun removeFavourite(property_id: String) {
        removeFavouriteJob?.cancel()
        removeFavouriteJob = viewModelScope.launch {
            withContext(coroutineContext) {
                removeFavouriteUseCase(property_id).collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let {
                                _removeFavouriteState.emit(UiState.Success(it))
                            } ?: run { _removeFavouriteState.emit(UiState.Empty()) }
                        }

                        is Resource.Error -> {
                            _removeFavouriteState.emit(
                                UiState.Error(result.message!!)
                            )
                        }

                        is Resource.Loading -> {
                            _removeFavouriteState.emit(UiState.Loading())
                        }
                    }
                }
            }
        }
    }

    fun addToCart(
        productModel: ProductModel,
        product_id: String,
        product_qty: String,
        product_price: String,
        with_installation: String
    ) {
        addToCartJob = viewModelScope.launch {
            withContext(coroutineContext) {
                val result = if (UserUtil.isUserLogin()) {
                    repository.addToCartApi(
                        product_id,
                        product_qty,
                        product_price,
                        with_installation
                    )
                } else {
                    repository.addToCartDB(productModel, product_qty)
                }

                result.collect { it ->
                    when (it) {
                        is Resource.Success -> {
                            it.data?.let {
                                _addToCartState.emit(UiState.Success(it))
                            } ?: run { _addToCartState.emit(UiState.Empty()) }
                            getCartCount()
                        }

                        is Resource.Error -> {
                            _addToCartState.emit(
                                UiState.Error(it.message!!)
                            )
                        }

                        is Resource.Loading -> {
                            _addToCartState.emit(UiState.Loading())
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

                result.collect { it ->
                    when (it) {
                        is Resource.Success -> {
                            it.data?.let {
                                _cartCountState.value = UiState.Success(it)
                            } ?: run { _cartCountState.value = UiState.Empty() }
                        }

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