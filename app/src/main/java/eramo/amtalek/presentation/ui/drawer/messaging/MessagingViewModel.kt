package eramo.amtalek.presentation.ui.drawer.messaging

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eramo.amtalek.data.remote.dto.contactBrokerDetails.ContactUsResponseInProperty
import eramo.amtalek.data.remote.dto.contactedAgent.ContactedAgentResponse
import eramo.amtalek.data.remote.dto.contactedAgent.message.ContactAgentsMessageResponse
import eramo.amtalek.domain.repository.ContactedAgentsMessageRepo
import eramo.amtalek.domain.usecase.contactedAgents.GetContactedAgents
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessagingViewModel @Inject constructor(
    private val getContactedAgentsUseCase: GetContactedAgents,
 //   private val contactedAgentsMessage: GetMessagesContactedAgents
    private val contactedAgentsMessage: ContactedAgentsMessageRepo

) : ViewModel() {

    private val _contactedAgents: MutableStateFlow<ContactedAgentResponse?> = MutableStateFlow(null)
    val contactedAgents: StateFlow<ContactedAgentResponse?> get() = _contactedAgents

//    private val _contactedAgentsMessageResult= MutableStateFlow<ContactAgentsMessageResponse?>(null)
//    val contactedAgentsMessageResult: StateFlow<ContactAgentsMessageResponse?> get() = _contactedAgentsMessageResult

    private val _contactedAgentsMessageResult = MutableStateFlow<Resource<ContactAgentsMessageResponse>>(Resource.Loading())
    val contactedAgentsMessageResult: MutableStateFlow<Resource<ContactAgentsMessageResponse>> = _contactedAgentsMessageResult

    fun getContactedAgents() {

        viewModelScope.launch {
            try {
                _contactedAgents.value = getContactedAgentsUseCase()
                Log.e("success", _contactedAgents.value.toString())

            } catch (e: Exception) {
                Log.e("failed", e.message.toString())
            }
        }

    }

//    fun getContactedAgentsMessage(agentId: String) {
//        viewModelScope.launch {
//            try {
//                Log.e("success", contactedAgentsMessage(agentId).toString())
//                _contactedAgentsMessageResult.value = contactedAgentsMessage(agentId)
//
//            } catch (e: Exception) {
//                Log.e("failed to get message", e.message.toString())
//            }
//            }
//    }

    fun getContactedAgentsMessage(agentId: String) {
        viewModelScope.launch {
            contactedAgentsMessage.getContactedAgentsMessage(agentId).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        Log.e("getMessageAgens success", "sendContactRequest: ${result.data}")
                        _contactedAgentsMessageResult.value = Resource.Success(result.data)

                    }
                    is Resource.Error -> {
                        Log.e("getMessageAgens fails", "sendContactRequest: ${result.data}")
                        _contactedAgentsMessageResult.value = Resource.Error(result.message!!)
                    }
                    is Resource.Loading -> {
                        _contactedAgentsMessageResult.value = Resource.Loading()
                    }
                }
            }
        }
    }


}