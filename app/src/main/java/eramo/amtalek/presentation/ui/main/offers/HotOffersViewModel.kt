package eramo.amtalek.presentation.ui.main.offers

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.data.remote.dto.fav.AddOrRemoveFavResponse
import eramo.amtalek.data.remote.dto.hotoffers.HotOffersResponse
import eramo.amtalek.domain.model.drawer.myfavourites.ProjectModel
import eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel
import eramo.amtalek.domain.repository.AddOrRemoveFavRepository
import eramo.amtalek.domain.repository.HotOffersRepository
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HotOffersViewModel @Inject constructor(
    private val repository: HotOffersRepository,
    private val addOrRemoveFavRepository: AddOrRemoveFavRepository

):ViewModel(){
    //////////////////////////////////////////
    private var _hotOffers = MutableStateFlow<UiState<HotOffersResponse>>(UiState.Empty())
    var hotOffers: StateFlow<UiState<HotOffersResponse>> = _hotOffers
    /////////////////////////////////////////
    private var _forSellListState = MutableLiveData<MutableList<PropertyModel>>()
    var forSellListState: LiveData<MutableList<PropertyModel>> = _forSellListState

    private var _forRentListState = MutableLiveData<MutableList<PropertyModel>>()
    var forRentListState: LiveData<MutableList<PropertyModel>> = _forRentListState

    private var _forBothListState = MutableLiveData<MutableList<PropertyModel>>()
    var forBothListState: LiveData<MutableList<PropertyModel>> = _forBothListState

    private var _projectsListState = MutableLiveData<MutableList<ProjectModel>>()
    var projectsListState: LiveData<MutableList<ProjectModel>> = _projectsListState


    private val _favState = MutableStateFlow<UiState<AddOrRemoveFavResponse>>(UiState.Empty())
    val favState: StateFlow<UiState<AddOrRemoveFavResponse>> = _favState

    //////////////////////////////////////////
    private var getHotOffersJob: Job? = null
    //////////////////////////////////////////
    fun getHotOffers(){
        getHotOffersJob?.cancel()
        getHotOffersJob = viewModelScope.launch {
            withContext(coroutineContext){
                repository.getHotOffers().collect(){
                    when (it){
                        is Resource.Success -> {
                            val projectsList:ArrayList<ProjectModel> = ArrayList()
                            val data = it.data
                            filterProperties(data)
                            for (project in it.data?.data?.projects!!){
                                if (project != null) {
                                    projectsList.add(project.toProjectModel())
                                }
                            }
                            _projectsListState.postValue(projectsList)
                            _hotOffers.value = UiState.Success(it.data)
                        }
                        is Resource.Error -> {
                            _hotOffers.value = UiState.Error(it.message!!)
                        }
                        is Resource.Loading ->{
                            _hotOffers.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }

    private fun filterProperties(data: HotOffersResponse?) {
        val forSellPropertyList:ArrayList<PropertyModel> = ArrayList()
        val forRentPropertyList:ArrayList<PropertyModel> = ArrayList()
        val forBothPropertyList:ArrayList<PropertyModel> = ArrayList()
        for (property in data?.data?.properties!!){
            when (property?.forWhat) {
                "for_sale" -> {
                    forSellPropertyList.add(property.toPropertyModel())
                }
                "for_rent" -> {
                    forRentPropertyList.add(property.toPropertyModel())
                }
                "for_both" -> {
                    forBothPropertyList.add(property.toPropertyModel())
                }
            }
        }
        _forSellListState.value =forSellPropertyList
        _forRentListState.value =forRentPropertyList
        _forBothListState.value = forBothPropertyList

    }
    fun addOrRemoveFav(propertyId: Int) {
        viewModelScope.launch {
            withContext(coroutineContext){
                addOrRemoveFavRepository.addOrRemoveFav(propertyId).collect(){result->
                    when(result){
                        is Resource.Success->{
                            _favState.value = UiState.Success(result.data)
                        }
                        is Resource.Error->{
                            _favState.value = UiState.Error(result.message!!)
                        }
                        is Resource.Loading->{
                            _favState.value = UiState.Loading()
                        }
                    }
                }

            }
        }

    }

}