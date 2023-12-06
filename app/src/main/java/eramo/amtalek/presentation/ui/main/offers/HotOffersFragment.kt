package eramo.amtalek.presentation.ui.main.offers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentHotOffersBinding
import eramo.amtalek.presentation.adapters.recyclerview.DummyProjectAdapter
import eramo.amtalek.presentation.adapters.recyclerview.DummyRealEstateAdapter
import eramo.amtalek.presentation.adapters.viewpager.HotOffersTypesPagerAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.presentation.viewmodel.navbottom.ShopViewModel
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.navOptionsAnimation
import javax.inject.Inject

@AndroidEntryPoint
class HotOffersFragment : BindingFragment<FragmentHotOffersBinding>(),
    DummyProjectAdapter.OnItemClickListener {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentHotOffersBinding::inflate

    private val viewModelShared: SharedViewModel by activityViewModels()
    private val viewModel: ShopViewModel by viewModels()

    @Inject
    lateinit var dummyProjectAdapter: DummyProjectAdapter

    @Inject
    lateinit var dummyRealEstateAdapter: DummyRealEstateAdapter

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
            toolbarIvMenu.setOnClickListener { viewModelShared.openDrawer.value = true }
            tvTitle.text = getString(R.string.hot_offer_title)
        }
    }

    private fun setupTabLayoutPager() {
        val hotOffersTypesPagerAdapter = HotOffersTypesPagerAdapter(childFragmentManager, lifecycle)
        binding.apply {
            viewPager.adapter = hotOffersTypesPagerAdapter
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = getString(R.string.for_sell)
                    1 -> tab.text = getString(R.string.for_rent)
                }
            }.attach()
        }
    }

    override fun onProductClick(model: String) {
        findNavController().navigate(R.id.projectDetailsFragment, null, navOptionsAnimation())
    }
}