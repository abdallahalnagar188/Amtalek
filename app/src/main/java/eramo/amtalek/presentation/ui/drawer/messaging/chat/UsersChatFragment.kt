package eramo.amtalek.presentation.ui.drawer.messaging.chat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.data.remote.dto.contactedAgent.message.AgentData
import eramo.amtalek.data.remote.dto.contactedAgent.message.ContactAgentsMessageResponse
import eramo.amtalek.data.remote.dto.contactedAgent.message.Message
import eramo.amtalek.databinding.FragmentUsersChatBinding
import eramo.amtalek.presentation.adapters.recyclerview.RvUsersChatAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.drawer.messaging.MessagingViewModel
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UsersChatFragment : BindingFragment<FragmentUsersChatBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentUsersChatBinding::inflate

    @Inject
    lateinit var rvUsersChatAdapter: RvUsersChatAdapter

    private val viewModel: MessagingViewModel by viewModels()
    private val viewModelShared: SharedViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Handle back navigation
        binding.toolbar.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        // Initialize chat view and fetch messages
        setupChat()
        viewModel.messagesState.value.data?.let { setupViews(it) }
    }

    private fun setupChat() {
        // Assuming `agentId` is passed via arguments or another method
        val agentId = arguments?.getString("id") ?: return

        viewModel.getMessages(agentId)
    }

    private fun setupViews(model: ContactAgentsMessageResponse) {
        setupToolbar(model.data?.agentData?.name.orEmpty())
        model.data?.agentData?.messages?.let { initRvChat(it) }
        model.data?.agentData?.let { listeners(it) }

        viewModelShared.getMessages(model.data?.agentData?.id.toString())
        Log.e("idForAgent", model.data?.agentData?.id.toString())
    }

    private fun listeners(model: AgentData) {
        binding.apply {
            btnSendMessage.setOnClickListener {
                viewModelShared.getMessages(model.id.toString())
                val messageText = etWriteMessage.text.toString().trim()
                val messageId = model.id ?: 0
                val link = model.messages.firstOrNull()?.link.orEmpty()

                rvUsersChatAdapter.sendMessage(message = messageText, messageId = messageId, link = link)
                etWriteMessage.text?.clear() // Clear the message input field after sending
                rv.smoothScrollToPosition(rvUsersChatAdapter.currentList.size)

            }
        }
    }

    private fun setupToolbar(agentName: String) {
        binding.toolbar.tvTitle.text = agentName
    }

    private fun initRvChat(list: List<Message>) {
        binding.rv.adapter = rvUsersChatAdapter

        rvUsersChatAdapter.submitList(list.toMutableList())
    }

    override fun onPause() {
        super.onPause()
        rvUsersChatAdapter.submitList(null)
    }
}
