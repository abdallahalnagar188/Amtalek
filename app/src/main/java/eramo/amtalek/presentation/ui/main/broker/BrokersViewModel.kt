package eramo.amtalek.presentation.ui.main.broker

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.broker.entity.BrokersResponse
import eramo.amtalek.data.remote.dto.brokersDetails.BrokersDetailsResponse
import eramo.amtalek.data.remote.dto.brokersProperties.BrokersPropertyResponse
import eramo.amtalek.data.remote.dto.userDetials.UserDetailsResponse
import eramo.amtalek.domain.model.auth.UserModel
import eramo.amtalek.domain.usecase.broker.GetBrokers
import eramo.amtalek.domain.usecase.broker.GetBrokersDetails
import eramo.amtalek.domain.usecase.broker.GetBrokersProperties
import eramo.amtalek.domain.usecase.user.GetUserDetails
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BrokersViewModel @Inject constructor(
    private val getBrokersUseCase: GetBrokers,
    private val getBrokersDetailsUseCase: GetBrokersDetails,
    private val getBrokersPropertiesUseCase: GetBrokersProperties,
    private val getUserDetailsUseCase: GetUserDetails,
) : ViewModel() {
    private val _brokers: MutableStateFlow<BrokersResponse?> = MutableStateFlow(null)
    val brokers: StateFlow<BrokersResponse?> get() = _brokers

    private val _brokersDetails: MutableStateFlow<BrokersDetailsResponse?> = MutableStateFlow(null)
    val brokersDetails: StateFlow<BrokersDetailsResponse?> get() = _brokersDetails


    private val _userDetails: MutableStateFlow<UserDetailsResponse?> = MutableStateFlow(null)
    val userDetails: StateFlow<UserDetailsResponse?> get() = _userDetails

    private val _brokersProperties: MutableStateFlow<BrokersPropertyResponse?> = MutableStateFlow(null)
    val brokersProperties: StateFlow<BrokersPropertyResponse?> get() = _brokersProperties



    fun getBrokers() {
        viewModelScope.launch {
            try {

                _brokers.value = getBrokersUseCase()
                Log.e("success", _brokers.value.toString())

            } catch (e: Exception) {
                Log.e("failed", e.message.toString())
            }
        }
    }

    fun getBrokersDetails(id: Int){
        viewModelScope.launch {
            try {
                _brokersDetails.value = getBrokersDetailsUseCase(id)
                Log.e("success", _brokersDetails.value.toString())

            } catch (e: Exception) {
                Log.e("failed", e.message.toString())
            }
        }
    }

    fun getBrokersProperties(id:Int){
        viewModelScope.launch {
            try {
                _brokersProperties.value = getBrokersPropertiesUseCase(id)
            } catch (e: Exception) {
                Log.e("failed", e.message.toString())
            }
        }
    }

    fun getUserDetails(id:Int){
        viewModelScope.launch {
            try {
                _userDetails.value = getUserDetailsUseCase(id)
            } catch (e: Exception) {
                Log.e("failed", e.message.toString())
            }
        }
    }
}