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
import eramo.amtalek.data.remote.dto.contactedAgent.message.DataX
import eramo.amtalek.data.remote.dto.contactedAgent.message.Message
import eramo.amtalek.databinding.FragmentUsersChatBinding
import eramo.amtalek.presentation.adapters.recyclerview.RvUsersChatAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.drawer.messaging.MessagingViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UsersChatFragment : BindingFragment<FragmentUsersChatBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentUsersChatBinding::inflate

    @Inject
    lateinit var rvUsersChatAdapter: RvUsersChatAdapter
    val viewModel: MessagingViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        viewModel.getContactedAgentsMessage(viewModel.contactedAgentsMessageResult.value.data?.data?.agentData?.id.toString())
        lifecycleScope.launch {
            Log.e("message agents ", viewModel.contactedAgentsMessageResult.toString())
            viewModel.contactedAgentsMessageResult.collect() {
                it.data?.data?.let { it1 -> setupViews(it1) }
            }
        }
        viewModel.contactedAgentsMessageResult.value.data?.data?.agentData?.messages?.get(0)
            ?.let { listeners(it) }


    }

    override fun onPause() {
        super.onPause()
    }

    private fun setupViews(model: DataX) {

        binding.toolbar.tvTitle.text = model.agentData?.name

        model.agentData?.name?.let { setupToolbar(it) }

        model.agentData?.messages?.toMutableList()?.let { initRvChat(it) }
        assignFakeData()
        model.agentData?.messages?.toList()?.toMutableList()?.let {
            rvUsersChatAdapter.submitList(it)
        }

    }

    private fun listeners(model: Message) {
        binding.apply {
//            toolbar.iconProfile.setOnClickListener {
//                findNavController().navigate(R.id.userProfileFragment, null, navOptionsAnimation())
//            }

            btnSendMessage.setOnClickListener {
                model.id?.let { messageId ->
                    val messageText = etWriteMessage.text.toString().trim()
                    rvUsersChatAdapter.sendMessage(
                        message = messageText,
                        messageId = messageId,
                        link = model.link.toString()
                    )
                    etWriteMessage.text?.clear() // Clear the message input field after sending
                    rv.smoothScrollToPosition(rvUsersChatAdapter.currentList.size)
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