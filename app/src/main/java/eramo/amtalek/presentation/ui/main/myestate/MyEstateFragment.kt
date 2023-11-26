package eramo.amtalek.presentation.ui.main.myestate

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
import eramo.amtalek.databinding.FragmentMyEstateBinding
import eramo.amtalek.presentation.adapters.recyclerview.DummyNewsPreviewAdapter
import eramo.amtalek.presentation.adapters.viewpager.MyEstateTypesPagerAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.presentation.viewmodel.navbottom.ShopViewModel
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.navOptionsAnimation
import javax.inject.Inject

@AndroidEntryPoint
class MyEstateFragment : BindingFragment<FragmentMyEstateBinding>(),
    DummyNewsPreviewAdapter.OnItemClickListener {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentMyEstateBinding::inflate

    private val viewModelShared: SharedViewModel by activityViewModels()
    private val viewModel: ShopViewModel by viewModels()

    @Inject
    lateinit var dummyNewsPreviewAdapter: DummyNewsPreviewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        listeners()
    }

    private fun setupViews() {
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)
        setupToolbar()

        setupTabLayoutPager()
    }

    private fun listeners() {
        binding.apply {
            btnAddProperty.setOnClickListener {
                findNavController().navigate(R.id.myAddPropertyFragmentFragment, null, navOptionsAnimation())
            }
        }
    }

    private fun setupToolbar() {
        binding.inToolbar.apply {
            toolbarIvMenu.setOnClickListener { viewModelShared.openDrawer.value = true }
            tvTitle.text = getString(R.string.my_estate)
        }
    }

    private fun setupTabLayoutPager() {
        val myEstateTypesPagerAdapter = MyEstateTypesPagerAdapter(childFragmentManager, lifecycle)
        binding.apply {
            viewPager.adapter = myEstateTypesPagerAdapter
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = getString(R.string.for_sell)
                    1 -> tab.text = getString(R.string.for_rent)
                }
            }.attach()
        }
    }

    override fun onProductClick(model: String) {
        findNavController().navigate(R.id.nyEstateRentFragment)
    }

}