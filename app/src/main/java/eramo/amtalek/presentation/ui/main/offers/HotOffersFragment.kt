package eramo.amtalek.presentation.ui.main.offers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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
import eramo.amtalek.util.LocalUtil
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.navOptionsAnimation
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HotOffersFragment : BindingFragment<FragmentHotOffersBinding>(),
    DummyProjectAdapter.OnItemClickListener {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentHotOffersBinding::inflate

    private val viewModelShared: SharedViewModel by activityViewModels()
    private val viewModel: ShopViewModel by viewModels()
    private val hotOffersViewModel: HotOffersViewModel by viewModels()
    @Inject
    lateinit var dummyProjectAdapter: DummyProjectAdapter

    @Inject
    lateinit var dummyRealEstateAdapter: DummyRealEstateAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hotOffersViewModel.getHotOffers(UserUtil.getUserCountryFiltrationTitleId())
        setUpObservers()
        setupViews()
        listeners()
    }

    private fun listeners() {
        binding.inToolbar.spinnerLayout.setOnClickListener {
            findNavController().navigate(R.id.filterCitiesDialogFragment, null, navOptionsAnimation())
        }

    }
    private fun initToolbar() {
        binding.inToolbar.apply {
            toolbarIvMenu.setOnClickListener { viewModelShared.openDrawer.value = true }
            inNotification.root.setOnClickListener {
                findNavController().navigate(R.id.notificationFragment)
            }
        }
        if (LocalUtil.isEnglish()){
            binding.inToolbar.tvSpinnerText.text = UserUtil.getCityFiltrationTitleEn()

        }else{
            binding.inToolbar.tvSpinnerText.text = UserUtil.getCityFiltrationTitleAr()
        }
        if (UserUtil.getCityFiltrationTitleAr().isEmpty()&&UserUtil.getCityFiltrationTitleEn().isEmpty()){
            binding.inToolbar.tvSpinnerText.text = context?.getString(R.string.select_city)

        }
    }

    private fun setupViews() {
        initToolbar()
        setupTabLayoutPager()
    }
    private fun setUpObservers() {
        fetchGetHotOffers()
    }
    private fun fetchGetHotOffers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                hotOffersViewModel.hotOffers.collect(){
                    when(it){
                        is UiState.Success->{

                            dismissShimmerEffect()
                        }
                        is UiState.Error->{

                            dismissShimmerEffect()
                        }
                        is UiState.Loading->{
//                            showShimmerEffect()
                        }
                        else -> {}
                    }
                }
            }
        }
    }



    private fun setupTabLayoutPager() {
        val hotOffersTypesPagerAdapter = HotOffersTypesPagerAdapter(childFragmentManager, lifecycle)
        binding.apply {
            viewPager.adapter = hotOffersTypesPagerAdapter
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                when (position) {
                    0 -> tab.text =  getString(R.string.forBoth)
                    1 -> tab.text =  getString(R.string.for_sell)
                    2 -> tab.text =  getString(R.string.for_rent)
                }
            }.attach()
        }
    }

    override fun onProductClick(model: String) {
        findNavController().navigate(R.id.projectDetailsFragment, null, navOptionsAnimation())
    }
    private fun showShimmerEffect() {
        binding.apply {
            shimmerLayout.startShimmer()

            viewPager.visibility = View.GONE
            shimmerLayout.visibility = View.VISIBLE
        }
    }
    private fun dismissShimmerEffect() {
        binding.apply {
            shimmerLayout.stopShimmer()

            viewPager.visibility = View.VISIBLE
            shimmerLayout.visibility = View.GONE
        }
    }
}