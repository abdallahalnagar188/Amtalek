package eramo.amtalek.presentation.ui.main.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.fav.AddOrRemoveFavResponse
import eramo.amtalek.data.remote.dto.myHome.allCitys.AllCityResponse
import eramo.amtalek.data.remote.dto.myHome.news.NewsDetailsResponse
import eramo.amtalek.data.remote.dto.myHome.news.newsDetails.NewsDetailsResponseX
import eramo.amtalek.data.remote.dto.myHome.news.newscateg.NewsCategoryResponse
import eramo.amtalek.data.remote.dto.project.allProjects.AllProjectsResponse
import eramo.amtalek.data.remote.dto.property.allproperty.AllPropertyResponse
import eramo.amtalek.data.remote.dto.property.allproperty.DataX
import eramo.amtalek.domain.model.auth.UserModel
import eramo.amtalek.domain.model.home.HomeExtraSectionsModel
import eramo.amtalek.domain.model.home.cities.HomeCitiesSectionsModel
import eramo.amtalek.domain.model.home.news.HomeNewsSectionsModel
import eramo.amtalek.domain.model.home.project.HomeProjectsSectionModel
import eramo.amtalek.domain.model.home.property.HomePropertySectionModel
import eramo.amtalek.domain.model.home.slider.SliderModel
import eramo.amtalek.domain.repository.AddOrRemoveFavRepository
import eramo.amtalek.domain.repository.AllNewsRepo
import eramo.amtalek.domain.repository.AllNormalPropertiesRepo
import eramo.amtalek.domain.repository.AllPropertyRepo
import eramo.amtalek.domain.repository.MyHomeRepository
import eramo.amtalek.domain.repository.NewsCategoryRepo
import eramo.amtalek.domain.repository.NewsDetailsRepo
import eramo.amtalek.domain.usecase.allcitys.GetAllCities
import eramo.amtalek.domain.usecase.allprojects.GetAllProjects
import eramo.amtalek.domain.usecase.allpropety.GetAllNormalProperty
import eramo.amtalek.domain.usecase.allpropety.GetAllProperty
import eramo.amtalek.domain.usecase.drawer.GetProfileUseCase
import eramo.amtalek.presentation.ui.main.home.seemore.AllPropertiesPagingSource
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeMyViewModel @Inject constructor(
    private val homeRepository: MyHomeRepository,
    private val getProfileUseCase: GetProfileUseCase,
    private val addOrRemoveFavRepository: AddOrRemoveFavRepository,
    private val allPropertyUseCase: GetAllProperty,
    private val allProjectsUseCase: GetAllProjects,
    private val allNormalPropertyUseCase: GetAllNormalProperty,
    private val allCitiesUseCase: GetAllCities,
    private val allPropertyRepo: AllPropertyRepo,
    private val allNormalProperty: AllNormalPropertiesRepo,
    private val api: AmtalekApi,// Inject BrokerApi for PagingSource
    private val newsRepo: AllNewsRepo,
    private val newsCategoryUseCase: NewsCategoryRepo,
    private val newsDetailsRepo: NewsDetailsRepo,

) : ViewModel() {

    private val _allProperty: MutableStateFlow<AllPropertyResponse?> = MutableStateFlow(null)
    val allProperty: StateFlow<AllPropertyResponse?> get() = _allProperty

    private val _allNormalProperty: MutableStateFlow<HomePropertySectionModel?> = MutableStateFlow(null)
    // val allNormalProperty: StateFlow<HomePropertySectionModel?> get() = _allNormalProperty

    private val _allProject: MutableStateFlow<AllProjectsResponse?> = MutableStateFlow(null)
    val allProject: StateFlow<AllProjectsResponse?> get() = _allProject

    private val _allCities: MutableStateFlow<AllCityResponse?> = MutableStateFlow(null)
    val allCities: StateFlow<AllCityResponse?> get() = _allCities

    private val _homeFeaturedPropertiesState = MutableStateFlow<UiState<List<HomePropertySectionModel>>>(UiState.Empty())
    val homeFeaturedPropertiesState: StateFlow<UiState<List<HomePropertySectionModel>>> = _homeFeaturedPropertiesState

    private val _homeProjectsState = MutableStateFlow<UiState<List<HomeProjectsSectionModel>>>(UiState.Empty())
    val homeProjectsState: StateFlow<UiState<List<HomeProjectsSectionModel>>> = _homeProjectsState

    private val _homeFilterByCityState = MutableStateFlow<UiState<List<HomeCitiesSectionsModel>>>(UiState.Empty())
    val homeFilterByCityState: StateFlow<UiState<List<HomeCitiesSectionsModel>>> = _homeFilterByCityState

    private val _homeSliderState = MutableStateFlow<UiState<List<SliderModel>>>(UiState.Empty())
    val homeSliderState: StateFlow<UiState<List<SliderModel>>> = _homeSliderState

    private val _clickOnAd = MutableStateFlow<UiState<List<SliderModel>>>(UiState.Empty())
    val clickOnAd: StateFlow<UiState<List<SliderModel>>> = _clickOnAd

    private val _homeMostViewedPropertiesState = MutableStateFlow<UiState<List<HomePropertySectionModel>>>(UiState.Empty())
    val homeMostViewedPropertiesState: StateFlow<UiState<List<HomePropertySectionModel>>> = _homeMostViewedPropertiesState

    private val _homeNormalPropertiesState = MutableStateFlow<UiState<List<HomePropertySectionModel>>>(UiState.Empty())
    val homeNormalPropertiesState: StateFlow<UiState<List<HomePropertySectionModel>>> = _homeNormalPropertiesState


    private val _homeNewsState = MutableStateFlow<UiState<List<HomeNewsSectionsModel>>>(UiState.Empty())
    val homeNewsState: StateFlow<UiState<List<HomeNewsSectionsModel>>> = _homeNewsState

    private val _homeExtraSectionsState = MutableStateFlow<UiState<List<HomeExtraSectionsModel>>>(UiState.Empty())
    val homeExtraSectionsState: StateFlow<UiState<List<HomeExtraSectionsModel>>> = _homeExtraSectionsState


    private val _getProfileState = MutableStateFlow<UiState<UserModel>>(UiState.Empty())
    val getProfileState: StateFlow<UiState<UserModel>> = _getProfileState

    private val _initScreenState = MutableStateFlow<UiState<Boolean>>(UiState.Empty())
    val initScreenState: StateFlow<UiState<Boolean>> = _initScreenState

    private val _favState = MutableStateFlow<UiState<AddOrRemoveFavResponse>>(UiState.Empty())
    val favState: StateFlow<UiState<AddOrRemoveFavResponse>> = _favState

    private val _newsCategory = MutableStateFlow<Resource<NewsCategoryResponse>>(Resource.Loading())
    val newsCategory: StateFlow<Resource<NewsCategoryResponse>> = _newsCategory

    private val _newsDetails = MutableStateFlow<Resource<NewsDetailsResponseX>>(Resource.Loading())
    val newsDetails: StateFlow<Resource<NewsDetailsResponseX>> = _newsDetails

    //---------------------------------------------------------------------------------//
    private var initScreenJob: Job? = null
    private var getHomeFeaturedPropertiesJob: Job? = null
    private var getHomeProjectsJob: Job? = null
    private var getHomeFilterByCityJob: Job? = null
    private var getHomeSliderJob: Job? = null
    private var getHomeMostViewedPropertiesJob: Job? = null
    private var getHomeNormalPropertiesJob: Job? = null
    private var getHomeNewsJob: Job? = null
    private var getHomeExtraSectionsJob: Job? = null
    private var getProfileJob: Job? = null

    fun cancelRequests() {
        initScreenJob?.cancel()
        getHomeFeaturedPropertiesJob?.cancel()
        getHomeProjectsJob?.cancel()
        getHomeFilterByCityJob?.cancel()
        getHomeNormalPropertiesJob?.cancel()
        getHomeSliderJob?.cancel()
        getHomeMostViewedPropertiesJob?.cancel()
        getHomeNewsJob?.cancel()
        getHomeExtraSectionsJob?.cancel()
        getProfileJob?.cancel()
    }


    val allPropertiesPagingData: Flow<PagingData<DataX>> = Pager(
        config = PagingConfig(
            pageSize = 1,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { AllPropertiesPagingSource(api) }
    ).flow
        .cachedIn(viewModelScope)

    val allNormalPropertyPagingFlow: Flow<PagingData<DataX>> = Pager(
        config = PagingConfig(
            pageSize = 1,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { allNormalProperty.getAllNormalPropertiesPagingSource() }
    ).flow
        .cachedIn(viewModelScope)

    val allNewsPagingFlow: Flow<PagingData<eramo.amtalek.data.remote.dto.myHome.news.allnews.DataX>> = Pager(
        config = PagingConfig(
            pageSize = 1,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { newsRepo.getAllNewsPagingSource() }
    ).flow
        .cachedIn(viewModelScope)

    //---------------------------------------------------------------------------------//

    fun getNewsDetails(id: String) {
        viewModelScope.launch {
            newsDetailsRepo.getNewsDetails(id).collect {
                when (it) {
                    is Resource.Success -> {
                        _newsDetails.value = Resource.Success(it.data)
                    }
                    is Resource.Error -> {
                        _newsDetails.value = Resource.Error(it.message!!)
                    }

                    is Resource.Loading -> {
                        _newsDetails.value = Resource.Loading()
                    }
                }
            }
        }
    }
    private fun getHomeFeaturedProperties(cityId: String) {
        getHomeFeaturedPropertiesJob?.cancel()
        getHomeFeaturedPropertiesJob = viewModelScope.launch(Dispatchers.IO) {

            homeRepository.getHomeFeaturedProperty(cityId).collect() { result ->
                when (result) {
                    is Resource.Success -> {
                        Log.e("getHomeFeatured success", result.data.toString())
                        ///its a list cuz the response data is array of a single object** careful with that
                        val list = mutableListOf<HomePropertySectionModel>()
                        for (item in result.data?.data!!) {
                            list.add(item.toHomePropertySectionModel())
                        }
                        _homeFeaturedPropertiesState.emit(UiState.Success(list))
                    }

                    is Resource.Error -> {
                        _homeFeaturedPropertiesState.emit(UiState.Error(result.message!!))
                    }

                    is Resource.Loading -> {
                        _homeFeaturedPropertiesState.emit(UiState.Loading())
                    }
                }
            }
        }

    }

    private fun getHomeProjects(cityId: String) {
        getHomeProjectsJob?.cancel()
        getHomeProjectsJob = viewModelScope.launch(Dispatchers.IO) {

            homeRepository.getHomeProjects(cityId).collect() { result ->
                when (result) {
                    is Resource.Success -> {
                        val list = mutableListOf<HomeProjectsSectionModel>()
                        for (item in result.data?.data!!) {
                            list.add(item.toHomeProjectsSectionModel())
                        }
                        _homeProjectsState.emit(UiState.Success(list))
                    }

                    is Resource.Error -> {
                        _homeProjectsState.emit(UiState.Error(result.message!!))
                    }

                    is Resource.Loading -> {
                        _homeProjectsState.emit(UiState.Loading())
                    }
                }

            }
        }
    }

    fun getNewsCategory(id: Int) {
        viewModelScope.launch {
            newsCategoryUseCase.getAllNewsCategories(id).collectLatest {
                when (it) {
                    is Resource.Success -> {
                        _newsCategory.value = Resource.Success(it.data)
                    }

                    is Resource.Error -> {
                        _newsCategory.emit(Resource.Error(it.message!!))
                    }

                    is Resource.Loading -> {
                        _newsCategory.emit(Resource.Loading())
                    }
                }
            }
        }
    }

    private fun getHomeFilterByCity(countryId: String) {
        getHomeFilterByCityJob?.cancel()
        getHomeFilterByCityJob = viewModelScope.launch(Dispatchers.IO) {

            homeRepository.getFilterByCity(countryId = countryId).collect() { result ->
                when (result) {
                    is Resource.Success -> {
                        val list = mutableListOf<HomeCitiesSectionsModel>()
                        for (item in result.data?.data!!) {
                            list.add(item.toHomeCitiesSectionsModel())
                        }
                        _homeFilterByCityState.emit(UiState.Success(list))
                    }

                    is Resource.Error -> {
                        _homeFilterByCityState.emit(UiState.Error(result.message!!))
                    }

                    is Resource.Loading -> {
                        _homeFilterByCityState.emit(UiState.Loading())
                    }
                }

            }
        }

    }

    private fun getHomeSlider() {
        getHomeSliderJob?.cancel()
        getHomeSliderJob = viewModelScope.launch(Dispatchers.IO) {

            homeRepository.getHomeSlider().collect() { result ->
                when (result) {
                    is Resource.Success -> {
                        val list = mutableListOf<SliderModel>()
                        for (item in result.data?.data!!) {
                            if (item != null) {
                                list.add(item.toSliderModel())
                            }
                        }
                        _homeSliderState.emit(UiState.Success(list))
                    }

                    is Resource.Error -> {
                        _homeSliderState.emit(UiState.Error(result.message!!))
                    }

                    is Resource.Loading -> {
                        _homeSliderState.emit(UiState.Loading())
                    }
                }


            }
        }
    }

    fun clickedOnAd(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            homeRepository.clickedOnAds(id).collect() { result ->
                when (result) {
                    is Resource.Success -> {
                        val list = mutableListOf<SliderModel>()
                        for (item in result.data?.data!!) {
                            if (item != null) {
                                list.add(item.toSliderModel())
                            }
                        }
                        _clickOnAd.emit(UiState.Success(list))
                    }

                    is Resource.Error -> {
                        _clickOnAd.emit(UiState.Error(result.message!!))
                    }

                    is Resource.Loading -> {
                        _clickOnAd.emit(UiState.Loading())
                    }
                }


            }
        }
    }

    fun getHomeNormalProperties(cityId: String) {
        getHomeNormalPropertiesJob?.cancel()
        getHomeNormalPropertiesJob = viewModelScope.launch(Dispatchers.IO) {

            homeRepository.getHomeNormalProperties(cityId).collect() { result ->
                when (result) {
                    is Resource.Success -> {
                        val list = mutableListOf<HomePropertySectionModel>()
                        for (item in result.data?.data!!) {
                            list.add(item.toHomePropertySectionModel())
                        }
                        _homeNormalPropertiesState.emit(UiState.Success(list))
                    }

                    is Resource.Error -> {
                        _homeNormalPropertiesState.emit(UiState.Error(result.message!!))
                    }

                    is Resource.Loading -> {
                        _homeNormalPropertiesState.emit(UiState.Loading())
                    }
                }
            }
        }
    }

    private fun getMostViewedProperties(countryId: String) {
        getHomeMostViewedPropertiesJob?.cancel()
        getHomeMostViewedPropertiesJob = viewModelScope.launch(Dispatchers.IO) {

            homeRepository.getHomeMostViewedProperties(countryId).collect() { result ->
                when (result) {
                    is Resource.Success -> {
                        val list = mutableListOf<HomePropertySectionModel>()
                        for (item in result.data?.data!!) {
                            list.add(item.toHomePropertySectionModel())
                        }
                        _homeMostViewedPropertiesState.emit(UiState.Success(list))
                    }

                    is Resource.Error -> {
                        _homeMostViewedPropertiesState.emit(UiState.Error(result.message!!))
                    }

                    is Resource.Loading -> {
                        _homeMostViewedPropertiesState.emit(UiState.Loading())
                    }
                }

            }


        }
    }

    fun getHomeNews() {
        getHomeNewsJob?.cancel()
        getHomeNewsJob = viewModelScope.launch(Dispatchers.IO) {

            homeRepository.getHomeNews().collect() { result ->
                when (result) {
                    is Resource.Success -> {
                        val list = mutableListOf<HomeNewsSectionsModel>()
                        for (item in result.data?.data!!) {
                            list.add(item.toHomeNewsSectionModel())
                        }
                        _homeNewsState.emit(UiState.Success(list))
                    }

                    is Resource.Error -> {
                        _homeNewsState.emit(UiState.Error(result.message!!))
                    }

                    is Resource.Loading -> {
                        _homeNewsState.emit(UiState.Loading())
                    }
                }


            }
        }
    }

    private fun getHomeExtraSections(countryId: String) {
        getHomeExtraSectionsJob?.cancel()
        getHomeExtraSectionsJob = viewModelScope.launch(Dispatchers.IO) {

            homeRepository.getHomeExtraSections(countryId).collect() { result ->
                when (result) {
                    is Resource.Success -> {
                        val list = mutableListOf<HomeExtraSectionsModel>()
                        for (item in result.data?.data!!) {
                            list.add(item.toHomeExtraSectionsModel())
                        }
                        _homeExtraSectionsState.emit(UiState.Success(list))
                    }

                    is Resource.Error -> {
                        _homeExtraSectionsState.emit(UiState.Error(result.message!!))
                    }

                    is Resource.Loading -> {
                        _homeExtraSectionsState.emit(UiState.Loading())
                    }
                }
            }
        }
    }

    fun getHomeApis(countryId: String, cityId: String) {
        initScreenJob?.cancel()
        initScreenJob = viewModelScope.launch(Dispatchers.IO) {

            _initScreenState.value = UiState.Loading()
            getHomeFeaturedProperties(cityId)
            getHomeProjects(cityId)
            getHomeFilterByCity(countryId)
            getHomeSlider()
            getMostViewedProperties(cityId)
            getHomeSlider()
            getHomeNews()
            getHomeExtraSections(cityId)
            getHomeNormalProperties(cityId)
            joinAll(
                getHomeFeaturedPropertiesJob!!,
                getHomeProjectsJob!!,
                getHomeFilterByCityJob!!,
                getHomeSliderJob!!,
                getHomeMostViewedPropertiesJob!!,
                getHomeNewsJob!!,
                getHomeExtraSectionsJob!!,
                getHomeNormalPropertiesJob!!
            )
            _initScreenState.value = UiState.Success(null)

        }
    }

    //---------------------------------------------------------------------------------//
//    fun getProfile(type:String,id:String) {
//        getProfileJob?.cancel()
//        getProfileJob = viewModelScope.launch {
//            withContext(coroutineContext) {
//                getProfileUseCase(type, id).collect { result ->
//                    when (result) {
//                        is Resource.Success -> {
////                            saveUserInfo(result.data?.toUserModel()!!)
//                            _getProfileState.value = UiState.Success(result.data.toUserModel())
//                        }
//
//                        is Resource.Error -> {
//                            _getProfileState.value =
//                                UiState.Error(result.message!!)
//                            UserUtil.clearUserInfo()
//                        }
//
//                        is Resource.Loading -> {
//                            _getProfileState.value = UiState.Loading()
//                        }
//                    }
//                }
//            }
//        }
//    }
    private fun saveUserInfo(user: UserModel) {
        UserUtil.saveUserInfo(
            isRemember = true,
            userToken = UserUtil.getUserToken(),
            userID = user.id.toString(),
            firstName = user.firstName,
            lastName = user.lastName,
            phone = user.phone,
            email = user.email,
            countryId = user.country.toString(),
            cityName = user.cityName,
            cityId = user.city.toString(),
            countryName = user.countryName,
            userBio = user.bio,
            profileImageUrl = user.userImage,
            userType = user.actorType,
            hasPackage = user.hasPackage,
            brokerName = user.name ?: "",
            dashboardLink = user.dashboardLink ?: "",
            packageId = user.packageInfo?.packageId.toString()

        )
    }

    fun addOrRemoveFav(propertyId: Int) {
        viewModelScope.launch {
            withContext(coroutineContext) {
                addOrRemoveFavRepository.addOrRemoveFav(propertyId).collect() { result ->
                    when (result) {
                        is Resource.Success -> {
                            _favState.value = UiState.Success(result.data)
                        }

                        is Resource.Error -> {
                            _favState.value = UiState.Error(result.message!!)
                        }

                        is Resource.Loading -> {
                            _favState.value = UiState.Loading()
                        }
                    }
                }

            }
        }

    }

    fun getAllProperty() {
        viewModelScope.launch {
            try {
                _allProperty.value = allPropertyUseCase()
                Log.e("success", _allProperty.value.toString())

            } catch (e: Exception) {
                Log.e("failed", e.message.toString())
            }
        }
    }

    fun getAllProjects() {
        viewModelScope.launch {
            try {
                _allProject.value = allProjectsUseCase()
                Log.e("success", _allProject.value.toString())

            } catch (e: Exception) {
                Log.e("failed", e.message.toString())
            }
        }
    }
//
//    fun getAllNormalProjects(){
//        viewModelScope.launch {
//            try {
//                _allNormalProperty.value = allNormalPropertyUseCase()
//                Log.e("success", _allNormalProperty.value.toString())
//
//            } catch (e: Exception) {
//                Log.e("failed", e.message.toString())
//            }
//        }
//    }

    fun getAllCities() {
        viewModelScope.launch {
            try {
                _allCities.value = allCitiesUseCase()
                Log.e("success", _allNormalProperty.value.toString())

            } catch (e: Exception) {
                Log.e("failed", e.message.toString())
            }
        }
    }
}