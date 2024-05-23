package eramo.amtalek.presentation.viewmodel.navbottom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.data.remote.dto.home.HomeResponse
import eramo.amtalek.data.remote.dto.products.orders.CartCountResponse
import eramo.amtalek.domain.model.OffersModel
import eramo.amtalek.domain.model.ResultModel
import eramo.amtalek.domain.model.auth.GetProfileModel
import eramo.amtalek.domain.model.auth.UserModel
import eramo.amtalek.domain.model.products.AdsModel
import eramo.amtalek.domain.model.products.CategoryModel
import eramo.amtalek.domain.model.products.ProductModel
import eramo.amtalek.domain.repository.CartRepository
import eramo.amtalek.domain.repository.HomeRepository
import eramo.amtalek.domain.usecase.drawer.GetProfileUseCase
import eramo.amtalek.domain.usecase.drawer.UpdateFirebaseDeviceTokenUseCase
import eramo.amtalek.domain.usecase.product.AddFavouriteUseCase
import eramo.amtalek.domain.usecase.product.HomeAdsUseCase
import eramo.amtalek.domain.usecase.product.HomeDealsByUserIdUseCase
import eramo.amtalek.domain.usecase.product.HomeFeaturedByUserIdUseCase
import eramo.amtalek.domain.usecase.product.HomeOffersUseCase
import eramo.amtalek.domain.usecase.product.HomeProductsByUserIdUseCase
import eramo.amtalek.domain.usecase.product.HomeProductsManufacturerByUserIdUseCase
import eramo.amtalek.domain.usecase.product.RemoveFavouriteUseCase
import eramo.amtalek.util.ANIMATION_DELAY
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.notification.FirebaseMessageReceiver
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeDealsByUserIdUseCase: HomeDealsByUserIdUseCase,
    private val homeProductsByUserIdUseCase: HomeProductsByUserIdUseCase,
    private val homeFeaturedByUserIdUseCase: HomeFeaturedByUserIdUseCase,
    private val homeProductsManufacturerByUserIdUseCase: HomeProductsManufacturerByUserIdUseCase,
    private val addFavouriteUseCase: AddFavouriteUseCase,
    private val removeFavouriteUseCase: RemoveFavouriteUseCase,
    private val updateFirebaseDeviceTokenUseCase: UpdateFirebaseDeviceTokenUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val homeAdsUseCase: HomeAdsUseCase,
    private val homeOffersUseCase: HomeOffersUseCase,
    private val cartRepository: CartRepository,
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _homeState = MutableStateFlow<UiState<HomeResponse>>(UiState.Empty())
    val homeState: StateFlow<UiState<HomeResponse>> = _homeState

    private val _homeFilteredByCityState = MutableStateFlow<UiState<HomeResponse>>(UiState.Empty())
    val homeFilteredByCityState: StateFlow<UiState<HomeResponse>> = _homeFilteredByCityState

    private val _initScreenState = MutableStateFlow<UiState<Boolean>>(UiState.Empty())
    val initScreenState: StateFlow<UiState<Boolean>> = _initScreenState

    private val _latestDealsState = MutableStateFlow<UiState<Map<String, Any>>>(UiState.Empty())
    val latestDealsState: StateFlow<UiState<Map<String, Any>>> = _latestDealsState

    private val _allProductsState = MutableStateFlow<UiState<List<ProductModel>>>(UiState.Empty())
    val allProductsState: StateFlow<UiState<List<ProductModel>>> = _allProductsState

    private val _allFeaturedState = MutableStateFlow<UiState<List<ProductModel>>>(UiState.Empty())
    val allFeaturedState: StateFlow<UiState<List<ProductModel>>> = _allFeaturedState

    private val _allProductsManufacturerState =
        MutableStateFlow<UiState<List<CategoryModel>>>(UiState.Empty())
    val allProductsManufacturerState: StateFlow<UiState<List<CategoryModel>>> =
        _allProductsManufacturerState

    private val _addFavouriteState = MutableSharedFlow<UiState<ResultModel>>()
    val addFavouriteState: SharedFlow<UiState<ResultModel>> = _addFavouriteState

    private val _removeFavouriteState = MutableSharedFlow<UiState<ResultModel>>()
    val removeFavouriteState: SharedFlow<UiState<ResultModel>> = _removeFavouriteState

    private val _getProfileState = MutableStateFlow<UiState<UserModel>>(UiState.Empty())
    val getProfileState: StateFlow<UiState<UserModel>> = _getProfileState

    private val _firebaseTokenState = MutableStateFlow<UiState<ResultModel>>(UiState.Empty())
    val firebaseTokenState: StateFlow<UiState<ResultModel>> = _firebaseTokenState

    private val _homeAdsState = MutableStateFlow<UiState<List<AdsModel>>>(UiState.Empty())
    val homeAdsState: StateFlow<UiState<List<AdsModel>>> = _homeAdsState

    private val _homeOffersState = MutableStateFlow<UiState<List<OffersModel>>>(UiState.Empty())
    val homeOffersState: StateFlow<UiState<List<OffersModel>>> = _homeOffersState

    private val _cartCountState = MutableStateFlow<UiState<CartCountResponse>>(UiState.Empty())
    val cartCountState: StateFlow<UiState<CartCountResponse>> = _cartCountState

    private var getHomeJob: Job? = null
    private var getHomeFilteredByCityJob: Job? = null
    private var initScreenJob: Job? = null
    private var latestDealsJob: Job? = null
    private var allProductsJob: Job? = null
    private var allFeaturedJob: Job? = null
    private var allProductsManufacturerJob: Job? = null
    private var addFavouriteJob: Job? = null
    private var removeFavouriteJob: Job? = null
    private var getProfileJob: Job? = null
    private var firebaseTokenJob: Job? = null
    private var homeAdsJob: Job? = null
    private var homeOffersJob: Job? = null
    private var cartCountJob: Job? = null

    fun cancelRequest() {
        getHomeJob?.cancel()
        initScreenJob?.cancel()
        latestDealsJob?.cancel()
        allProductsJob?.cancel()
        allFeaturedJob?.cancel()
        allProductsManufacturerJob?.cancel()
        addFavouriteJob?.cancel()
        removeFavouriteJob?.cancel()
        getProfileJob?.cancel()
        firebaseTokenJob?.cancel()
        homeAdsJob?.cancel()
        homeOffersJob?.cancel()
        cartCountJob?.cancel()
    }
    fun getScreenApis() {
        latestDealsJob?.cancel()
        latestDealsJob = viewModelScope.launch {
            _initScreenState.value = UiState.Loading()
            latestDeals()
            allProducts()
            allFeatured()
            allProductsManufacturer()
//            getProfile()
//            updateFirebaseToken(FirebaseMessageReceiver.token ?: "") waiting tell its done from backend
            getHomeAds()
            getHomeOffers()
            joinAll(
                latestDealsJob!!,
                allProductsJob!!,
                allFeaturedJob!!,
                allProductsManufacturerJob!!,
                getProfileJob!!,
                firebaseTokenJob!!,
                homeAdsJob!!,
                homeOffersJob!!
            )
            _initScreenState.value = UiState.Success(null)
        }
    }

     fun getProfile(type:String,id:String) {
        getProfileJob?.cancel()
        getProfileJob = viewModelScope.launch {
            withContext(coroutineContext) {
                getProfileUseCase(type, id).collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            saveUserInfo(result.data?.toUserModel()!!)
                            _getProfileState.value = UiState.Success(result.data.toUserModel())
                        }

                        is Resource.Error -> {
                            _getProfileState.value =
                                UiState.Error(result.message!!)
                            UserUtil.clearUserInfo()
                        }

                        is Resource.Loading -> {
                            _getProfileState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }

    private fun saveUserInfo(user: UserModel) {
        UserUtil.saveUserInfo(
            isRemember = true,
            userToken = UserUtil.getUserToken(), userID =  user.id.toString(),
            firstName = user.firstName, lastName = user.lastName, phone =  user.phone,
            email = user.email, countryId =  user.country.toString(),
            cityName = user.cityName, cityId =  user.city.toString(), countryName =  user.countryName, userBio = user.bio, profileImageUrl =  user.userImage,
            userType = user.actorType
        )
    }

    fun getHome() {
        getHomeJob?.cancel()
        getHomeJob = viewModelScope.launch {
            withContext(coroutineContext) {

                homeRepository.getHome().collect {
                    when (it) {
                        is Resource.Success -> {
                            _homeState.value = UiState.Success(it.data)
                        }

                        is Resource.Error -> {
                            _homeState.value = UiState.Error(it.message!!)
                        }

                        is Resource.Loading -> {
                            _homeState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }

    fun getHomeFilteredByCity(cityId: String) {
        getHomeFilteredByCityJob?.cancel()
        getHomeFilteredByCityJob = viewModelScope.launch {
            withContext(coroutineContext) {

                homeRepository.getHomeFilteredByCity(cityId).collect {
                    when (it) {
                        is Resource.Success -> {
                            _homeFilteredByCityState.value = UiState.Success(it.data)
                        }

                        is Resource.Error -> {
                            _homeFilteredByCityState.value = UiState.Error(it.message!!)
                        }

                        is Resource.Loading -> {
                            _homeFilteredByCityState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }


    fun latestDeals() {
        latestDealsJob?.cancel()
        latestDealsJob = viewModelScope.launch {
            delay(ANIMATION_DELAY)
            withContext(coroutineContext) {
                homeDealsByUserIdUseCase().collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { data ->
                                _latestDealsState.value = UiState.Success(data)
                            } ?: run { _latestDealsState.value = UiState.Empty() }
                        }

                        is Resource.Error -> {
                            _latestDealsState.value =
                                UiState.Error(result.message!!)
                        }

                        else -> Unit
                    }
                }
            }
        }
    }

    fun allProducts() {
        allProductsJob?.cancel()
        allProductsJob = viewModelScope.launch {
            delay(ANIMATION_DELAY)
            withContext(coroutineContext) {
                homeProductsByUserIdUseCase().collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { data ->
                                _allProductsState.value = UiState.Success(data)
                            } ?: run { _allProductsState.value = UiState.Empty() }
                        }

                        is Resource.Error -> {
                            _allProductsState.value =
                                UiState.Error(result.message!!)
                        }

                        else -> Unit
                    }
                }
            }
        }
    }

    fun allFeatured() {
        allFeaturedJob?.cancel()
        allFeaturedJob = viewModelScope.launch {
            delay(ANIMATION_DELAY)
            withContext(coroutineContext) {
                homeFeaturedByUserIdUseCase().collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { data ->
                                _allFeaturedState.value = UiState.Success(data)
                            } ?: run { _allFeaturedState.value = UiState.Empty() }
                        }

                        is Resource.Error -> {
                            _allFeaturedState.value =
                                UiState.Error(result.message!!)
                        }

                        else -> Unit
                    }
                }
            }
        }
    }

    fun allProductsManufacturer() {
        allProductsManufacturerJob?.cancel()
        allProductsManufacturerJob = viewModelScope.launch {
            delay(ANIMATION_DELAY)
            withContext(coroutineContext) {
                homeProductsManufacturerByUserIdUseCase().collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { data ->
                                _allProductsManufacturerState.value = UiState.Success(data)
                            } ?: run { _allProductsManufacturerState.value = UiState.Empty() }
                        }

                        is Resource.Error -> {
                            _allProductsManufacturerState.value =
                                UiState.Error(result.message!!)
                        }

                        else -> Unit
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

    fun updateFirebaseToken(deviceToken: String) {
        firebaseTokenJob?.cancel()
        firebaseTokenJob = viewModelScope.launch {
            withContext(coroutineContext) {
                updateFirebaseDeviceTokenUseCase(deviceToken).collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let {
                                _firebaseTokenState.value = UiState.Success(it)
                            } ?: run { _firebaseTokenState.value = UiState.Empty() }
                        }

                        is Resource.Error -> {
                            _firebaseTokenState.value =
                                UiState.Error(result.message!!)
                        }

                        else -> Unit
                    }
                }
            }
        }
    }

    fun getHomeAds() {
        homeAdsJob?.cancel()
        homeAdsJob = viewModelScope.launch {
            delay(ANIMATION_DELAY)
            withContext(coroutineContext) {
                homeAdsUseCase().collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let {
                                _homeAdsState.value = UiState.Success(it)
                            } ?: run { _homeAdsState.value = UiState.Empty() }
                        }

                        is Resource.Error -> {
                            _homeAdsState.value =
                                UiState.Error(result.message!!)
                        }

                        else -> Unit
                    }
                }
            }
        }
    }

    fun getHomeOffers() {
        homeOffersJob?.cancel()
        homeOffersJob = viewModelScope.launch {
            delay(ANIMATION_DELAY)
            withContext(coroutineContext) {
                homeOffersUseCase().collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let {
                                _homeOffersState.value = UiState.Success(it)
                            } ?: run { _homeOffersState.value = UiState.Empty() }
                        }

                        is Resource.Error -> {
                            _homeOffersState.value =
                                UiState.Error(result.message!!)
                        }

                        else -> Unit
                    }
                }
            }
        }
    }

    fun getCartCount() {
        cartCountJob?.cancel()
        cartCountJob = viewModelScope.launch {
            delay(ANIMATION_DELAY)
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