package eramo.amtalek.presentation.ui.drawer.messaging

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentMessagingChatBinding
import eramo.amtalek.domain.model.drawer.MessagingChatModel
import eramo.amtalek.presentation.adapters.recyclerview.RvMessagingChatAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.util.Dummy
import eramo.amtalek.util.StatusBarUtil
import javax.inject.Inject

@AndroidEntryPoint
class MessagingChatFragment : BindingFragment<FragmentMessagingChatBinding>() {

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
        binding.rv.adapter = rvMessagingChatAdapter
        rvMessagingChatAdapter.submitList(data)

    }
}