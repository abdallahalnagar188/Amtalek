package eramo.amtalek.presentation.ui.search.searchresult

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.domain.model.home.slider.SliderModel
import eramo.amtalek.domain.repository.search.SearchRepository
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchResultViewModel @Inject constructor(
    private val searchRepository: SearchRepository
) : ViewModel() {
    private val _searchResultSliderState = MutableStateFlow<UiState<List<SliderModel>>>(UiState.Empty())
    val searchResultSliderState: StateFlow<UiState<List<SliderModel>>> = _searchResultSliderState
    private var getHomeSliderJob: Job?=null


    fun getSearchResultSlider(){
        getHomeSliderJob?.cancel()
        getHomeSliderJob = viewModelScope.launch(Dispatchers.IO) {

            searchRepository.getSearchResultSlider().collect(){result->
                when(result){
                    is Resource.Success ->{
                        val list = mutableListOf<SliderModel>()
                        for (item in result.data?.data!!){
                            if (item != null) {
                                list.add(item.toSliderModel())
                            }
                        }
                        _searchResultSliderState.emit(UiState.Success(list))
                    }
                    is Resource.Error ->{
                        _searchResultSliderState.emit(UiState.Error(result.message!!))
                    }
                    is Resource.Loading->{
                        _searchResultSliderState.emit(UiState.Loading())
                    }
                }


            }
        }
    }

}