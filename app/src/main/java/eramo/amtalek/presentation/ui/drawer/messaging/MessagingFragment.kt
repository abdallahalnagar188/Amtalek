package eramo.amtalek.presentation.ui.drawer.messaging

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentMessagingBinding
import eramo.amtalek.presentation.adapters.recyclerview.messaging.RvMessagingChatAdapter
import eramo.amtalek.presentation.ui.BindingFragment

@AndroidEntryPoint
class MessagingFragment : BindingFragment<FragmentMessagingBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentMessagingBinding::inflate

    val viewModel:MessagingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getContactedAgentsMessage(viewModel.contactedAgents.value?.data?.get(0)?.id.toString())
        Log.e("Message",viewModel.contactedAgents.toString())
        setupViews()
    }

    private fun setupViews() {
        setupToolbar()
        setupTabLayoutPager()
    }

    private fun setupToolbar() {
        binding.inToolbar.apply {
            ivBack.setOnClickListener { findNavController().popBackStack() }
            tvTitle.text = getString(R.string.messages)
        }
    }

    private fun setupTabLayoutPager() {
        val messagingTypesPagerAdapter = RvMessagingChatAdapter()
        binding.apply {
            viewPager.adapter = messagingTypesPagerAdapter
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = getString(R.string.chat)
                    1 -> tab.text = getString(R.string.offer)
                }
            }.attach()
        }
    }
}