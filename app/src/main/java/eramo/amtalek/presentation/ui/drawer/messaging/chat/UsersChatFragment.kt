package eramo.amtalek.presentation.ui.drawer.messaging.chat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.data.remote.dto.contactedAgent.DataX
import eramo.amtalek.data.remote.dto.contactedAgent.message.AgentData
import eramo.amtalek.data.remote.dto.contactedAgent.message.Message
import eramo.amtalek.databinding.FragmentUsersChatBinding
import eramo.amtalek.presentation.adapters.recyclerview.RvUsersChatAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.drawer.messaging.MessagingViewModel
import eramo.amtalek.presentation.ui.main.home.HomeMyViewModel
import eramo.amtalek.presentation.ui.main.home.details.projects.ProjectDetailsViewModel
import eramo.amtalek.presentation.viewmodel.navbottom.extension.PropertyDetailsViewModel
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

//        binding.btnSendMessage.setOnClickListener{
//            val message =  binding.etWriteMessage.text.toString()
//            propViewModel.sendToBrokerInChat( message =message,
//                vendorId = UserUtil.getUserId(),
//                name = null,
//                phone = null,
//                email =  null,
//                )
//        }
        Log.e("id for agent", agentId)

        viewModel.getContactedAgentsMessage(agentId)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.contactedAgentsMessageResult.collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        resource.data?.data?.let { dataX ->
                            dataX.agentData?.let { setupViews(it) }
                            viewModel.sentToBrokerState.value.data?.data?.let { listeners(it) }

                        }
                    }

                    is Resource.Error -> {
                        // Handle error
                        Log.e("Error", (resource.message ?: "Unknown error").toString())
                    }

                    is Resource.Loading -> {
                        // Show loading state if needed
                    }
                }
            }
        }


//
//        lifecycleScope.launch {
//            Log.e("message agents ", viewModel.contactedAgentsMessageResult.toString())
//            viewModel.contactedAgentsMessageResult.collect() {
//                it.data?.data?.let { it1 -> it1.agentData?.let { it2 -> setupViews(it2) } }
//            }
//        }
        viewModel.sentToBrokerState.value.data?.data?.let { listeners(it) }



    }

    private fun setupViews(model: AgentData) {
        binding.toolbar.tvTitle.text = model.name

        // setupToolbar(model.agentData?.name?:"")

        model.messages?.toMutableList()?.let { initRvChat(it) }
        assignFakeData()
        model.messages?.toList()?.toMutableList()?.let {
            rvUsersChatAdapter.submitList(it)
        }

    }

    fun listeners(model: DataX) {
        binding.apply {
            btnSendMessage.setOnClickListener {
                val messageText = etWriteMessage.text.toString().trim()
                if (messageText.isNotEmpty()) {
                    rvUsersChatAdapter.sendMessage(messageText, model.id ?: 0, null.toString())
                    val message = Message(
                        id = model.id,
                        message = messageText,
                        link = null,
                        messageTime = SimpleDateFormat("yyyy_mm_dd", Locale.getDefault()).format(Date()),
                        messageType = "sender"
                    )
                    Log.e("failed to send ", message.toString())
                    val updatedList = rvUsersChatAdapter.currentList.toMutableList().apply { add(message) }
                    rvUsersChatAdapter.submitList(updatedList)
                    viewModel.sentToBrokerState.value.data?.data?.actorType?.let { it1 ->
                        viewModel.sendToBrokerInChat(
                            vendorId = viewModel.sentToBrokerState.value.data?.data?.vendorId,
                            name = viewModel.sentToBrokerState.value.data?.data?.name,
                            email = viewModel.sentToBrokerState.value.data?.data?.email,
                            phone = viewModel.sentToBrokerState.value.data?.data?.phone,
                            message = messageText,
                            vendorType = it1
                        )
                    }
//                    val currentList = rvUsersChatAdapter.currentList.toMutableList()
//                    currentList.add(message)
//                    rvUsersChatAdapter.submitList(currentList)
                    binding.etWriteMessage.text?.clear()
                }
                etWriteMessage.text?.clear() // Clear the message input field after sending
                binding.rv.smoothScrollToPosition(rvUsersChatAdapter.itemCount - 1)
                rvUsersChatAdapter.notifyDataSetChanged()

            }

        }
    }


    private fun setupToolbar(agentName: String) {
        binding.toolbar.apply {
            ivBack.setOnClickListener { findNavController().popBackStack() }
            tvTitle.text = agentName
        }
    }

    private fun assignFakeData() {
        binding.apply {
//            tvUserName.text = "Erlan Sadewa"
//            tvUserId.text = "erlan.sadewa"
//            Glide.with(requireContext())
//                .load("https://preview.keenthemes.com/metronic-v4/theme/assets/pages/media/profile/profile_user.jpg")
//                .into(ivUserImage)


        }
    }

    private fun initRvChat(list: MutableList<Message>) {
        binding.rv.adapter = rvUsersChatAdapter
        rvUsersChatAdapter.submitList(
            list

        )
    }

}