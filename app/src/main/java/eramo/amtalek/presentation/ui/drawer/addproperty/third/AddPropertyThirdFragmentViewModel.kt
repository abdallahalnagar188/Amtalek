package eramo.amtalek.presentation.ui.drawer.addproperty.third

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.domain.model.auth.CityModel
import eramo.amtalek.domain.model.auth.CountryModel
import eramo.amtalek.domain.model.auth.RegionModel
import eramo.amtalek.domain.model.property.CriteriaModel
import eramo.amtalek.domain.repository.certaria.PropertyCategoriesRepository
import eramo.amtalek.domain.repository.certaria.PropertyFinishingRepository
import eramo.amtalek.domain.repository.certaria.PropertyFloorFinishingRepository
import eramo.amtalek.domain.repository.certaria.PropertyPurposeRepository
import eramo.amtalek.domain.repository.certaria.PropertyTypesRepository
import eramo.amtalek.domain.usecase.auth.CountriesAndCitiesUseCase
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.cancel
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddPropertyThirdFragmentViewModel @Inject constructor(
    private val propertyCategoriesRepository: PropertyCategoriesRepository,
    private val propertyFinishingRepository: PropertyFinishingRepository,
    private val propertyPurposeRepository: PropertyPurposeRepository,
    private val propertyFloorFinishingRepository: PropertyFloorFinishingRepository,
    private val propertyTypesRepository: PropertyTypesRepository,
    private val countriesAndCitiesUseCase: CountriesAndCitiesUseCase

):ViewModel() {
    private val _propertyCategoriesState = MutableStateFlow<UiState<List<CriteriaModel>>>(UiState.Empty())
    val propertyCategoriesState:StateFlow<UiState<List<CriteriaModel>>> = _propertyCategoriesState

    private val _propertyFinishingState = MutableStateFlow<UiState<List<CriteriaModel>>>(UiState.Empty())
    val propertyFinishingState:StateFlow<UiState<List<CriteriaModel>>> = _propertyFinishingState
    private val _propertyFloorFinishingState = MutableStateFlow<UiState<List<CriteriaModel>>>(UiState.Empty())
    val propertyFloorFinishingState:StateFlow<UiState<List<CriteriaModel>>> = _propertyFloorFinishingState

    private val _propertyPurposeState = MutableStateFlow<UiState<List<CriteriaModel>>>(UiState.Empty())
    val propertyPurposeState:StateFlow<UiState<List<CriteriaModel>>> = _propertyPurposeState

    private val _propertyTypesState = MutableStateFlow<UiState<List<CriteriaModel>>>(UiState.Empty())
    val propertyTypesState:StateFlow<UiState<List<CriteriaModel>>> = _propertyTypesState

    private val _countriesState = MutableStateFlow<UiState<List<CountryModel>>>(UiState.Empty())
    val countriesState: StateFlow<UiState<List<CountryModel>>> = _countriesState

    private val _citiesState = MutableStateFlow<UiState<List<CityModel>>>(UiState.Empty())
    val citiesState: StateFlow<UiState<List<CityModel>>> = _citiesState
    private val _regionsState = MutableStateFlow<UiState<List<RegionModel>>>(UiState.Empty())
    val regionsState: StateFlow<UiState<List<RegionModel>>> = _regionsState
    private val _subRegionsState = MutableStateFlow<UiState<List<RegionModel>>>(UiState.Empty())
    val subRegionsState: StateFlow<UiState<List<RegionModel>>> = _subRegionsState


    private val _initScreenState = MutableStateFlow<UiState<Boolean>>(UiState.Empty())
    val initScreenState: StateFlow<UiState<Boolean>> = _initScreenState

    private var initScreenJob:Job? = null
    private var propertyCategoriesJob:Job? = null
    private var propertyFinishingJob:Job? = null
    private var propertyFloorFinishingJob:Job? = null
    private var propertyPurposeJob:Job? = null
    private var propertyTypesJob:Job? = null
    private var getCountriesJob: Job? = null
    private var getCitiesJob: Job? = null
    private var getRegionsJob: Job? = null

    private fun getPropertyCategories(){
        propertyCategoriesJob?.cancel()
        propertyCategoriesJob = viewModelScope.launch {
            withContext(coroutineContext){
                propertyCategoriesRepository.getPropertyCategories().collect{result->
                    when(result){
                        is Resource.Success -> {
                            val list:MutableList<CriteriaModel> = ArrayList()
                            for (item in result.data?.data!!){
                                if (item != null) {
                                    list.add(item.toCriteriaModel())
                                }
                            }
                            _propertyCategoriesState.value = UiState.Success(list)
                        }
                        is Resource.Error -> {
                            _propertyCategoriesState.value = UiState.Error(result.message!!)
                        }
                        is Resource.Loading->{
                            _propertyCategoriesState.value = UiState.Loading()
                        }                        }
                }
            }
        }
    }
    private fun getFinishingState(){
        propertyFinishingJob?.cancel()
        propertyFinishingJob = viewModelScope.launch {
            withContext(coroutineContext){
                propertyFinishingRepository.getPropertyFinishing().collect(){result->
                    when(result){
                        is Resource.Success -> {
                            val list:MutableList<CriteriaModel> = ArrayList()
                            for (item in result.data?.data!!){
                                if (item != null) {
                                    list.add(item.toCriteriaModel())
                                }
                            }
                            _propertyFinishingState.value = UiState.Success(list)
                        }
                        is Resource.Error->{
                            _propertyFinishingState.value = UiState.Error(message = result.message!!)
                        }
                        is Resource.Loading->{
                            _propertyFinishingState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }
    private fun getFloorFinishingState(){
        propertyFloorFinishingJob?.cancel()
        propertyFloorFinishingJob = viewModelScope.launch {
            withContext(coroutineContext){
                propertyFloorFinishingRepository.getPropertyFloorFinishing().collect(){result->
                    when(result){
                        is Resource.Success -> {
                            val list:MutableList<CriteriaModel> = ArrayList()
                            for (item in result.data?.data!!){
                                if (item != null) {
                                    list.add(item.toCriteriaModel())
                                }
                            }
                            _propertyFloorFinishingState.value = UiState.Success(list)
                        }
                        is Resource.Error->{
                            _propertyFloorFinishingState.value = UiState.Error(message = result.message!!)
                        }
                        is Resource.Loading->{
                            _propertyFloorFinishingState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }
    private fun getPropertyPurpose(){
        propertyPurposeJob?.cancel()
        propertyPurposeJob = viewModelScope.launch {
            withContext(coroutineContext){
                propertyPurposeRepository.getPropertyPurpose().collect(){result->
                    when(result){
                        is Resource.Success -> {
                            val list:MutableList<CriteriaModel> = ArrayList()
                            for (item in result.data?.data!!){
                                if (item != null) {
                                    list.add(item.toCriteriaModel())
                                }
                            }
                            _propertyPurposeState.value = UiState.Success(list)
                        }
                        is Resource.Error->{
                            _propertyPurposeState.value = UiState.Error(message = result.message!!)
                        }
                        is Resource.Loading->{
                            _propertyPurposeState.value = UiState.Loading()
                        }
                    }
                }
            }
    }
}
    private fun getPropertyTypes(){
        propertyTypesJob?.cancel()
        propertyTypesJob = viewModelScope.launch {
            withContext(coroutineContext){
                propertyTypesRepository.getPropertyType().collect(){result->
                    when(result){
                        is Resource.Success -> {
                            val list:MutableList<CriteriaModel> = ArrayList()
                            for (item in result.data?.data!!){
                                if (item != null) {
                                    list.add(item.toCriteriaModel())
                                }
                            }
                            _propertyTypesState.value = UiState.Success(list)
                        }
                        is Resource.Error->{
                            _propertyTypesState.value = UiState.Error(result.message!!)
                        }
                        is Resource.Loading->{
                            _propertyTypesState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }
    private fun getCountries() {
        getCountriesJob?.cancel()
        getCountriesJob = viewModelScope.launch {
            withContext(coroutineContext) {
                countriesAndCitiesUseCase.getCountries().collect {
                    when (it) {
                        is Resource.Success -> {
                            _countriesState.value = UiState.Success(it.data)
                        }

                        is Resource.Error -> {
                            _countriesState.value = UiState.Error(it.message!!)
                        }

                        is Resource.Loading -> {
                            _countriesState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }
    fun getCities(countryId: String) {
        getCitiesJob?.cancel()
        getCitiesJob = viewModelScope.launch {
            withContext(coroutineContext) {
                countriesAndCitiesUseCase.getCities(countryId).collect {
                    when (it) {
                        is Resource.Success -> {
                            _citiesState.value = UiState.Success(it.data)
                        }

                        is Resource.Error -> {
                            _citiesState.value = UiState.Error(it.message!!)
                        }

                        is Resource.Loading -> {
                            _citiesState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }
    fun getRegions(cityId: String) {
        getRegionsJob?.cancel()
        getRegionsJob = viewModelScope.launch {
            withContext(coroutineContext) {
                countriesAndCitiesUseCase.getRegions(cityId).collect {
                    when (it) {
                        is Resource.Success -> {
                            _regionsState.value = UiState.Success(it.data)
                        }

                        is Resource.Error -> {
                            _regionsState.value = UiState.Error(it.message!!)
                        }

                        is Resource.Loading -> {
                            _regionsState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }
    fun getSubRegions(regionId: String) {
        getRegionsJob?.cancel()
        getRegionsJob = viewModelScope.launch {
            withContext(coroutineContext) {
                countriesAndCitiesUseCase.getSubRegions(regionId).collect {
                    when (it) {
                        is Resource.Success -> {
                            _subRegionsState.value = UiState.Success(it.data)
                        }

                        is Resource.Error -> {
                            _subRegionsState.value = UiState.Error(it.message!!)
                        }

                        is Resource.Loading -> {
                            _subRegionsState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }

    fun getInitApis(){
        initScreenJob?.cancel()
        initScreenJob = viewModelScope.launch {
            withContext(coroutineContext){
                _initScreenState.value = UiState.Loading()
                getPropertyCategories()
                getFinishingState()
                getPropertyPurpose()
                getPropertyTypes()
                getCountries()
                getFloorFinishingState()
                joinAll(propertyPurposeJob!!,propertyCategoriesJob!!,propertyFinishingJob!!,propertyFloorFinishingJob!!,propertyTypesJob!!,getCountriesJob!!)
                _initScreenState.value = UiState.Success(null)
            }
        }
    }
}