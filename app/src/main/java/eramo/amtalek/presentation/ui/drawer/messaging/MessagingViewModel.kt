package eramo.amtalek.presentation.ui.drawer.messaging

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.data.remote.dto.broker.entity.BrokersResponse
import eramo.amtalek.data.remote.dto.contactedAgent.ContactedAgentResponse
import eramo.amtalek.data.remote.dto.contactedAgent.message.ContactAgentsMessageResponse
import eramo.amtalek.domain.repository.ContactedAgentsMessageRepo
import eramo.amtalek.domain.usecase.contactedAgents.GetContactedAgents
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessagingViewModel @Inject constructor(
    private val getContactedAgentsUseCase: GetContactedAgents,
    private val contactedAgentsMessageRepo: ContactedAgentsMessageRepo,
) : ViewModel() {


    private val _messages: MutableStateFlow<ContactAgentsMessageResponse?> = MutableStateFlow(null)
    val messages: StateFlow<ContactAgentsMessageResponse?> get() = _messages

    private val _contactedAgents = MutableStateFlow<ContactedAgentResponse?>(null)
    val contactedAgents: StateFlow<ContactedAgentResponse?> get() = _contactedAgents

    private val _messagesState = MutableStateFlow<Resource<ContactAgentsMessageResponse>>(Resource.Loading())
    val messagesState: StateFlow<Resource<ContactAgentsMessageResponse>> get() = _messagesState

    fun getContactedAgents() {
        viewModelScope.launch {
            try {
                val agents = getContactedAgentsUseCase()
                _contactedAgents.value = agents
                Log.e("success", agents.toString())
            } catch (e: Exception) {
                Log.e("failed", e.message.toString())
            }
        }
    }

    fun getMessages(agentId: String) {
        viewModelScope.launch {
            contactedAgentsMessageRepo.getContactedAgentsMessage(agentId).collect { resource ->
                _messagesState.value = resource
                Log.e("Success",resource.toString())
            }
        }
    }
}
