package eramo.amtalek.presentation.ui.main.broker

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.broker.entity.DataX
import eramo.amtalek.data.remote.dto.broker.entity.BrokersResponse
import eramo.amtalek.data.remote.dto.brokersDetails.BrokersDetailsResponse
import eramo.amtalek.data.remote.dto.brokersProperties.BrokersPropertyResponse
import eramo.amtalek.data.remote.dto.brokersProperties.OriginalItem
import eramo.amtalek.data.remote.dto.contactedAgent.ContactedAgentResponse
import eramo.amtalek.data.remote.dto.userDetials.UserDetailsResponse
import eramo.amtalek.domain.repository.BrokersDetailsRepo
import eramo.amtalek.domain.repository.BrokersPropertiesRepo
import eramo.amtalek.domain.usecase.broker.GetBrokers
import eramo.amtalek.domain.usecase.broker.GetBrokersDetails
import eramo.amtalek.domain.usecase.broker.GetBrokersProperties
import eramo.amtalek.domain.usecase.user.GetUserDetails
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BrokersViewModel @Inject constructor(
    private val getBrokersDetailsUseCase: BrokersDetailsRepo,
    private val getBrokersPropertiesUseCase: BrokersPropertiesRepo,
    private val getUserDetailsUseCase: GetUserDetails,
    private val brokerApi: AmtalekApi // Inject BrokerApi for PagingSource
) : ViewModel() {



    private val _brokersDetails= MutableStateFlow<Resource<BrokersDetailsResponse?>>(Resource.Loading())
    val brokersDetails: MutableStateFlow<Resource<BrokersDetailsResponse?>> get() = _brokersDetails

//    private val _brokersDetails: MutableStateFlow<BrokersDetailsResponse?> = MutableStateFlow(null)
//    val brokersDetails: StateFlow<BrokersDetailsResponse?> get() = _brokersDetails

    private val _userDetails: MutableStateFlow<UserDetailsResponse?> = MutableStateFlow(null)
    val userDetails: StateFlow<UserDetailsResponse?> get() = _userDetails

    private val _brokersProperties= MutableStateFlow<Resource<BrokersPropertyResponse?>>(Resource.Loading())
    val brokersProperties: MutableStateFlow<Resource<BrokersPropertyResponse?>> get() = _brokersProperties

    val brokersPaging: Flow<PagingData<DataX>> = Pager(
        config = PagingConfig(pageSize = 1, enablePlaceholders = false),
        pagingSourceFactory = { BrokersPagingSource(brokerApi) } // Use the PagingSource
    ).flow.cachedIn(viewModelScope)


    fun getAllBrokerPropertyPagingFlow(brokerId: Int): Flow<PagingData<eramo.amtalek.data.remote.dto.brokersProperties.newBrokerProps.DataX>> {
        return Pager(
            config = PagingConfig(
                pageSize = 1,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { getBrokersPropertiesUseCase.getAllBrokersPropertiesFromRemote(brokerId) } // Pass brokerId here
        ).flow
            .cachedIn(viewModelScope)
    }


    fun getBrokersDetails(id: Int) {
        viewModelScope.launch {
           getBrokersDetailsUseCase.getBrokersDetailsFromRemote(id).collect {
               when(it){
                   is Resource.Success -> {
                       _brokersDetails.value = Resource.Success(it.data)
                   }

                   is Resource.Error -> {
                       _brokersDetails.value = Resource.Error(it.message!!)
                   }
                   is Resource.Loading -> {
                       _brokersDetails.value = Resource.Loading()
                   }
               }

           }
        }
    }

//    fun getBrokersProperties(id: Int) {
//        viewModelScope.launch {
//            getBrokersPropertiesUseCase.getBrokersPropertiesFromRemote(id).collect {
//                when(it){
//                    is Resource.Success -> {
//                        _brokersProperties.value =
//                            Resource.Success(it.data)
//                    }
//                    is Resource.Error -> {
//                        _brokersProperties.value = Resource.Error(it.message!!)
//                    }
//                    is Resource.Loading -> {
//                        _brokersProperties.value =
//                            Resource.Loading()
//                    }
//                }
//            }
//        }
//    }

    fun getUserDetails(id: Int) {
        viewModelScope.launch {
            try {
                _userDetails.value = getUserDetailsUseCase(id)
            } catch (e: Exception) {
                Log.e("failed", e.message.toString())
            }
        }
    }
}
