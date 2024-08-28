package eramo.amtalek.presentation.ui.drawer.subscription.packages

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.data.remote.dto.packages.SubscribeToPackageResponse
import eramo.amtalek.domain.model.drawer.PackageModel
import eramo.amtalek.domain.repository.PackagesRepository
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PackagesViewModel @Inject constructor(
    private val packagesRepository: PackagesRepository
) : ViewModel() {
    private val _getUserPackagesState = MutableStateFlow<UiState<List<PackageModel?>>>(UiState.Empty())
    val getUserPackageState: StateFlow<UiState<List<PackageModel?>>> = _getUserPackagesState


    private val _getAgencyPackagesState = MutableStateFlow<UiState<List<PackageModel?>>>(UiState.Empty())
    val getAgencyPackageState: StateFlow<UiState<List<PackageModel?>>> = _getAgencyPackagesState

    private val _subscribeToPackagesState = MutableStateFlow<UiState<SubscribeToPackageResponse>>(UiState.Empty())
    val subscribeToPackagesState: StateFlow<UiState<SubscribeToPackageResponse>> = _subscribeToPackagesState

    private val _initScreenState = MutableStateFlow<UiState<Boolean>>(UiState.Empty())
    val initScreenState: StateFlow<UiState<Boolean>> = _initScreenState

    private var getUserPackagesJob: Job? = null
    private var getAgencyPackagesJob: Job? = null
    private var initScreenJob: Job? = null

    fun getUserPackages() {
        getUserPackagesJob?.cancel()
        getUserPackagesJob = viewModelScope.launch {
            withContext(coroutineContext) {
                packagesRepository.getPackages("user").collect() {
                    when (it) {
                        is Resource.Success -> {
                            val data = it.data?.data
                            _getUserPackagesState.emit(UiState.Success(data?.map { it?.toPackageModel() }))
                        }

                        is Resource.Error -> {
                            val error = it.message
                            _getUserPackagesState.emit(UiState.Error(error!!))

                        }

                        is Resource.Loading -> {
                            _getUserPackagesState.emit(UiState.Loading())
                        }
                    }
                }
            }
        }
    }

    fun getAgencyPackages() {
        getAgencyPackagesJob?.cancel()
        getAgencyPackagesJob = viewModelScope.launch {
            withContext(coroutineContext) {
                packagesRepository.getPackages("broker").collect() {
                    when (it) {
                        is Resource.Success -> {
                            val data = it.data?.data
                            _getAgencyPackagesState.emit(UiState.Success(data?.map { it?.toPackageModel() }))
                        }

                        is Resource.Error -> {
                            val error = it.message
                            _getAgencyPackagesState.emit(UiState.Error(error!!))

                        }

                        is Resource.Loading -> {
                            _getAgencyPackagesState.emit(UiState.Loading())
                        }
                    }
                }
            }
        }
    }

    fun getUserAndAgencyPackages() {
        initScreenJob?.cancel()
        initScreenJob = viewModelScope.launch {
            withContext(coroutineContext) {
                getUserPackages()
                getAgencyPackages()
                joinAll(
                    getUserPackagesJob!!,
                    getAgencyPackagesJob!!
                )
                _initScreenState.value = UiState.Success(null)

            }
        }
    }

    fun subscribeToPackage(actorType: String, duration: String, packageId: String) {
        viewModelScope.launch {
            withContext(coroutineContext) {
                packagesRepository.subscribeToPackage(actorType = actorType, duration = duration, packageId = packageId).collect() {
                    when (it) {
                        is Resource.Success -> {
                            val data = it.data
                            _subscribeToPackagesState.emit(UiState.Success(data))
                            Log.e("subscribeToPackage", data?.message.toString())
                        }

                        is Resource.Error -> {
                            val error = it.message
                            _subscribeToPackagesState.emit(UiState.Error(error!!))

                        }

                        is Resource.Loading -> {
                            _subscribeToPackagesState.emit(UiState.Loading())
                        }
                    }

                }
            }
        }
    }
}