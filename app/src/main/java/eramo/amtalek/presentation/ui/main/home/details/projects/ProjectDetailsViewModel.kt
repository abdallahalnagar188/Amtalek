package eramo.amtalek.presentation.ui.main.home.details.projects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.data.remote.dto.project.ProjectDetailsResponse
import eramo.amtalek.data.remote.dto.property.SendToBrokerResponse
import eramo.amtalek.domain.repository.ProjectRepository
import eramo.amtalek.domain.repository.SendToBrokerRepository
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectDetailsViewModel @Inject constructor(
    private val projectRepository: ProjectRepository,
    private val sendToBrokerRepository: SendToBrokerRepository
):ViewModel() {


    private var _projectDetailsState = MutableStateFlow<UiState<ProjectDetailsResponse>>(UiState.Empty())
    val projectDetailsState : StateFlow<UiState<ProjectDetailsResponse>> = _projectDetailsState

    private var _sentToBrokerState = MutableStateFlow<UiState<SendToBrokerResponse>>(UiState.Empty())
    val sentToBrokerState : StateFlow<UiState<SendToBrokerResponse>> = _sentToBrokerState

    private var projectDetailsJob: Job? = null
    private var sentToBrokerJob: Job? = null

    fun sendToBroker(
        vendorId: String?,
        name: String?,
        email: String?,
        phone: String?,
        message: String?){
        sentToBrokerJob?.cancel()
        sentToBrokerJob =viewModelScope.launch {
            sendToBrokerRepository.sendToBroker(vendorId,name,email,phone,message).collect(){
                when(it){
                    is Resource.Success -> {
                        _sentToBrokerState.value = UiState.Success(it.data)
                    }
                    is Resource.Error -> {
                        _sentToBrokerState.value = UiState.Error(it.message!!)
                    }
                    is Resource.Loading ->{
                        _sentToBrokerState.value = UiState.Loading()

                    }
                }
            }
        }

    }
    fun getProjectDetails(listingNumber: String){
        projectDetailsJob?.cancel()
        projectDetailsJob = viewModelScope.launch {
            projectRepository.getProjectDetails(listingNumber).collect(){
                when(it){
                    is Resource.Success -> {
                        _projectDetailsState.value = UiState.Success(it.data)
                    }
                    is Resource.Error -> {
                        _projectDetailsState.value = UiState.Error(it.message!!)
                     }
                    is Resource.Loading ->{
                        _projectDetailsState.value = UiState.Loading()

                    }
                }
            }
        }

    }
}