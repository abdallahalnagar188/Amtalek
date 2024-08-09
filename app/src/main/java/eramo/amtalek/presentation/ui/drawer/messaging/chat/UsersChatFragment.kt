package eramo.amtalek.presentation.ui.drawer.messaging.chat

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.data.remote.dto.contactedAgent.message.Message
import eramo.amtalek.databinding.FragmentUsersChatBinding
import eramo.amtalek.presentation.adapters.recyclerview.RvUsersChatAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.drawer.messaging.MessagingViewModel
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class UsersChatFragment : BindingFragment<FragmentUsersChatBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentUsersChatBinding::inflate

    private lateinit var agentId: String
    private lateinit var brokerType: String
    private lateinit var agentName: String
    private lateinit var agentImage:String

    @Inject
    lateinit var rvUsersChatAdapter: RvUsersChatAdapter


    val viewModel: MessagingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        agentId = arguments.let {
            UsersChatFragmentArgs.fromBundle(it!!).agentId
        }
        brokerType = arguments.let {
            UsersChatFragmentArgs.fromBundle(it!!).brokerType
        }
        agentName = arguments.let {
            UsersChatFragmentArgs.fromBundle(it!!).agentName
        }
        agentImage = arguments.let {
            UsersChatFragmentArgs.fromBundle(it!!).agentImage
        }.toString()

        binding.tvTitle.text = agentName
        Glide.with(requireContext())
            .load(agentImage)
            .placeholder(R.drawable.ic_profile)
            .into(binding.ivProfile)
        rvUsersChatAdapter.setAgentImageUrl(agentImage)
        rvUsersChatAdapter.setAgentName(agentName)
        binding.btnRefrsh.setOnClickListener {
            fetchMessages()
        }
        fetchMessages()
        listeners()
        //observeSendMessageState()
    }


    @SuppressLint("NotifyDataSetChanged")
    fun listeners() {
        binding.apply {
            btnSendMessage.setOnClickListener {
                val timeFormatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
                val messageTime = timeFormatter.format(Date())
                val messageText = etWriteMessage.text.toString().trim()
                if (messageText.isNotEmpty()) {
                    val messageId = viewModel.messages.value?.get(0)?.id ?: 0
                    val newMessage = Message(
                        id = messageId,
                        message = messageText,
                        link = "",
                        messageTime = messageTime,
                        messageType = "sender"
                    )

                    viewModel.sendMessageToBrokerInChat(
                        vendorId = agentId,
                        name = UserUtil.getUserFirstName(),
                        email = UserUtil.getUserEmail(),
                        phone = UserUtil.getUserPhone(),
                        message=  messageText,
                        vendorType = brokerType,
                    )
                   // Log.e("data ", UserUtil.getUserType())
                    Log.e("Sending message", newMessage.toString())
                    val updatedList = rvUsersChatAdapter.currentList.toMutableList().apply { add(newMessage) }
                    rvUsersChatAdapter.submitList(updatedList)
                    rvUsersChatAdapter.notifyDataSetChanged()
                    binding.rv.smoothScrollToPosition(rvUsersChatAdapter.itemCount - 1)
                    etWriteMessage.text?.clear()
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchMessages() {
        viewModel.getContactedAgentsMessage(agentId)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.contactedAgentsMessageResult.collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        resource.data?.data?.let { data ->
                            data.agentData?.messages?.let {
                                rvUsersChatAdapter.submitList(it)
                                rvUsersChatAdapter.notifyDataSetChanged()
                                initRvChat(it.toMutableList())
                                binding.rv.smoothScrollToPosition(rvUsersChatAdapter.itemCount - 1)
                            }
                        }
                    }

                    is Resource.Error -> {
                        Log.e("Error", (resource.message ?: "Unknown error").toString())
                    }

                    is Resource.Loading -> {
                        Log.e("Loading", "Loading")
                    }

                }
            }
        }
    }



//    private fun observeSendMessageState() {
//        viewLifecycleOwner.lifecycleScope.launch {
//            viewModel.sentToBrokerState.collect { state ->
//                when (state) {
//                    is UiState.Success -> {
//                        state.data?.data?.let { setupViews(it) }
//                        Log.d("SendMessage", "Message sent successfully")
//                    }
//                    is UiState.Error -> {
//                        Log.e("SendMessage", "Error sending message: ${state.message}")
//                    }
//                    is UiState.Loading -> {
//                        Log.d("SendMessage", "Sending message...")
//                    }
//
//                    is UiState.Empty -> {
//                        Log.d("SendMessage", "Sending message...")
//                    }
//                }
//            }
//        }
//    }


    private fun initRvChat(list: MutableList<Message>) {
        binding.rv.adapter = rvUsersChatAdapter
        rvUsersChatAdapter.submitList(list)
    }
}
