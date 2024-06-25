package eramo.amtalek.presentation.ui.drawer.addproperty.fifth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.domain.model.project.AmenityModel
import eramo.amtalek.domain.model.property.CriteriaModel
import eramo.amtalek.domain.repository.certaria.PropertyAmenitiesRepository
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddPropertyFifthFragmentViewModel @Inject constructor(
    private val propertyAmenitiesRepository: PropertyAmenitiesRepository
):ViewModel(){
    var _propertyAmenitiesState: MutableStateFlow<UiState<List<AmenityModel>>> =MutableStateFlow(UiState.Empty())
    val propertyAmenitiesState: StateFlow<UiState<List<AmenityModel>>> = _propertyAmenitiesState


    var propertyAmenitiesJob:Job? = null

    fun getPropertyAmenities(){
        propertyAmenitiesJob?.cancel()
        propertyAmenitiesJob = viewModelScope.launch {
            withContext(coroutineContext){
                val response = propertyAmenitiesRepository.getAmenities()
                response.collect(){result->
                    when(result){
                        is Resource.Success->{
                            val list:MutableList<AmenityModel> = ArrayList()
                            for (item in result.data?.data!!){
                                if (item != null) {
                                    list.add(item.toAmenityModel())
                                }
                            }
                            _propertyAmenitiesState.value = UiState.Success(list)
                        }
                        is Resource.Error->{
                            _propertyAmenitiesState.value = UiState.Error(result.message!!)
                        }
                        is Resource.Loading->{
                            _propertyAmenitiesState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }
}