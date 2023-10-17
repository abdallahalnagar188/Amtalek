package eramo.amtalek.presentation.viewmodel.navbottom.extension

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.domain.model.ResultModel
import eramo.amtalek.domain.model.products.ProductModel
import eramo.amtalek.domain.model.request.SearchRequest
import eramo.amtalek.domain.repository.ProductsRepository
import eramo.amtalek.domain.usecase.product.AddFavouriteUseCase
import eramo.amtalek.domain.usecase.product.RemoveFavouriteUseCase
import eramo.amtalek.util.ANIMATION_DELAY
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
class SearchViewModel @Inject constructor(
    private val productsRepository: ProductsRepository,
    private val addFavouriteUseCase: AddFavouriteUseCase,
    private val removeFavouriteUseCase: RemoveFavouriteUseCase,
) : ViewModel() {

    private val _filterState = MutableStateFlow<UiState<List<ProductModel>>>(UiState.Empty())
    val filterState: StateFlow<UiState<List<ProductModel>>> = _filterState

    private val _searchState = MutableStateFlow<UiState<List<ProductModel>>>(UiState.Empty())
    val searchState: StateFlow<UiState<List<ProductModel>>> = _searchState

    private val _addFavouriteState = MutableSharedFlow<UiState<ResultModel>>()
    val addFavouriteState: SharedFlow<UiState<ResultModel>> = _addFavouriteState

    private val _removeFavouriteState = MutableSharedFlow<UiState<ResultModel>>()
    val removeFavouriteState: SharedFlow<UiState<ResultModel>> = _removeFavouriteState

    private var filterJob: Job? = null
    private var searchJob: Job? = null
    private var addFavouriteJob: Job? = null
    private var removeFavouriteJob: Job? = null

    fun getScreenApi(searchTitle: String, filterRequest: SearchRequest?) {
        if (searchTitle.isNotEmpty()) productSearch(searchTitle)
        else productFilter(filterRequest!!)
    }

    fun cancelRequest() {
        filterJob?.cancel()
        searchJob?.cancel()
        addFavouriteJob?.cancel()
        removeFavouriteJob?.cancel()
    }

    fun productFilter(filterRequest: SearchRequest) {
        filterJob?.cancel()
        filterJob = viewModelScope.launch {
            delay(ANIMATION_DELAY)
            withContext(coroutineContext) {
                productsRepository.productFilter(filterRequest).collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { data ->
                                _filterState.value = UiState.Success(data)
                            } ?: run { _filterState.value = UiState.Empty() }
                        }

                        is Resource.Error -> {
                            _filterState.value =
                                UiState.Error(result.message!!)
                        }

                        is Resource.Loading -> {
                            _filterState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }

    fun productSearch(title: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(ANIMATION_DELAY)
            withContext(coroutineContext) {
                productsRepository.productSearch(title).collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _searchState.value = UiState.Success(result.data)
                        }

                        is Resource.Error -> {
                            _searchState.value =
                                UiState.Error(result.message!!)
                        }

                        is Resource.Loading -> {
                            _searchState.value = UiState.Loading()
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
}