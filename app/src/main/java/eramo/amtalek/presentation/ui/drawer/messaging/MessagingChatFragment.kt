package eramo.amtalek.presentation.ui.drawer.messaging

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentMessagingChatBinding
import eramo.amtalek.domain.model.drawer.MessagingChatModel
import eramo.amtalek.presentation.adapters.recyclerview.messaging.RvMessagingChatAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.util.Dummy
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.navOptionsAnimation
import javax.inject.Inject

@AndroidEntryPoint
class MessagingChatFragment : BindingFragment<FragmentMessagingChatBinding>(), RvMessagingChatAdapter.OnItemClickListener {

    override val isRefreshingEnabled: Boolean get() = false
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentMessagingChatBinding::inflate

    @Inject
    lateinit var rvMessagingChatAdapter: RvMessagingChatAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)

        initChatRv(Dummy.dummyMessagingChatList())
    }


    private fun initChatRv(data: List<MessagingChatModel>) {
        rvMessagingChatAdapter.setListener(this@MessagingChatFragment)
        binding.rv.adapter = rvMessagingChatAdapter
        rvMessagingChatAdapter.submitList(data)

        setupSearchRv(data)
    }

    private fun setupSearchRv(data: List<MessagingChatModel>) {
        binding.etSearch.addTextChangedListener { text ->
            if (text.toString().isEmpty()) {
                rvMessagingChatAdapter.submitList(data)
            } else {
                val list = data.filter {
                    it.senderName.lowercase().contains(text.toString().lowercase())
                }
                rvMessagingChatAdapter.submitList(null)
                rvMessagingChatAdapter.submitList(list)
            }
        }
    }

    override fun onChatClick(model: MessagingChatModel) {
        findNavController().navigate(R.id.usersChatFragment, null, navOptionsAnimation())
    }
}