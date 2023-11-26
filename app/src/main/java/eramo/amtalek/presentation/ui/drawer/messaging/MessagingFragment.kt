package eramo.amtalek.presentation.ui.drawer.messaging

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentMessagingBinding
import eramo.amtalek.presentation.adapters.viewpager.MessagingTypesPagerAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.util.StatusBarUtil

@AndroidEntryPoint
class MessagingFragment : BindingFragment<FragmentMessagingBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentMessagingBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)
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
        val messagingTypesPagerAdapter = MessagingTypesPagerAdapter(childFragmentManager, lifecycle)
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