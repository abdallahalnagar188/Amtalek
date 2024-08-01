package eramo.amtalek.presentation.ui.drawer.messaging

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.data.remote.dto.contactedAgent.Data
import eramo.amtalek.databinding.FragmentMessagingChatBinding
import eramo.amtalek.domain.model.drawer.MessagingChatModel
import eramo.amtalek.presentation.adapters.recyclerview.messaging.RvMessagingChatAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.drawer.messaging.chat.UsersChatFragmentArgs
import eramo.amtalek.presentation.ui.main.home.details.properties.PropertyDetailsFragmentArgs
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.util.Dummy
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.navOptionsAnimation
import kotlinx.coroutines.flow.collect
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
    val svm: SharedViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {


        viewModel.getContactedAgents()
        lifecycleScope.launch {
            Log.e("contacted agents", viewModel.contactedAgents.toString())
            viewModel.contactedAgents.collect() {
                initChatRv(viewModel.contactedAgents.value?.data ?: emptyList())
            }
        }

    }

    private fun handleContactAction(agentId: String) {
        svm.getMessages(agentId)
    }


    private fun initChatRv(data: List<Data?>) {
        rvMessagingChatAdapter.setListener(this@MessagingChatFragment)
        binding.rv.adapter = rvMessagingChatAdapter
        rvMessagingChatAdapter.submitList(data)
        setupSearchRv(data)
    }

    private fun setupSearchRv(data: List<Data?>) {
        binding.etSearch.addTextChangedListener { text ->
            if (text.toString().isEmpty()) {
                rvMessagingChatAdapter.submitList(data)
            } else {
                val list = data.filter {
                    it?.name?.lowercase()?.contains(text.toString().lowercase()) == true
                }
                rvMessagingChatAdapter.submitList(null)
                rvMessagingChatAdapter.submitList(list)
            }
        }
    }

//    private fun handleContactAction(agentId: String) {
//        viewModel.getContactedAgentsMessage(agentId)
//    }
    override fun onChatClick(model: Data) {
        findNavController().navigate(
            R.id.usersChatFragment,
            model.id?.let { UsersChatFragmentArgs(it.toString()).toBundle() }
        )
    Log.e("id",model.id.toString())
    }
}