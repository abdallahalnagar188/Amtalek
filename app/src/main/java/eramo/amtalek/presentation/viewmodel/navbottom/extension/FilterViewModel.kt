package eramo.amtalek.presentation.viewmodel.navbottom.extension

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.data.remote.dto.products.search.PriceResponse
import eramo.amtalek.domain.model.FilterCategoryModel
import eramo.amtalek.domain.model.products.CategoryModel
import eramo.amtalek.domain.repository.ProductsRepository
import eramo.amtalek.domain.usecase.product.GetFilterCategoriesUseCase
import eramo.amtalek.domain.usecase.product.HomeProductsManufacturerByUserIdUseCase
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
class FilterViewModel @Inject constructor(
    private val productsRepository: ProductsRepository,
    private val getFilterCategoriesUseCase: GetFilterCategoriesUseCase,
) : ViewModel() {

    private val _filterCategoriesState =
        MutableStateFlow<UiState<List<FilterCategoryModel>>>(UiState.Empty())
    val filterCategoriesState: StateFlow<UiState<List<FilterCategoryModel>>> =
        _filterCategoriesState

    private val _maxPriceState = MutableStateFlow<UiState<PriceResponse>>(UiState.Empty())
    val maxPriceState: StateFlow<UiState<PriceResponse>> = _maxPriceState

    private val _minPriceState = MutableStateFlow<UiState<PriceResponse>>(UiState.Empty())
    val minPriceState: StateFlow<UiState<PriceResponse>> = _minPriceState

    private var filterCategoriesJob: Job? = null
    private var maxPriceJob: Job? = null
    private var minPriceJob: Job? = null

    fun cancelRequest() {
        filterCategoriesJob?.cancel()
        maxPriceJob?.cancel()
        minPriceJob?.cancel()
    }

    fun getScreenApis() {
        filterCategories()
        maxProductPrice()
        minProductPrice()
    }

    private fun filterCategories() {
        filterCategoriesJob?.cancel()
        filterCategoriesJob = viewModelScope.launch {
            delay(Constants.ANIMATION_DELAY)
            withContext(coroutineContext) {
                getFilterCategoriesUseCase().collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let {
                                _filterCategoriesState.value = UiState.Success(it)
                            } ?: run { _filterCategoriesState.value = UiState.Empty() }
                        }
                        is Resource.Error -> {
                            _filterCategoriesState.value =
                                UiState.Error(result.message!!)
                        }
                        is Resource.Loading -> {
                            _filterCategoriesState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }

    private fun maxProductPrice() {
        maxPriceJob?.cancel()
        maxPriceJob = viewModelScope.launch {
            delay(Constants.ANIMATION_DELAY)
            withContext(coroutineContext) {
                productsRepository.maxProductPrice().collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let {
                                _maxPriceState.value = UiState.Success(it)
                            } ?: run { _maxPriceState.value = UiState.Empty() }
                        }
                        is Resource.Error -> {
                            _maxPriceState.value =
                                UiState.Error(result.message!!)
                        }
                        is Resource.Loading -> {
                            _maxPriceState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }

    private fun minProductPrice() {
        minPriceJob?.cancel()
        minPriceJob = viewModelScope.launch {
            delay(Constants.ANIMATION_DELAY)
            withContext(coroutineContext) {
                productsRepository.minProductPrice().collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let {
                                _minPriceState.value = UiState.Success(it)
                            } ?: run { _minPriceState.value = UiState.Empty() }
                        }
                        is Resource.Error -> {
                            _minPriceState.value =
                                UiState.Error(result.message!!)
                        }
                        is Resource.Loading -> {
                            _minPriceState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }
}