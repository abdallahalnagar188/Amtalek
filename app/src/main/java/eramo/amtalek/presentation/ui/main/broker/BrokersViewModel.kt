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
import eramo.amtalek.data.remote.dto.userDetials.UserDetailsResponse
import eramo.amtalek.domain.usecase.broker.GetBrokers
import eramo.amtalek.domain.usecase.broker.GetBrokersDetails
import eramo.amtalek.domain.usecase.broker.GetBrokersProperties
import eramo.amtalek.domain.usecase.user.GetUserDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BrokersViewModel @Inject constructor(
    private val getBrokersDetailsUseCase: GetBrokersDetails,
    private val getBrokersPropertiesUseCase: GetBrokersProperties,
    private val getUserDetailsUseCase: GetUserDetails,
    private val brokerApi: AmtalekApi // Inject BrokerApi for PagingSource
) : ViewModel() {

    private val _brokersDetails: MutableStateFlow<BrokersDetailsResponse?> = MutableStateFlow(null)
    val brokersDetails: StateFlow<BrokersDetailsResponse?> get() = _brokersDetails

    private val _userDetails: MutableStateFlow<UserDetailsResponse?> = MutableStateFlow(null)
    val userDetails: StateFlow<UserDetailsResponse?> get() = _userDetails

    private val _brokersProperties: MutableStateFlow<BrokersPropertyResponse?> = MutableStateFlow(null)
    val brokersProperties: StateFlow<BrokersPropertyResponse?> get() = _brokersProperties

    val brokersPaging: Flow<PagingData<DataX>> = Pager(
        config = PagingConfig(pageSize = 1, enablePlaceholders = false),
        pagingSourceFactory = { BrokersPagingSource(brokerApi) } // Use the PagingSource
    ).flow.cachedIn(viewModelScope)


    fun getBrokersDetails(id: Int) {
        viewModelScope.launch {
            try {
                _brokersDetails.value = getBrokersDetailsUseCase(id)
                Log.e("success", _brokersDetails.value.toString())
            } catch (e: Exception) {
                Log.e("failed", e.message.toString())
            }
        }
    }

    fun getBrokersProperties(id: Int) {
        viewModelScope.launch {
            try {
                _brokersProperties.value = getBrokersPropertiesUseCase(id)
            } catch (e: Exception) {
                Log.e("failed", e.message.toString())
            }
        }
    }

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
