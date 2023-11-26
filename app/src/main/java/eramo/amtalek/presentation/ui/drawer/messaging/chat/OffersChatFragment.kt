package eramo.amtalek.presentation.ui.drawer.messaging.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentOffersChatBinding
import eramo.amtalek.presentation.adapters.recyclerview.RvOffersChatAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.util.Dummy
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.navOptionsAnimation
import javax.inject.Inject

@AndroidEntryPoint
class OffersChatFragment : BindingFragment<FragmentOffersChatBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentOffersChatBinding::inflate

    @Inject
    lateinit var rvOffersChatAdapter: RvOffersChatAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupListeners()
    }

    private fun setupViews() {
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)
        setupToolbar()

        assignFakeData()
        initRvChat()
    }

    private fun setupListeners(){
        binding.apply {
            viewBrokerHeader.setOnClickListener {
                findNavController().navigate(R.id.brokersDetailsFragment,null, navOptionsAnimation())
            }
        }
    }

    private fun setupToolbar() {
        binding.toolbar.apply {
            ivBack.setOnClickListener { findNavController().popBackStack() }
            tvTitle.text = getString(R.string.chat_messaging)
        }
    }

    private fun assignFakeData(){
        binding.apply {
            tvBrokerName.text = "ERA Estate Development Company"
            tvBrokerId.text = "era.estate"
            Glide.with(requireContext()).load("https://www.era-egypt.com/wp-content/uploads/2021/06/ERA-2004.png").into(ivBrokerLogo)

            tvOfferTitle.text = "شقة سكنية للبيع تشطيب سوبر لوكس"
            tvOfferPrice.text = "4,000,000 ج.م"
            Glide.with(requireContext()).load("https://www.era-egypt.com/wp-content/uploads/2021/06/ERA-2004.png").into(ivOfferImage)

        }
    }

    private fun initRvChat() {
        binding.rv.adapter = rvOffersChatAdapter
        rvOffersChatAdapter.submitList(Dummy.dummyChatList())

    }
}