package eramo.amtalek.presentation.ui.drawer.messaging

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.data.remote.dto.adons.AddonsResponse
import eramo.amtalek.data.remote.dto.contactedAgent.ContactedAgentResponse
import eramo.amtalek.data.remote.dto.contactedAgent.SentToBrokerMessageResponse
import eramo.amtalek.data.remote.dto.contactedAgent.message.ContactAgentsMessageResponse
import eramo.amtalek.data.remote.dto.contactedAgent.message.Message
import eramo.amtalek.domain.repository.AddonsRepo
import eramo.amtalek.domain.repository.ContactedAgentRepo
import eramo.amtalek.domain.repository.ContactedAgentsMessageRepo
import eramo.amtalek.domain.repository.SentToBrokerMessageRepo
import eramo.amtalek.domain.usecase.contactedAgents.GetContactedAgents
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MessagingViewModel @Inject constructor(
    private val getContactedAgentsUseCase: ContactedAgentRepo,
    private val contactedAgentsMessage: ContactedAgentsMessageRepo,
    private val sendToBrokerRepository: SentToBrokerMessageRepo,
    private val getAddonsUseCase: AddonsRepo
) : ViewModel() {

    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> get() = _messages

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _contactedAgents= MutableStateFlow<Resource<ContactedAgentResponse?>>(Resource.Loading())
    val contactedAgents: MutableStateFlow<Resource<ContactedAgentResponse?>> get() = _contactedAgents


    private val _contactedAgentsMessageResult = MutableStateFlow<Resource<ContactAgentsMessageResponse>>(Resource.Loading())
    val contactedAgentsMessageResult: MutableStateFlow<Resource<ContactAgentsMessageResponse>> = _contactedAgentsMessageResult


    private val _addons = MutableStateFlow<Resource<AddonsResponse>>(Resource.Loading())
    val addons: MutableStateFlow<Resource<AddonsResponse>> = _addons

    private var sendMessageToPropertyOwnerJob: Job? = null
    private var getAddonsJob: Job? = null

    fun getContactedAgents() {
        viewModelScope.launch {
            getContactedAgentsUseCase.getContactedAgents().collect(){
                when(it){
                    is Resource.Success -> {
                        _contactedAgents.value = Resource.Success(it.data)
                    }
                    is Resource.Error -> {
                        _contactedAgents.value = Resource.Error(it.message!!)
                        Log.e("getContactedAgents", "getContactedAgents: ${it.message}")
                    }
                    is Resource.Loading -> {
                        _contactedAgents.value = Resource.Loading()
                    }
                }
            }

        }
    }

    fun getContactedAgentsMessage(agentId: String) {
        viewModelScope.launch {
            contactedAgentsMessage.getContactedAgentsMessage(agentId).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        Log.e("getMessageAgents success", "sendContactRequest: ${result.data}")
                        _contactedAgentsMessageResult.value = Resource.Success(result.data)
                        _messages.value = result.data?.data?.agentData?.messages ?: emptyList()
                    }

                    is Resource.Error -> {
                        Log.e("getMessageAgents fails", "sendContactRequest: ${result.message}")
                        _contactedAgentsMessageResult.value = Resource.Error(result.message!!)
                    }

                    is Resource.Loading -> {
                        LoadingDialog.showDialog()
                        _contactedAgentsMessageResult.value = Resource.Loading()
                    }
                }
            }
        }
    }


    private var _sentToBrokerState = MutableStateFlow<UiState<SentToBrokerMessageResponse>>(UiState.Empty())
    val sentToBrokerState: StateFlow<UiState<SentToBrokerMessageResponse>> = _sentToBrokerState

    fun sendMessageToBrokerInChat(
        vendorId: String?,
        name: String?,
        email: String?,
        phone: String?,
        message: String?,
        vendorType: String
    ) {
        sendMessageToPropertyOwnerJob?.cancel()
        sendMessageToPropertyOwnerJob = viewModelScope.launch {
            withContext(coroutineContext) {
                sendToBrokerRepository.sendToBrokerInChat(
                    vendorId,
                    name,
                    email,
                    phone,
                    message,
                    vendorType
                ).collect {
                    when (it) {
                        is Resource.Success -> {
                            _sentToBrokerState.value = UiState.Success(it.data)

                        }

                        is Resource.Error -> {
                            _sentToBrokerState.value = UiState.Error(it.message!!)
                        }

                        is Resource.Loading -> {
                            _sentToBrokerState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }
}
