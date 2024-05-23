package eramo.amtalek.presentation.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.domain.model.home.HomeExtraSectionsModel
import eramo.amtalek.domain.model.home.cities.HomeCitiesSectionsModel
import eramo.amtalek.domain.model.home.news.HomeNewsSectionsModel
import eramo.amtalek.domain.model.home.project.HomeProjectsSectionModel
import eramo.amtalek.domain.model.home.property.HomePropertySectionModel
import eramo.amtalek.domain.model.home.slider.SliderModel
import eramo.amtalek.domain.repository.MyHomeRepository
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeMyViewModel @Inject constructor(
  val homeRepository:MyHomeRepository
): ViewModel(){
    private val _homeFeaturedPropertiesState = MutableStateFlow<UiState<List<HomePropertySectionModel>>>(UiState.Empty())
    val homeFeaturedPropertiesState:StateFlow<UiState<List<HomePropertySectionModel>>> = _homeFeaturedPropertiesState

    private val _homeProjectsState = MutableStateFlow<UiState<List<HomeProjectsSectionModel>>>(UiState.Empty())
    val homeProjectsState:StateFlow<UiState<List<HomeProjectsSectionModel>>> = _homeProjectsState

    private val _homeFilterByCityState = MutableStateFlow<UiState<List<HomeCitiesSectionsModel>>>(UiState.Empty())
    val homeFilterByCityState:StateFlow<UiState<List<HomeCitiesSectionsModel>>> = _homeFilterByCityState

    private val _homeSliderState = MutableStateFlow<UiState<List<SliderModel>>>(UiState.Empty())
    val homeSliderState:StateFlow<UiState<List<SliderModel>>> = _homeSliderState

    private val _homeMostViewedPropertiesState = MutableStateFlow<UiState<List<HomePropertySectionModel>>>(UiState.Empty())
    val homeMostViewedPropertiesState:StateFlow<UiState<List<HomePropertySectionModel>>> = _homeMostViewedPropertiesState


    private val _homeNewsState = MutableStateFlow<UiState<List<HomeNewsSectionsModel>>>(UiState.Empty())
    val homeNewsState:StateFlow<UiState<List<HomeNewsSectionsModel>>> = _homeNewsState

    private val _homeExtraSectionsState = MutableStateFlow<UiState<List<HomeExtraSectionsModel>>>(UiState.Empty())
    val homeExtraSectionsState:StateFlow<UiState<List<HomeExtraSectionsModel>>> = _homeExtraSectionsState



    //---------------------------------------------------------------------------------//
    private var getHomeFeaturedPropertiesJob:Job?=null
    private var getHomeProjectsJob:Job?=null
    private var getHomeFilterByCityJob:Job?=null
    private var getHomeSliderJob:Job?=null
    private var getHomeMostViewedPropertiesJob:Job?=null
    private var getHomeNewsJob:Job?=null
    private var getHomeExtraSectionsJob:Job?=null

    fun cancelRequests(){
        getHomeFeaturedPropertiesJob?.cancel()
        getHomeProjectsJob?.cancel()
        getHomeFilterByCityJob?.cancel()
        getHomeSliderJob?.cancel()
        getHomeMostViewedPropertiesJob?.cancel()
        getHomeNewsJob?.cancel()
        getHomeExtraSectionsJob?.cancel()
    }
    //---------------------------------------------------------------------------------//
    fun getHomeFeaturedProperties(countryId:String){
        getHomeFeaturedPropertiesJob?.cancel()
        getHomeFeaturedPropertiesJob = viewModelScope.launch {
            withContext(coroutineContext){
                homeRepository.getHomeFeaturedProperty(countryId).collect(){result->
                        when (result){
                            is Resource.Success->{
                                ///its a list cuz the response data is array of a single object** careful with that
                                val list = mutableListOf<HomePropertySectionModel>()
                                for (item in result.data?.data!!){
                                    list.add(item.toHomePropertySectionModel())
                                }
                                _homeFeaturedPropertiesState.emit(UiState.Success(list))
                            }
                            is Resource.Error->{
                                _homeFeaturedPropertiesState.emit(UiState.Error(result.message!!))
                            }
                            is Resource.Loading->{
                                _homeFeaturedPropertiesState.emit(UiState.Loading())
                            }
                        }
                }
            }
        }

    }
    fun getHomeProjects(countryId:String){
        getHomeProjectsJob?.cancel()
        getHomeProjectsJob = viewModelScope.launch {
            withContext(coroutineContext){
                homeRepository.getHomeProjects(countryId).collect(){result->
                    when(result){
                        is Resource.Success ->{
                            val list = mutableListOf<HomeProjectsSectionModel>()
                            for (item in result.data?.data!!){
                                list.add(item.toHomeProjectsSectionModel())
                            }
                            _homeProjectsState.emit(UiState.Success(list))
                        }
                        is Resource.Error ->{
                            _homeProjectsState.emit(UiState.Error(result.message!!))
                        }
                        is Resource.Loading->{
                            _homeProjectsState.emit(UiState.Loading())
                        }
                    }
                }
            }
        }
    }
    fun getHomeFilterByCity(countryId:String){
        getHomeFilterByCityJob?.cancel()
        getHomeFilterByCityJob = viewModelScope.launch {
            withContext(coroutineContext){
                homeRepository.getFilterByCity(countryId = countryId).collect(){result->
                    when(result){
                        is Resource.Success ->{
                            val list = mutableListOf<HomeCitiesSectionsModel>()
                            for (item in result.data?.data!!){
                                list.add(item.toHomeCitiesSectionsModel())
                            }
                            _homeFilterByCityState.emit(UiState.Success(list))
                        }
                        is Resource.Error ->{
                            _homeFilterByCityState.emit(UiState.Error(result.message!!))
                        }
                        is Resource.Loading->{
                            _homeFilterByCityState.emit(UiState.Loading())
                        }
                    }
                }
            }
        }

    }
    fun getHomeSlider(){
        getHomeSliderJob?.cancel()
        getHomeSliderJob = viewModelScope.launch {
            withContext(coroutineContext){
                homeRepository.getHomeSlider().collect(){result->
                    when(result){
                        is Resource.Success ->{
                            val list = mutableListOf<SliderModel>()
                            for (item in result.data?.data!!){
                                if (item != null) {
                                    list.add(item.toSliderModel())
                                }
                            }
                            _homeSliderState.emit(UiState.Success(list))
                        }
                        is Resource.Error ->{
                            _homeSliderState.emit(UiState.Error(result.message!!))
                        }
                        is Resource.Loading->{
                            _homeSliderState.emit(UiState.Loading())
                        }
                    }

                }
            }
        }
    }
    fun getMostViewedProperties(countryId:String){
        getHomeMostViewedPropertiesJob?.cancel()
        getHomeMostViewedPropertiesJob = viewModelScope.launch {
            withContext(coroutineContext) {
                homeRepository.getHomeMostViewedProperties(countryId).collect() { result ->
                    when(result){
                        is Resource.Success ->{
                            val list = mutableListOf<HomePropertySectionModel>()
                            for (item in result.data?.data!!){
                                list.add(item.toHomePropertySectionModel())
                            }
                            _homeMostViewedPropertiesState.emit(UiState.Success(list))
                        }
                        is Resource.Error ->{
                            _homeMostViewedPropertiesState.emit(UiState.Error(result.message!!))
                        }
                        is Resource.Loading->{
                            _homeMostViewedPropertiesState.emit(UiState.Loading())
                        }
                    }
                }
            }


        }
    }
    fun getHomeNews(){
        getHomeNewsJob?.cancel()
        getHomeNewsJob = viewModelScope.launch {
            withContext(coroutineContext){
                homeRepository.getHomeNews().collect(){result->
                    when(result){
                        is Resource.Success ->{
                            val list = mutableListOf<HomeNewsSectionsModel>()
                            for (item in result.data?.data!!){
                                list.add(item.toHomeNewsSectionModel())
                            }
                            _homeNewsState.emit(UiState.Success(list))
                        }
                        is Resource.Error ->{
                            _homeNewsState.emit(UiState.Error(result.message!!))
                        }
                        is Resource.Loading->{
                            _homeNewsState.emit(UiState.Loading())
                        }
                    }

                }
            }
        }
    }
    fun getHomeExtraSections(countryId:String){
        getHomeExtraSectionsJob?.cancel()
        getHomeExtraSectionsJob = viewModelScope.launch {
            withContext(coroutineContext){
                homeRepository.getHomeExtraSections(countryId).collect(){result->
                    when(result){
                        is Resource.Success ->{
                            val list = mutableListOf<HomeExtraSectionsModel>()
                            for (item in result.data?.data!!){
                                list.add(item.toHomeExtraSectionsModel())
                            }
                            _homeExtraSectionsState.emit(UiState.Success(list))
                        }
                        is Resource.Error ->{
                            _homeExtraSectionsState.emit(UiState.Error(result.message!!))
                        }
                        is Resource.Loading->{
                            _homeExtraSectionsState.emit(UiState.Loading())
                        }
                    }

                }
        }
    }
}

}