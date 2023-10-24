package eramo.amtalek.presentation.ui.drawer.messaging

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentMessagingOfferBinding
import eramo.amtalek.domain.model.drawer.MessagingOffersModel
import eramo.amtalek.presentation.adapters.recyclerview.messaging.RvMessagingOffersAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.util.Dummy
import eramo.amtalek.util.StatusBarUtil
import javax.inject.Inject

@AndroidEntryPoint
class MessagingOfferFragment : BindingFragment<FragmentMessagingOfferBinding>() {

    override val isRefreshingEnabled: Boolean get() = false
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentMessagingOfferBinding::inflate

    @Inject
    lateinit var rvMessagingOffersAdapter: RvMessagingOffersAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)

        initChatRv(Dummy.dummyMessagingOffersList())
    }


    private fun initChatRv(data: List<MessagingOffersModel>) {
        binding.rv.adapter = rvMessagingOffersAdapter
        rvMessagingOffersAdapter.submitList(data)

        setupSearchRv(data)
    }

    private fun setupSearchRv(data: List<MessagingOffersModel>) {
        binding.etSearch.addTextChangedListener { text ->
            if (text.toString().isEmpty()) {
                rvMessagingOffersAdapter.submitList(data)
            } else {
                val list = data.filter {
                    it.senderName.lowercase().contains(text.toString().lowercase())
                }
                rvMessagingOffersAdapter.submitList(null)
                rvMessagingOffersAdapter.submitList(list)
            }
        }
    }
}