package eramo.amtalek.presentation.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.data.remote.dto.home.HomeResponse
import eramo.amtalek.databinding.FragmentHomeBinding
import eramo.amtalek.databinding.ItemSliderTopBinding
import eramo.amtalek.domain.model.auth.CityModel
import eramo.amtalek.domain.model.drawer.myfavourites.MyFavouritesModel
import eramo.amtalek.domain.model.main.home.NewsModel
import eramo.amtalek.domain.model.main.home.ProjectHomeModel
import eramo.amtalek.domain.model.main.home.PropertiesByCityModel
import eramo.amtalek.domain.model.main.home.PropertyModel
import eramo.amtalek.presentation.adapters.recyclerview.DummyFeaturedAdapter
import eramo.amtalek.presentation.adapters.recyclerview.DummyNewsAdapter
import eramo.amtalek.presentation.adapters.recyclerview.DummySliderTopAdapter
import eramo.amtalek.presentation.adapters.recyclerview.RvMyFavouritesAdapter
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeFeaturedProjectsAdapter
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeFeaturedRealEstateAdapter
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeFindPropertyByCityAdapter
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeNewestDuplexesAdapter
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeNewestPropertiesAdapter
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeNewestVillasAdapter
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeNewsAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.presentation.viewmodel.navbottom.HomeViewModel
import eramo.amtalek.util.Dummy
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.enum.PropertyType
import eramo.amtalek.util.navOptionsAnimation
import eramo.amtalek.util.onBackPressed
import eramo.amtalek.util.showToast
import eramo.amtalek.util.state.UiState
import org.imaginativeworld.whynotimagecarousel.listener.CarouselListener
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import org.imaginativeworld.whynotimagecarousel.utils.setImage
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BindingFragment<FragmentHomeBinding>(),
    DummyFeaturedAdapter.OnItemClickListener,
    RvMyFavouritesAdapter.OnItemClickListener,
    RvHomeNewsAdapter.OnItemClickListener,
    RvHomeFeaturedRealEstateAdapter.OnItemClickListener,
    RvHomeFindPropertyByCityAdapter.OnItemClickListener,
    RvHomeNewestPropertiesAdapter.OnItemClickListener,
    RvHomeNewestVillasAdapter.OnItemClickListener,
    RvHomeNewestDuplexesAdapter.OnItemClickListener,
    RvHomeFeaturedProjectsAdapter.OnItemClickListener,
    DummyNewsAdapter.OnItemClickListener,
        FilterCitiesDialogFragment.FilterCitiesDialogOnClickListener
{

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentHomeBinding::inflate

    private val viewModel by viewModels<HomeViewModel>()
    private val viewModelShared: SharedViewModel by activityViewModels()

    private var backPressedTime: Long = 0

    @Inject
    lateinit var dummySliderTopAdapter: DummySliderTopAdapter

    @Inject
    lateinit var rvHomeFeaturedRealEstateAdapter: RvHomeFeaturedRealEstateAdapter

    @Inject
    lateinit var rvHomeFeaturedProjectsAdapter: RvHomeFeaturedProjectsAdapter

    @Inject
    lateinit var rvHomeFindPropertyByCityAdapter: RvHomeFindPropertyByCityAdapter

    // ---------------------------------------------------------------------------
    @Inject
    lateinit var rvHomeNewestPropertiesAdapter: RvHomeNewestPropertiesAdapter

    @Inject
    lateinit var rvHomeNewestVillasAdapter: RvHomeNewestVillasAdapter

    @Inject
    lateinit var rvHomeNewestDuplexesAdapter: RvHomeNewestDuplexesAdapter
    // ---------------------------------------------------------------------------

    @Inject
    lateinit var rvHomeNewsAdapter: RvHomeNewsAdapter

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

        requestApis()
        fetchData()


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
        Log.e("token", UserUtil.getUserToken())
    }

    private fun setupViews() {
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)

        initToolbar()
    }

    private fun listeners() {
        binding.apply {

            val filterCitiesDialogFragment = FilterCitiesDialogFragment()
            filterCitiesDialogFragment.setListener(this@HomeFragment)
            inToolbar.spinnerLayout.setOnClickListener {
                filterCitiesDialogFragment.show(
                    activity?.supportFragmentManager!!,
                    "FilterCitiesDialogFragment"
                )
            }

            inToolbar.inNotification.root.setOnClickListener {
                findNavController().navigate(R.id.notificationFragment, null, navOptionsAnimation())
            }
            inToolbar.inMessaging.root.setOnClickListener {
                findNavController().navigate(R.id.messagingFragment, null, navOptionsAnimation())
            }
        }

        this@HomeFragment.onBackPressed { pressBackAgainToExist() }
    }

    private fun requestApis() {
        viewModel.getHome()
    }

    private fun fetchData() {
        fetchHomeState()
    }

    // -------------------------------------- fetchData -------------------------------------- //

    private fun fetchHomeState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.homeState.collect { state ->
                    when (state) {

                        is UiState.Success -> {
                            parseHomeResponse(state.data!!)
                        }

                        is UiState.Error -> {
                            dismissShimmerEffect()
                            val errorMessage = state.message!!.asString(requireContext())
                            showToast(errorMessage)
                        }

                        is UiState.Loading -> {
                            showShimmerEffect()
                        }

                        else -> {}
                    }

                }
            }
        }
    }

    private fun parseHomeResponse(data: HomeResponse) {
        val topCarouselSliderList = parseTopCarouselSliderList(data.data?.sliders)
        setupCarouselSliderTop(topCarouselSliderList)

        setupFeaturedRealEstateRv(data.data?.featuredPropertiesCountry!!.map { it!!.toPropertyModel() })
        setupFeaturedProjectsRv(data.data.featuredProjectsCountry!!.map { it!!.toProjectModel() })

        setupFindPropertiesByCityRv(data.data.propertyInCity!!.map { it!!.toPropertiesByCityModel() })

        val betweenCarouselSliderList = parseBetweenCarouselSliderList(data.data.adds)
        setupSliderBetween(betweenCarouselSliderList)

        setupNewestPropertiesRv(data.data.appaerments!!.map { it!!.toPropertyModel() })
        setupNewestVillasRv(data.data.villas!!.map { it!!.toPropertyModel() })
        setupNewestDuplexesRv(data.data.duplixes!!.map { it!!.toPropertyModel() })

        setupNewsRv(data.data.news!!.map { it!!.toNewsModel() })

        dismissShimmerEffect()
    }

    private fun setupCarouselSliderTop(data: ArrayList<CarouselItem>) {
        binding.apply {
            carouselSliderTop.registerLifecycle(viewLifecycleOwner.lifecycle)
            carouselSliderTop.setData(data)
            carouselSliderTop.setIndicator(carouselSliderTopDots)

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

    private fun parseTopCarouselSliderList(data: List<HomeResponse.Data.Slider?>?): ArrayList<CarouselItem> {
        val list = ArrayList<CarouselItem>()
        val headers = mutableMapOf<String, String>()
        headers["header_key"] = "header_value"

        for (i in data!!) {
            list.add(
                CarouselItem(
                    imageUrl = i?.image,
                )
            )
        }

        return list
    }

    private fun parseBetweenCarouselSliderList(data: List<HomeResponse.Data.Adds?>?): ArrayList<CarouselItem> {
        val list = ArrayList<CarouselItem>()
        val headers = mutableMapOf<String, String>()
        headers["header_key"] = "header_value"

        for (i in data!!) {
            list.add(
                CarouselItem(
                    imageUrl = i?.image,
                )
            )
        }

        return list
    }

    private fun setupSliderBetween(data: ArrayList<CarouselItem>) {
        binding.apply {
            carouselSliderBetween.registerLifecycle(viewLifecycleOwner.lifecycle)
            carouselSliderBetween.setData(data)
            carouselSliderBetween.setIndicator(carouselSliderBetweenDots)
            carouselSliderBetween.carouselListener = object : CarouselListener {
                override fun onCreateViewHolder(
                    layoutInflater: LayoutInflater,
                    parent: ViewGroup
                ): ViewBinding {
//                    return ItemAdsBinding.inflate(
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
//                    val currentBinding = binding as ItemAdsBinding
                    val currentBinding = binding as ItemSliderTopBinding
                    currentBinding.apply {
                        imageView13.setImage(item)
                    }

                }
            }
        }
    }


    private fun setupCountriesSpinner() {
////        val citiesToolbarSpinnerAdapter = CitiesToolbarSpinnerAdapter(requireContext(), Dummy.dummyCitiesList())
////        binding.inToolbar.toolbarSpinner.adapter = citiesToolbarSpinnerAdapter
//
//        binding.inToolbar.toolbarSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
////                val model = parent?.getItemAtPosition(position) as CountriesSpinnerModel
////
////                Toast.makeText(requireContext(), model.countryName, Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//
//            }
//        }
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

    private fun setupFeaturedRealEstateRv(data: List<PropertyModel>) {
        rvHomeFeaturedRealEstateAdapter.setListener(this@HomeFragment)
        binding.inFeaturedRealEstate.rv.adapter = rvHomeFeaturedRealEstateAdapter
        setupFeaturedRealEstateHeaderListener(data)
    }

    private fun setupFeaturedRealEstateHeaderListener(data: List<PropertyModel>) {

        binding.inFeaturedRealEstate.apply {

            // default
            tvAll.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            tvForSell.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_faded_gray))
            tvForRent.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_faded_gray))
            rvHomeFeaturedRealEstateAdapter.submitList(data)

            // all
            tvAll.setOnClickListener {
                tvAll.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                tvForSell.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_faded_gray))
                tvForRent.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_faded_gray))

                rvHomeFeaturedRealEstateAdapter.submitList(null)
                rvHomeFeaturedRealEstateAdapter.submitList(data)
            }

            // sell
            tvForSell.setOnClickListener {
                tvAll.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_faded_gray))
                tvForSell.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                tvForRent.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_faded_gray))

                rvHomeFeaturedRealEstateAdapter.submitList(null)
                rvHomeFeaturedRealEstateAdapter.submitList(data.filter { it.type == PropertyType.FOR_SELL.key })
            }

            // rent
            tvForRent.setOnClickListener {
                tvAll.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_faded_gray))
                tvForSell.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_faded_gray))
                tvForRent.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))

                rvHomeFeaturedRealEstateAdapter.submitList(null)
                rvHomeFeaturedRealEstateAdapter.submitList(data.filter { it.type == PropertyType.FOR_RENT.key })
            }
        }
    }

    private fun setupFeaturedProjectsRv(data: List<ProjectHomeModel>) {
        rvHomeFeaturedProjectsAdapter.setListener(this@HomeFragment)
        binding.inFeaturedProjects.rv.adapter = rvHomeFeaturedProjectsAdapter
        rvHomeFeaturedProjectsAdapter.submitList(data)

    }

    private fun setupFindPropertiesByCityRv(data: List<PropertiesByCityModel>) {
        rvHomeFindPropertyByCityAdapter.setListener(this@HomeFragment)
        binding.inPropertiesByCity.rv.adapter = rvHomeFindPropertyByCityAdapter
        rvHomeFindPropertyByCityAdapter.submitList(data)

    }

    // -------------------------------------------------------------------------------------------------------------- //
    private fun setupNewestPropertiesRv(data: List<PropertyModel>) {
        rvHomeNewestPropertiesAdapter.setListener(this@HomeFragment)
        binding.inNewestPropertiesLayout.rv.adapter = rvHomeNewestPropertiesAdapter
        setupNewestPropertiesHeaderListener(data)
    }

    private fun setupNewestPropertiesHeaderListener(data: List<PropertyModel>) {

        binding.inNewestPropertiesLayout.apply {

            // default
            tvAll.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            tvForSell.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_faded_gray))
            tvForRent.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_faded_gray))
            rvHomeNewestPropertiesAdapter.submitList(data)


            tvAll.setOnClickListener {
                tvAll.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                tvForSell.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_faded_gray))
                tvForRent.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_faded_gray))

                rvHomeNewestPropertiesAdapter.submitList(null)
                rvHomeNewestPropertiesAdapter.submitList(data)
            }

            tvForSell.setOnClickListener {
                tvAll.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_faded_gray))
                tvForSell.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                tvForRent.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_faded_gray))

                rvHomeNewestPropertiesAdapter.submitList(null)
                rvHomeNewestPropertiesAdapter.submitList(data.filter { it.type == PropertyType.FOR_SELL.key })
            }

            tvForRent.setOnClickListener {
                tvAll.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_faded_gray))
                tvForSell.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_faded_gray))
                tvForRent.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))

                rvHomeNewestPropertiesAdapter.submitList(null)
                rvHomeNewestPropertiesAdapter.submitList(data.filter { it.type == PropertyType.FOR_RENT.key })
            }
        }
    }


    private fun setupNewestVillasRv(data: List<PropertyModel>) {
        rvHomeNewestVillasAdapter.setListener(this@HomeFragment)
        binding.inNewestVillasLayout.rv.adapter = rvHomeNewestVillasAdapter
        setupNewestVillasHeaderListener(data)
    }

    private fun setupNewestVillasHeaderListener(data: List<PropertyModel>) {

        binding.inNewestVillasLayout.apply {

            // default
            tvAll.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            tvForSell.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_faded_gray))
            tvForRent.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_faded_gray))
            rvHomeNewestVillasAdapter.submitList(data)


            tvAll.setOnClickListener {
                tvAll.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                tvForSell.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_faded_gray))
                tvForRent.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_faded_gray))

                rvHomeNewestVillasAdapter.submitList(null)
                rvHomeNewestVillasAdapter.submitList(data)
            }

            tvForSell.setOnClickListener {
                tvAll.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_faded_gray))
                tvForSell.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                tvForRent.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_faded_gray))

                rvHomeNewestVillasAdapter.submitList(null)
                rvHomeNewestVillasAdapter.submitList(data.filter { it.type == PropertyType.FOR_SELL.key })
            }

            tvForRent.setOnClickListener {
                tvAll.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_faded_gray))
                tvForSell.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_faded_gray))
                tvForRent.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))

                rvHomeNewestVillasAdapter.submitList(null)
                rvHomeNewestVillasAdapter.submitList(data.filter { it.type == PropertyType.FOR_RENT.key })
            }
        }
    }

    private fun setupNewestDuplexesRv(data: List<PropertyModel>) {
        rvHomeNewestDuplexesAdapter.setListener(this@HomeFragment)
        binding.inNewestDuplexesLayout.rv.adapter = rvHomeNewestDuplexesAdapter
        rvHomeNewestDuplexesAdapter.submitList(data)
        setupNewestDuplexesHeaderListener(data)
    }

    private fun setupNewestDuplexesHeaderListener(data: List<PropertyModel>) {

        binding.inNewestDuplexesLayout.apply {

            // default
            tvAll.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            tvForSell.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_faded_gray))
            tvForRent.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_faded_gray))
            rvHomeNewestDuplexesAdapter.submitList(data)

            tvAll.setOnClickListener {
                tvAll.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                tvForSell.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_faded_gray))
                tvForRent.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_faded_gray))

                rvHomeNewestDuplexesAdapter.submitList(null)
                rvHomeNewestDuplexesAdapter.submitList(data)
            }

            tvForSell.setOnClickListener {
                tvAll.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_faded_gray))
                tvForSell.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                tvForRent.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_faded_gray))

                rvHomeNewestDuplexesAdapter.submitList(null)
                rvHomeNewestDuplexesAdapter.submitList(data.filter { it.type == PropertyType.FOR_SELL.key })
            }

            tvForRent.setOnClickListener {
                tvAll.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_faded_gray))
                tvForSell.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_faded_gray))
                tvForRent.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))

                rvHomeNewestDuplexesAdapter.submitList(null)
                rvHomeNewestDuplexesAdapter.submitList(data.filter { it.type == PropertyType.FOR_RENT.key })
            }
        }
    }

    private fun setupNewsRv(data: List<NewsModel>) {
        rvHomeNewsAdapter.setListener(this@HomeFragment)
        binding.inNewsLayout.rv.adapter = rvHomeNewsAdapter
        rvHomeNewsAdapter.submitList(data)
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

    private fun showShimmerEffect() {
        binding.apply {
            shimmerLayout.startShimmer()

            viewLayout.visibility = View.GONE
            shimmerLayout.visibility = View.VISIBLE
        }
    }

    private fun dismissShimmerEffect() {
        binding.apply {
            shimmerLayout.stopShimmer()

            viewLayout.visibility = View.VISIBLE
            shimmerLayout.visibility = View.GONE
        }
    }

    private fun pressBackAgainToExist() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            requireActivity().finish()
        } else {
            showToast(getString(R.string.press_back_again_to_exist))
        }
        backPressedTime = System.currentTimeMillis()
    }

    // ------------------------------------------------------------------------------------------------------------------------------------ //

    override fun onFeaturedRealEstateClick(model: PropertyModel) {
        when (model.type) {
            getString(R.string.for_sell) -> {
                findNavController().navigate(R.id.propertyDetailsSellFragment, null, navOptionsAnimation())
            }

            getString(R.string.for_rent) -> {
                findNavController().navigate(R.id.propertyDetailsRentFragment, null, navOptionsAnimation())
            }
        }
    }

    override fun onFeaturedProjectClick(model: ProjectHomeModel) {
        findNavController().navigate(R.id.myProjectDetailsFragment, null, navOptionsAnimation())
    }

    override fun onFeaturedClick(model: String) {
        findNavController().navigate(R.id.propertyDetailsFragment, null, navOptionsAnimation())
    }

