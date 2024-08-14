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
import eramo.amtalek.domain.model.home.slider.SliderModel
import eramo.amtalek.domain.repository.AddOrRemoveFavRepository
import eramo.amtalek.domain.repository.HotOffersRepository
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.Dispatchers
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

    private val _favState = MutableStateFlow<UiState<AddOrRemoveFavResponse>>(UiState.Empty())
    val favState: StateFlow<UiState<AddOrRemoveFavResponse>> = _favState


    private val _hotOffersSliderState = MutableStateFlow<UiState<List<SliderModel>>>(UiState.Empty())
    val hotOffersSliderState: StateFlow<UiState<List<SliderModel>>> = _hotOffersSliderState

    //////////////////////////////////////////
    private var getHotOffersJob: Job? = null
    //////////////////////////////////////////

    fun getSearchResultSlider(){
        getHotOffersJob?.cancel()
        getHotOffersJob = viewModelScope.launch(Dispatchers.IO) {

            repository.getHotOffersSlider().collect(){result->
                when(result){
                    is Resource.Success ->{
                        val list = mutableListOf<SliderModel>()
                        for (item in result.data?.data!!){
                            if (item != null) {
                                list.add(item.toSliderModel())
                            }
                        }
                        _hotOffersSliderState.emit(UiState.Success(list))
                    }
                    is Resource.Error ->{
                        _hotOffersSliderState.emit(UiState.Error(result.message!!))
                    }
                    is Resource.Loading->{
                        _hotOffersSliderState.emit(UiState.Loading())
                    }
                }
            }
        }
    }


    fun getHotOffers(countryId:String){
        getHotOffersJob?.cancel()
        getHotOffersJob = viewModelScope.launch {
            withContext(coroutineContext){
                repository.getHotOffers(countryId).collect(){
                    when (it){
                        is Resource.Success -> {

                            _hotOffers.value = UiState.Success(it.data)

                            val data = it.data
                            filterProperties(data)


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