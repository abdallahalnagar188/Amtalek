package eramo.amtalek.presentation.ui.drawer.messaging

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentOffersChatBinding
import eramo.amtalek.presentation.adapters.recyclerview.RvOffersChatAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.util.Dummy
import eramo.amtalek.util.StatusBarUtil
import javax.inject.Inject

@AndroidEntryPoint
class OffersChatFragment : BindingFragment<FragmentOffersChatBinding>() {

    override val isRefreshingEnabled: Boolean get() = false
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentOffersChatBinding::inflate

    @Inject
    lateinit var rvOffersChatAdapter: RvOffersChatAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)
        setupToolbar()

        initRvChat()
    }

    private fun setupToolbar() {
        binding.toolbar.apply {
            ivBack.setOnClickListener { findNavController().popBackStack() }
            tvTitle.text = getString(R.string.chat)
        }
    }

    private fun initRvChat() {
        binding.rv.adapter = rvOffersChatAdapter
        rvOffersChatAdapter.submitList(Dummy.dummyChatList())
    }
}