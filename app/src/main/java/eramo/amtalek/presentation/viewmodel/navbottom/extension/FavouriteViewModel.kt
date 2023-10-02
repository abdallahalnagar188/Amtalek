package eramo.amtalek.presentation.viewmodel.navbottom.extension

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.domain.model.ResultModel
import eramo.amtalek.domain.model.products.ProductModel
import eramo.amtalek.domain.usecase.product.RemoveFavouriteUseCase
import eramo.amtalek.domain.usecase.product.UserFavListByUserIdUseCase
import eramo.amtalek.util.Constants
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
class FavouriteViewModel @Inject constructor(
    private val userFavListByUserIdUseCase: UserFavListByUserIdUseCase,
    private val removeFavouriteUseCase: RemoveFavouriteUseCase,
) : ViewModel() {

    private val _userFavState = MutableStateFlow<UiState<List<ProductModel>>>(UiState.Empty())
    val userFavState: StateFlow<UiState<List<ProductModel>>> = _userFavState

    private val _removeFavouriteState = MutableSharedFlow<UiState<ResultModel>>()
    val removeFavouriteState: SharedFlow<UiState<ResultModel>> = _removeFavouriteState

    private var userFavJob: Job? = null
    private var removeFavouriteJob: Job? = null

    fun cancelRequest() {
        userFavJob?.cancel()
    }

    fun userFav() {
        userFavJob?.cancel()
        userFavJob = viewModelScope.launch {
            delay(Constants.ANIMATION_DELAY)
            withContext(coroutineContext) {
                userFavListByUserIdUseCase().collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _userFavState.value = UiState.Success(result.data)
                        }
                        is Resource.Error -> {
                            _userFavState.value = UiState.Error(result.message!!)
                        }
                        is Resource.Loading -> {
                            _userFavState.value = UiState.Loading()
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