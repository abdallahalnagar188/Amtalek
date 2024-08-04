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
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.data.remote.dto.contactedAgent.DataX
import eramo.amtalek.data.remote.dto.contactedAgent.message.Message
import eramo.amtalek.databinding.FragmentUsersChatBinding
import eramo.amtalek.presentation.adapters.recyclerview.RvUsersChatAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.drawer.messaging.MessagingViewModel
import eramo.amtalek.presentation.ui.main.home.HomeMyViewModel
import eramo.amtalek.presentation.ui.main.home.details.projects.ProjectDetailsViewModel
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

    @Inject
    lateinit var rvUsersChatAdapter: RvUsersChatAdapter

    val viewModel: MessagingViewModel by viewModels()
    val homeMyViewModel: HomeMyViewModel by viewModels()
    val propViewModel: ProjectDetailsViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        agentId = arguments.let {
            UsersChatFragmentArgs.fromBundle(it!!).agentId
        }
        fetchMessages()

    }

    private fun setupViews(model: DataX) {
        binding.toolbar.tvTitle.text = model.name

        setupToolbar(model.name ?: "")
    }

    @SuppressLint("NotifyDataSetChanged")
    fun listeners(model: DataX) {
        binding.apply {
            btnSendMessage.setOnClickListener {
                val messageText = etWriteMessage.text.toString().trim()
                if (messageText.isNotEmpty()) {
                    val messageId = model.id ?: 0
                    val message = Message(
                        id = messageId,
                        message = messageText,
                        link = null,
                        messageTime = SimpleDateFormat("yyyy_MM_dd", Locale.getDefault()).format(
                            Date()
                        ),
                        messageType = "sender"
                    )

                    Log.e("Sending message", message.toString())

                    // Update RecyclerView with the new message
                    val updatedList = rvUsersChatAdapter.currentList.toMutableList().apply { add(message) }
                    rvUsersChatAdapter.submitList(updatedList)
                    rvUsersChatAdapter.notifyDataSetChanged()
                    binding.rv.smoothScrollToPosition(rvUsersChatAdapter.itemCount - 1)

                    // Send message to the broker
                        viewModel.sendToBrokerInChat(
                            vendorId = model.vendorId,
                            name = model.name,
                            email = model.email,
                            phone = model.phone,
                            message = messageText,
                            vendorType = model.actorType ?: ""
                        )

                    // Clear the input field
                    etWriteMessage.text?.clear()
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun fetchMessages() {
        viewModel.getContactedAgentsMessage(agentId)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.contactedAgentsMessageResult.collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        resource.data?.data?.let { data ->
                            data.agentData?.messages?.toList()?.toMutableList()?.let {
                                rvUsersChatAdapter.submitList(it)
                                rvUsersChatAdapter.notifyDataSetChanged()
                                initRvChat(it)
                                binding.rv.smoothScrollToPosition(rvUsersChatAdapter.itemCount - 1)
                            }
                        }
                    }

                    is Resource.Error -> {
                        // Handle error
                        Log.e("Error", (resource.message ?: "Unknown error").toString())
                    }

                    is Resource.Loading -> {
                        // Show loading state if needed
                        Log.e("Loading", "Loading")
                    }
                }
            }

        }
    }


    private fun setupToolbar(agentName: String) {
        binding.toolbar.apply {
            ivBack.setOnClickListener { findNavController().popBackStack() }
            tvTitle.text = agentName
        }
    }

    private fun initRvChat(list: MutableList<Message>) {
        binding.rv.adapter = rvUsersChatAdapter
        rvUsersChatAdapter.submitList(
            list

        )
    }

}