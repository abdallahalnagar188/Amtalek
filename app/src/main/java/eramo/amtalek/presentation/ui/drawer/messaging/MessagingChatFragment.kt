package eramo.amtalek.presentation.ui.drawer.messaging

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.data.remote.dto.contactedAgent.Data
import eramo.amtalek.databinding.FragmentMessagingChatBinding
import eramo.amtalek.presentation.adapters.recyclerview.messaging.RvMessagingChatAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.showToast
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MessagingChatFragment : BindingFragment<FragmentMessagingChatBinding>(),
    RvMessagingChatAdapter.OnItemClickListener {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentMessagingChatBinding::inflate

    @Inject
    lateinit var rvMessagingChatAdapter: RvMessagingChatAdapter

    val viewModel: MessagingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getContactedAgents()
        setupViews()
        initChatRv()
        fetchContactedAgents()

    }

    override fun onResume() {
        super.onResume()
        viewModel.getContactedAgents()
        lifecycleScope.launch {
            viewModel.contactedAgents.collect {
                rvMessagingChatAdapter.submitList(it?.data)
            }
        }
//        if (UserUtil.getHasPackage() == "yes") {
//            binding.addAdoms.visibility = View.VISIBLE
//        }

    }

    private fun setupViews() {

        binding.addAdoms.visibility = View.GONE
        binding.inToolbar.tvTitle.text = getString(R.string.messagings)
        binding.inToolbar.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.addAdoms.setOnClickListener {
            if (UserUtil.getHasPackage() == "yes"){
                findNavController().navigate(R.id.addAdomsFragment)
            }
            else{
                showToast(getString(R.string.no_package))
            }
        }


    }

    private fun fetchContactedAgents() {
        lifecycleScope.launch {
            viewModel.contactedAgents.collectLatest {
                rvMessagingChatAdapter.submitList(it?.data)
            }
        }
    }

    private fun initChatRv() {
        rvMessagingChatAdapter.setListener(this@MessagingChatFragment)
        binding.rv.adapter = rvMessagingChatAdapter

    }


    override fun onChatClick(model: Data) {
        if (model.messageType == "valid") {
            val action = MessagingChatFragmentDirections.actionToUsersChatFragment(
                model.id.toString(),
                model.actorType.toString(),
                model.name.toString(),
                model.image
            )
            findNavController().navigate(action)
        } else {
            showToast("Agent is not valid")
        }
    }

}