//    override fun onPropertyClick(model: String) {
//        findNavController().navigate(R.id.propertyDetailsFragment, null, navOptionsAnimation())
//    }


    override fun onPropertyByCityClick(model: PropertiesByCityModel) {
        findNavController().navigate(R.id.propertyDetailsSellFragment, null, navOptionsAnimation())

    }

    override fun onNewestPropertyClick(model: PropertyModel) {
        when (model.type) {
            getString(R.string.for_sell) -> {
                findNavController().navigate(R.id.propertyDetailsSellFragment, null, navOptionsAnimation())
            }

            getString(R.string.for_rent) -> {
                findNavController().navigate(R.id.propertyDetailsRentFragment, null, navOptionsAnimation())
            }
        }
    }


    override fun onNewestVillaClick(model: PropertyModel) {
        when (model.type) {
            getString(R.string.for_sell) -> {
                findNavController().navigate(R.id.propertyDetailsSellFragment, null, navOptionsAnimation())
            }

            getString(R.string.for_rent) -> {
                findNavController().navigate(R.id.propertyDetailsRentFragment, null, navOptionsAnimation())
            }
        }
    }


    override fun onNewestDuplexesClick(model: PropertyModel) {
        when (model.type) {
            getString(R.string.for_sell) -> {
                findNavController().navigate(R.id.propertyDetailsSellFragment, null, navOptionsAnimation())
            }

            getString(R.string.for_rent) -> {
                findNavController().navigate(R.id.propertyDetailsRentFragment, null, navOptionsAnimation())
            }
        }
    }


    override fun onNewsClick(model: String) {
//        findNavController().navigate(R.id.nyEstateRentFragment, null, navOptionsAnimation())
    }

    override fun onPropertyClick(model: MyFavouritesModel) {
    }

    override fun onNewsClick(model: NewsModel) {
        findNavController().navigate(R.id.newsDetailsFragment, null, navOptionsAnimation())
    }

    override fun onFilterCitiesDialogItemClick(model: CityModel) {
        showToast(model.name)
    }

}