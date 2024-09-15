package eramo.amtalek.presentation.ui.search.searchform

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.data.remote.dto.fav.AddOrRemoveFavResponse
import eramo.amtalek.data.remote.dto.search.searchform.SearchFilterationResponse
import eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel
import eramo.amtalek.domain.model.project.AmenityModel
import eramo.amtalek.domain.model.property.CriteriaModel
import eramo.amtalek.domain.repository.AddOrRemoveFavRepository
import eramo.amtalek.domain.repository.certaria.PropertyAmenitiesRepository
import eramo.amtalek.domain.repository.certaria.PropertyFinishingRepository
import eramo.amtalek.domain.repository.certaria.PropertyPurposeRepository
import eramo.amtalek.domain.repository.certaria.PropertyTypesRepository
import eramo.amtalek.domain.repository.search.AllLocationsRepository
import eramo.amtalek.domain.repository.search.CurrenciesRepository
import eramo.amtalek.domain.repository.search.SearchFilltrationRepo
import eramo.amtalek.domain.repository.search.SearchRepository
import eramo.amtalek.domain.search.LocationModel
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import javax.inject.Inject
import kotlin.math.min

@HiltViewModel
class SearchFormViewModel @Inject constructor(
    private val allLocationsRepository: AllLocationsRepository,
    private val propertyTypesRepository: PropertyTypesRepository,
    private val propertyFinishingRepository: PropertyFinishingRepository,
    private val propertyPurposeRepository: PropertyPurposeRepository,
    private val propertyAmenitiesRepository: PropertyAmenitiesRepository,
    private val currenciesRepository: CurrenciesRepository,
    private val searchRepository: SearchRepository,
    private val addOrRemoveFavRepository: AddOrRemoveFavRepository,
    private val searchFilltrationRepo: SearchFilltrationRepo
) : ViewModel() {

    private var _allLocationsState = MutableStateFlow<UiState<List<LocationModel>>>(UiState.Empty())
    val allLocationState: StateFlow<UiState<List<LocationModel>>> = _allLocationsState

    private val _propertyTypesState = MutableStateFlow<UiState<List<CriteriaModel>>>(UiState.Empty())
    val propertyTypesState: StateFlow<UiState<List<CriteriaModel>>> = _propertyTypesState

    private val _initScreenState = MutableStateFlow<UiState<Boolean>>(UiState.Empty())
    val initScreenState: StateFlow<UiState<Boolean>> = _initScreenState

    private val _propertyFinishingState = MutableStateFlow<UiState<List<CriteriaModel>>>(UiState.Empty())
    val propertyFinishingState: StateFlow<UiState<List<CriteriaModel>>> = _propertyFinishingState


    private val _propertyPurposeState = MutableStateFlow<UiState<List<CriteriaModel>>>(UiState.Empty())
    val propertyPurposeState: StateFlow<UiState<List<CriteriaModel>>> = _propertyPurposeState

    var _propertyAmenitiesState: MutableStateFlow<UiState<List<AmenityModel>>> = MutableStateFlow(UiState.Empty())
    val propertyAmenitiesState: StateFlow<UiState<List<AmenityModel>>> = _propertyAmenitiesState

    private val _currenciesState = MutableStateFlow<UiState<List<CriteriaModel>>>(UiState.Empty())
    val currenciesState: StateFlow<UiState<List<CriteriaModel>>> = _currenciesState

    private val _searchState = MutableStateFlow<PagingData<PropertyModel>?>(null)
    val searchState: MutableStateFlow<PagingData<PropertyModel>?> = _searchState

    private val _favState = MutableStateFlow<UiState<AddOrRemoveFavResponse>>(UiState.Empty())
    val favState: StateFlow<UiState<AddOrRemoveFavResponse>> = _favState


    private val _searchFilltrationState = MutableStateFlow<Resource<SearchFilterationResponse>>(Resource.Loading())
    val searchFilltrationState: StateFlow<Resource<SearchFilterationResponse>> = _searchFilltrationState

    private var searchJob: Job? = null
    private var initScreenJob: Job? = null
    private var allLocationJob: Job? = null
    private var propertyTypesJob: Job? = null
    private var propertyFinishingJob: Job? = null
    private var propertyPurposeJob: Job? = null
    private var propertyAmenitiesJob: Job? = null
    private var currenciesJob: Job? = null


    fun getSearchFilltration(
        propertyType: String,
        purpose: String,
        finishing: String,
        currency: String,
       amenities: List<String> = emptyList(), // List of integers for amenities
        minPrice: String,
        maxPrice: String,
        minArea: String,
        maxArea: String,
        minBedrooms: String,
        minBathrooms: String
    ) {
        viewModelScope.launch {
            searchFilltrationRepo.getSearchFilteration(
                propertyType = propertyType,
                purpose = purpose,
                finishing = finishing,
                currency = currency,
                amenities = amenities,
                minPrice = minPrice,
                maxPrice = maxPrice,
                minArea = minArea,
                maxArea = maxArea,
                minBedrooms = minBedrooms,
                minBathrooms = minBathrooms
            ).collect() {
                when (it) {
                    is Resource.Success -> {
                        _searchFilltrationState.value = Resource.Success(it.data!!)
                    }
                    is Resource.Loading -> {
                        _searchFilltrationState.value = Resource.Loading()
                    }
                    is Resource.Error -> {
                        _searchFilltrationState.value = Resource.Error(it.message!!)
                    }
                    else -> {}
                }

            }
        }
    }

    fun search(
        keyword: String?,
        city: String?,
        country: String?,
        currency: Int?,
        finishing: String?,
        maxArea: String?,
        minArea: String?,
        maxPrice: String?,
        minPrice: String?,
        minBathes: String?,
        minBeds: String?,
        priceArrangeKeys: String?,
        propertyType: String?,
        purpose: String?,
        region: String?,
        subRegion: String?,
        amenitiesListIds: String?,
        priority_keys: String?
    ) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            searchRepository.search(
                keyword = convertToRequestBody(keyword),
                city = convertToRequestBody(city),
                country = convertToRequestBody(country),
                currency = convertToRequestBodyInt(currency),
                finishing = convertToRequestBody(finishing),
                maxArea = convertToRequestBody(maxArea),
                minArea = convertToRequestBody(minArea),
                maxPrice = convertToRequestBody(maxPrice),
                minPrice = convertToRequestBody(minPrice),
                minBathes = convertToRequestBody(minBathes),
                minBeds = convertToRequestBody(minBeds),
                priceArrangeKeys = convertToRequestBody(priceArrangeKeys),
                propertyType = convertToRequestBody(propertyType),
                purpose = convertToRequestBody(purpose),
                region = convertToRequestBody(region),
                subRegion = convertToRequestBody(subRegion),
                amenities = convertToRequestBody(amenitiesListIds),
                priority_keys = convertToRequestBody(priority_keys)


            ).cachedIn(viewModelScope).collect() {
                _searchState.value = it
            }
        }
    }

    fun convertToRequestBody(part: String?): RequestBody? {
        return try {
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), part!!)
        } catch (e: Exception) {
            null
        }

    }

    fun convertToRequestBodyInt(part: Int?): RequestBody? {
        return try {
            if (part.toString() == "null") {
                null
            } else {
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), part.toString())
            }
        } catch (e: Exception) {
            null
        }
    }

    fun getPropertyTypes() {
        propertyTypesJob?.cancel()
        propertyTypesJob = viewModelScope.launch {
            withContext(coroutineContext) {
                propertyTypesRepository.getPropertyType().collect() { result ->
                    when (result) {
                        is Resource.Success -> {
                            val list: MutableList<CriteriaModel> = ArrayList()
                            for (item in result.data?.data!!) {
                                if (item != null) {
                                    list.add(item.toCriteriaModel())
                                }
                            }
                            _propertyTypesState.value = UiState.Success(list)
                        }

                        is Resource.Error -> {
                            _propertyTypesState.value = UiState.Error(result.message!!)
                        }

                        is Resource.Loading -> {
                            _propertyTypesState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }

    fun getAllLocations() {
        allLocationJob?.cancel()
        allLocationJob = viewModelScope.launch {
            allLocationsRepository.getAllLocations().collect {
                when (it) {
                    is Resource.Loading -> {
                        _allLocationsState.value = UiState.Loading()
                    }

                    is Resource.Success -> {
                        _allLocationsState.value = UiState.Success(it.data?.mapLocations())
                    }

                    is Resource.Error -> {
                        _allLocationsState.value = UiState.Error(it.message!!)
                    }
                }
            }
        }

    }

    fun getFinishingTypes() {
        propertyFinishingJob?.cancel()
        propertyFinishingJob = viewModelScope.launch {
            withContext(coroutineContext) {
                propertyFinishingRepository.getPropertyFinishing().collect() { result ->
                    when (result) {
                        is Resource.Success -> {
                            val list: MutableList<CriteriaModel> = ArrayList()
                            for (item in result.data?.data!!) {
                                if (item != null) {
                                    list.add(item.toCriteriaModel())
                                }
                            }
                            _propertyFinishingState.value = UiState.Success(list)
                        }

                        is Resource.Error -> {
                            _propertyFinishingState.value = UiState.Error(message = result.message!!)
                        }

                        is Resource.Loading -> {
                            _propertyFinishingState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }

    fun getPropertyPurpose() {
        propertyPurposeJob?.cancel()
        propertyPurposeJob = viewModelScope.launch {
            withContext(coroutineContext) {
                propertyPurposeRepository.getPropertyPurpose().collect() { result ->
                    when (result) {
                        is Resource.Success -> {
                            val list: MutableList<CriteriaModel> = ArrayList()
                            for (item in result.data?.data!!) {
                                if (item != null) {
                                    list.add(item.toCriteriaModel())
                                }
                            }
                            _propertyPurposeState.value = UiState.Success(list)
                        }

                        is Resource.Error -> {
                            _propertyPurposeState.value = UiState.Error(message = result.message!!)
                        }

                        is Resource.Loading -> {
                            _propertyPurposeState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }

    fun getPropertyAmenities() {
        propertyAmenitiesJob?.cancel()
        propertyAmenitiesJob = viewModelScope.launch {
            withContext(coroutineContext) {
                val response = propertyAmenitiesRepository.getAmenities()
                response.collect() { result ->
                    when (result) {
                        is Resource.Success -> {
                            val list: MutableList<AmenityModel> = ArrayList()
                            for (item in result.data?.data!!) {
                                if (item != null) {
                                    list.add(item.toAmenityModel())
                                }
                            }
                            _propertyAmenitiesState.value = UiState.Success(list)
                        }

                        is Resource.Error -> {
                            _propertyAmenitiesState.value = UiState.Error(result.message!!)
                        }

                        is Resource.Loading -> {
                            _propertyAmenitiesState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }

    fun getCurrencies() {
        currenciesJob?.cancel()
        currenciesJob = viewModelScope.launch {
            withContext(coroutineContext) {
                currenciesRepository.getAllCurrencies().collect() {
                    when (it) {
                        is Resource.Success -> {
                            _currenciesState.value = UiState.Success(it.data?.toCriteriaModels())
                        }

                        is Resource.Error -> {
                            _currenciesState.value = UiState.Error(it.message!!)
                        }

                        is Resource.Loading -> {
                            _currenciesState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }

    fun getInitApis() {
        initScreenJob?.cancel()
        initScreenJob = viewModelScope.launch {
            withContext(coroutineContext) {
                _initScreenState.value = UiState.Loading()
                getPropertyTypes()
                getFinishingTypes()
                getPropertyPurpose()
                getPropertyAmenities()
                getCurrencies()
                joinAll(
                    propertyTypesJob!!,
                    propertyFinishingJob!!, propertyPurposeJob!!,
                    propertyAmenitiesJob!!,
                    currenciesJob!!
                )
                _initScreenState.value = UiState.Success(null)
            }
        }
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

}