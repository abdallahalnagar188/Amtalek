package eramo.amtalek.presentation.viewmodel.navbottom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.data.remote.dto.products.orders.CartCountResponse
import eramo.amtalek.domain.model.ResultModel
import eramo.amtalek.domain.model.products.CategoryModel
import eramo.amtalek.domain.model.products.ProductModel
import eramo.amtalek.domain.repository.CartRepository
import eramo.amtalek.domain.usecase.product.*
import eramo.amtalek.util.Constants
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(
    private val homeProductsManufacturerByUserIdUseCase: HomeProductsManufacturerByUserIdUseCase,
    private val allProductsByUserIdUseCase: AllProductsByUserIdUseCase,
    private val allCategorizationByUserIdUseCase: AllCategorizationByUserIdUseCase,
    private val addFavouriteUseCase: AddFavouriteUseCase,
    private val removeFavouriteUseCase: RemoveFavouriteUseCase,
    private val cartRepository: CartRepository,
) : ViewModel() {

    private val _allProductsState = MutableStateFlow<PagingData<ProductModel>?>(null)
    val allProductsState: StateFlow<PagingData<ProductModel>?> = _allProductsState

    private val _productsByCatState = MutableStateFlow<PagingData<ProductModel>?>(null)
    val productsByCatState: StateFlow<PagingData<ProductModel>?> = _productsByCatState

    private val _allCategoriesState =
        MutableStateFlow<UiState<List<CategoryModel>>>(UiState.Empty())
    val allCategoriesState: StateFlow<UiState<List<CategoryModel>>> =
        _allCategoriesState

    private val _addFavouriteState = MutableSharedFlow<UiState<ResultModel>>()
    val addFavouriteState: SharedFlow<UiState<ResultModel>> = _addFavouriteState

    private val _removeFavouriteState = MutableSharedFlow<UiState<ResultModel>>()
    val removeFavouriteState: SharedFlow<UiState<ResultModel>> = _removeFavouriteState

    private val _cartCountState = MutableStateFlow<UiState<CartCountResponse>>(UiState.Empty())
    val cartCountState: StateFlow<UiState<CartCountResponse>> = _cartCountState

    private var allProductsJob: Job? = null
    private var productsByCatJob: Job? = null
    private var allCategoriesJob: Job? = null
    private var addFavouriteJob: Job? = null
    private var removeFavouriteJob: Job? = null
    private var cartCountJob: Job? = null

    fun getScreenApis(manufacturerId: String) {
        allCategories()
        if (manufacturerId.isEmpty()) allProducts()
        else productsByCat(manufacturerId)
    }

    fun cancelRequest() {
        allProductsJob?.cancel()
        productsByCatJob?.cancel()
        allCategoriesJob?.cancel()
        addFavouriteJob?.cancel()
        removeFavouriteJob?.cancel()
        cartCountJob?.cancel()
    }

    fun allProducts() {
        allProductsJob?.cancel()
        allProductsJob = viewModelScope.launch {
            delay(Constants.ANIMATION_DELAY)
            withContext(coroutineContext) {
                allProductsByUserIdUseCase().cachedIn(viewModelScope).collect {
                    _allProductsState.value = it
                }
            }
        }
    }

    fun productsByCat(catId: String) {
        productsByCatJob?.cancel()
        productsByCatJob = viewModelScope.launch {
            delay(Constants.ANIMATION_DELAY)
            withContext(coroutineContext) {
                allCategorizationByUserIdUseCase(catId).cachedIn(viewModelScope).collect {
                    _productsByCatState.value = it
                }
            }
        }
    }

    fun allCategories() {
        allCategoriesJob?.cancel()
        allCategoriesJob = viewModelScope.launch {
            delay(Constants.ANIMATION_DELAY)
            withContext(coroutineContext) {
                homeProductsManufacturerByUserIdUseCase().collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { data ->
                                _allCategoriesState.value = UiState.Success(data)
                            } ?: run { _allCategoriesState.value = UiState.Empty() }
                        }
                        is Resource.Error -> {
                            _allCategoriesState.value =
                                UiState.Error(result.message!!)
                        }
                        is Resource.Loading -> {
                            _allCategoriesState.value = UiState.Loading()
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
                            allProducts()
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
                            allProducts()
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

    fun getCartCount() {
        cartCountJob?.cancel()
        cartCountJob = viewModelScope.launch {
            delay(Constants.ANIMATION_DELAY)
            withContext(coroutineContext) {
                val result = if (UserUtil.isUserLogin())
                    cartRepository.getCartCountApi()
                else
                    cartRepository.getCartCountDB()

                result.collect { it ->
                    when (it) {
                        is Resource.Success -> {
                            it.data?.let {
                                _cartCountState.value = UiState.Success(it)
                            } ?: run { _cartCountState.value = UiState.Empty() }
                        }
                        is Resource.Error -> {
                            _cartCountState.value = UiState.Error(it.message!!)
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