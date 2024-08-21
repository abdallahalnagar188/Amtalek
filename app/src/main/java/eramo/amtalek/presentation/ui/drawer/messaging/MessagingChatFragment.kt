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
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.showToast
import eramo.amtalek.util.state.Resource
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
                when (it) {
                    is Resource.Success -> {
                        rvMessagingChatAdapter.submitList(it.data?.data)
                        LoadingDialog.dismissDialog()
                    }

                    is Resource.Error -> {
                        showToast(it.message.toString())
                        LoadingDialog.dismissDialog()
                    }

                    is Resource.Loading -> {
                        LoadingDialog.showDialog()
                    }
                }

            }
        }

    }

    private fun setupViews() {

        binding.addAdoms.visibility = View.GONE
        binding.inToolbar.tvTitle.text = getString(R.string.messagings)
        binding.inToolbar.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.addAdoms.setOnClickListener {
            if (UserUtil.getHasPackage() == "yes") {
                findNavController().navigate(R.id.addAdomsFragment)
            } else {
                showToast(getString(R.string.no_package))
            }
        }


    }

    private fun fetchContactedAgents() {
        lifecycleScope.launch {
            viewModel.contactedAgents.collectLatest {
                when (it) {
                    is Resource.Success -> {
                        rvMessagingChatAdapter.submitList(it.data?.data)
                        LoadingDialog.dismissDialog()
                    }

                    is Resource.Error -> {
                       // showToast(it.message.toString())
                        LoadingDialog.dismissDialog()
                    }

                    is Resource.Loading -> {
                        LoadingDialog.showDialog()
                    }
                }
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
