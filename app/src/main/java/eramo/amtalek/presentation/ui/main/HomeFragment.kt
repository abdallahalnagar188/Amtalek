package eramo.amtalek.presentation.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentHomeBinding
import eramo.amtalek.databinding.ItemAdsBinding
import eramo.amtalek.databinding.ItemSliderTopBinding
import eramo.amtalek.domain.model.drawer.myfavourites.MyFavouritesModel
import eramo.amtalek.presentation.adapters.recyclerview.DummyFeaturedAdapter
import eramo.amtalek.presentation.adapters.recyclerview.DummyNewsAdapter
import eramo.amtalek.presentation.adapters.recyclerview.DummySliderTopAdapter
import eramo.amtalek.presentation.adapters.recyclerview.RvMyFavouritesAdapter
import eramo.amtalek.presentation.adapters.spinner.CitiesToolbarSpinnerAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.presentation.viewmodel.navbottom.HomeViewModel
import eramo.amtalek.util.Dummy
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.navOptionsAnimation
import org.imaginativeworld.whynotimagecarousel.listener.CarouselListener
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import org.imaginativeworld.whynotimagecarousel.utils.setImage
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BindingFragment<FragmentHomeBinding>(),
    DummyFeaturedAdapter.OnItemClickListener,
    RvMyFavouritesAdapter.OnItemClickListener,
    DummyNewsAdapter.OnItemClickListener {

    override val isRefreshingEnabled: Boolean get() = true
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentHomeBinding::inflate

    private val viewModel by viewModels<HomeViewModel>()
    private val viewModelShared: SharedViewModel by activityViewModels()

    @Inject
    lateinit var dummySliderTopAdapter: DummySliderTopAdapter

    @Inject
    lateinit var dummyFeaturedAdapter: DummyFeaturedAdapter

    @Inject
    lateinit var rvMyFavouritesAdapter: RvMyFavouritesAdapter

    @Inject
    lateinit var dummyNewsAdapter: DummyNewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        super.registerApiRequest { viewModel.getScreenApis() }
//        super.registerApiCancellation { viewModel.cancelRequest() }
//        viewModel.getCartCount()


        setupViews()
        listeners()

        setupSliderBetween()

        dummyFeaturedAdapter.setListener(this)
        rvMyFavouritesAdapter.setListener(this)
        dummyNewsAdapter.setListener(this)
        binding.apply {
            inSearch.inCurrency.tvText.hint = getString(R.string.currency)

            dummyFeaturedAdapter.submitList(Dummy.list())
            rvFeatures.adapter = dummyFeaturedAdapter

//            rvMyFavouritesAdapter.submitList(Dummy.list())
            rvProperties.adapter = rvMyFavouritesAdapter

            dummyNewsAdapter.submitList(Dummy.list())
            rvNews.adapter = dummyNewsAdapter

            tvSale.setOnClickListener { selectedTabPosition(0) }
            tvLatest.setOnClickListener { selectedTabPosition(1) }
            tvRent.setOnClickListener { selectedTabPosition(2) }

            tvSeeAllProperties.setOnClickListener {
                findNavController().navigate(
                    R.id.allPropertiesFragment,
                    null,
                    navOptionsAnimation()
                )
            }

            tvSeeAllNews.setOnClickListener {
                findNavController().navigate(R.id.myEstateFragment, null, navOptionsAnimation())
            }
        }
    }

    private fun setupViews() {
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)

        initToolbar()
        setupCarouselSliderTop()
    }

    private fun listeners() {
        binding.inToolbar.inNotification.root.setOnClickListener {
            findNavController().navigate(R.id.notificationFragment, null, navOptionsAnimation())
        }

    }


    private fun setupCountriesSpinner() {
        val citiesToolbarSpinnerAdapter = CitiesToolbarSpinnerAdapter(requireContext(), Dummy.dummyCitiesList())
        binding.inToolbar.toolbarSpinner.adapter = citiesToolbarSpinnerAdapter

        binding.inToolbar.toolbarSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                val model = parent?.getItemAtPosition(position) as CountriesSpinnerModel
//
//                Toast.makeText(requireContext(), model.countryName, Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private fun initToolbar() {
        binding.inToolbar.apply {
            toolbarIvMenu.setOnClickListener { viewModelShared.openDrawer.value = true }
//            ivSearch.setOnClickListener { findNavController().navigate(R.id.searchPropertyFragment) }
            inNotification.root.setOnClickListener {
                findNavController().navigate(R.id.notificationFragment)
            }
        }

        setupCountriesSpinner()
    }

    private fun setupCarouselSliderTop() {
        binding.apply {
//        carouselSliderTop.registerLifecycle(lifecycle)
            carouselSliderTop.registerLifecycle(viewLifecycleOwner.lifecycle)
            carouselSliderTop.setData(Dummy.dummyCarouselList())
            carouselSliderTop.setIndicator(dotFeatured)


            carouselSliderTop.carouselListener = object : CarouselListener {
                override fun onCreateViewHolder(
                    layoutInflater: LayoutInflater,
                    parent: ViewGroup
                ): ViewBinding {
                    return ItemSliderTopBinding.inflate(
                        layoutInflater,
                        parent,
                        false
                    )
                }

                override fun onBindViewHolder(
                    binding: ViewBinding,
                    item: CarouselItem,
                    position: Int
                ) {
                    val currentBinding = binding as ItemSliderTopBinding
                    currentBinding.apply {
                        imageView13.setImage(item)
                    }

                }
            }
        }
    }

    private fun setupSliderBetween() {
        binding.apply {
            carouselSliderBetween.registerLifecycle(lifecycle)
            carouselSliderBetween.setData(Dummy.dummyCarouselList())
            carouselSliderBetween.setIndicator(dotAds)
            carouselSliderBetween.carouselListener = object : CarouselListener {
                override fun onCreateViewHolder(
                    layoutInflater: LayoutInflater,
                    parent: ViewGroup
                ): ViewBinding {
                    return ItemAdsBinding.inflate(
                        layoutInflater,
                        parent,
                        false
                    )
                }

                override fun onBindViewHolder(
                    binding: ViewBinding,
                    item: CarouselItem,
                    position: Int
                ) {
                    val currentBinding = binding as ItemAdsBinding
                    currentBinding.apply {

                    }

                }
            }
        }
    }

    private fun selectedTabPosition(position: Int) {
        binding.apply {
            tvSale.setBackgroundResource(R.drawable.ic_gray_end)
            tvLatest.setBackgroundResource(R.color.gray_low)
            tvRent.setBackgroundResource(R.drawable.ic_gray_start)

            tvSale
                .setTextColor(ContextCompat.getColor(requireContext(), R.color.amtalek_blue_dark))
            tvLatest
                .setTextColor(ContextCompat.getColor(requireContext(), R.color.amtalek_blue_dark))
            tvRent
                .setTextColor(ContextCompat.getColor(requireContext(), R.color.amtalek_blue_dark))

            when (position) {
                0 -> {
                    tvSale.setBackgroundResource(R.drawable.ic_blue_end)
                    tvSale.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                }

                1 -> {
                    tvLatest.setBackgroundResource(R.color.amtalek_blue_dark)
                    tvLatest.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                }

                2 -> {
                    tvRent.setBackgroundResource(R.drawable.ic_blue_start)
                    tvRent.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                }
            }
        }
    }

    override fun onFeaturedClick(model: String) {
        findNavController().navigate(R.id.propertyDetailsFragment, null, navOptionsAnimation())
    }

//    override fun onPropertyClick(model: String) {
//        findNavController().navigate(R.id.propertyDetailsFragment, null, navOptionsAnimation())
//    }

    override fun onNewsClick(model: String) {
        findNavController().navigate(R.id.nyEstateRentFragment, null, navOptionsAnimation())
    }

    override fun onPropertyClick(model: MyFavouritesModel) {
    }
}