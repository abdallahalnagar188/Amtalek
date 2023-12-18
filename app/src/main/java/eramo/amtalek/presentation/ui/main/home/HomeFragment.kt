package eramo.amtalek.presentation.ui.main.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
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
import eramo.amtalek.domain.model.main.home.ProjectModel
import eramo.amtalek.domain.model.main.home.PropertiesByCityModel
import eramo.amtalek.domain.model.main.home.PropertyModel
import eramo.amtalek.presentation.adapters.recyclerview.DummyFeaturedAdapter
import eramo.amtalek.presentation.adapters.recyclerview.DummyNewsAdapter
import eramo.amtalek.presentation.adapters.recyclerview.RvMyFavouritesAdapter
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeFeaturedProjectsAdapter
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeFeaturedRealEstateAdapter
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeFindPropertyByCityAdapter
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeNewestDuplexesAdapter
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeNewestPropertiesAdapter
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeNewestVillasAdapter
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeNewsAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.main.home.details.properties.PropertyDetailsRentFragmentArgs
import eramo.amtalek.presentation.ui.main.home.details.properties.PropertyDetailsSellAndRentFragmentArgs
import eramo.amtalek.presentation.ui.main.home.details.properties.PropertyDetailsSellFragmentArgs
import eramo.amtalek.presentation.ui.main.home.seemore.SeeMoreProjectsFragmentArgs
import eramo.amtalek.presentation.ui.main.home.seemore.SeeMorePropertiesFragmentArgs
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.presentation.viewmodel.navbottom.HomeViewModel
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.enum.PropertyType
import eramo.amtalek.util.navOptionsAnimation
import eramo.amtalek.util.navOptionsFromTopAnimation
import eramo.amtalek.util.onBackPressed
import eramo.amtalek.util.showToast
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.launch
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
    FilterCitiesDialogFragment.FilterCitiesDialogOnClickListener {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentHomeBinding::inflate

    private val viewModel by viewModels<HomeViewModel>()
    private val viewModelShared: SharedViewModel by activityViewModels()

    private var backPressedTime: Long = 0

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        super.registerApiRequest { viewModel.getScreenApis() }
//        super.registerApiCancellation { viewModel.cancelRequest() }
//        viewModel.getCartCount()


        setupViews()
        listeners()

        requestApis()
        fetchData()

        Log.e("token", UserUtil.getUserToken())
    }

    private fun setupViews() {
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
//        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)

        initToolbar()
    }

    @SuppressLint("ClickableViewAccessibility")
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

            inToolbar.FHomeEtSearch.setOnTouchListener { view, motionEvent ->

                when (motionEvent?.action) {
                    MotionEvent.ACTION_UP -> {
                        view.performClick()
                        findNavController().navigate(R.id.searchPropertyFragment, null, navOptionsFromTopAnimation())
                    }
                }
                return@setOnTouchListener true
            }
        }

        this@HomeFragment.onBackPressed { pressBackAgainToExist() }
    }

    private fun requestApis() {
        if (UserUtil.isUserLogin()) {
            Log.e("time", System.currentTimeMillis().toString())
            viewModel.getProfile()
            viewModel.getHome()
        } else {
            viewModel.getHome()
        }
    }

    private fun fetchData() {
        fetchGetProfileState()
        fetchUserCityState()

        fetchHomeState()
        fetchHomeFilteredByCityState()
    }

    // -------------------------------------- fetchData -------------------------------------- //

    private fun fetchGetProfileState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getProfileState.collect { state ->
                    when (state) {

                        is UiState.Success -> {
                            viewModelShared.profileData.value = UiState.Success(state.data!!)
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

    private fun fetchUserCityState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelShared.profileData.collect { state ->
                    when (state) {

                        is UiState.Success -> {
                            binding.inToolbar.tvSpinnerText.text = state.data?.cityName
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

    private fun fetchHomeState() {
        viewLifecycleOwner.lifecycleScope.launch {
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

    private fun fetchHomeFilteredByCityState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.homeFilteredByCityState.collect { state ->
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
        try {
            binding.apply {
                val topCarouselSliderList = parseTopCarouselSliderList(data.data?.sliders)
                setupCarouselSliderTop(topCarouselSliderList)

                // featuredProperties
                val featuredPropertiesList = data.data?.featuredPropertiesCountry!!.map { it!!.toPropertyModel() }
                setupFeaturedRealEstateRv(featuredPropertiesList)
                inFeaturedRealEstate.tvSeeMore.setOnClickListener {
                    findNavController().navigate(
                        R.id.seeMorePropertiesFragment, SeeMorePropertiesFragmentArgs(
                            featuredPropertiesList.toTypedArray(),
                            getString(R.string.featured_real_estate_in_egypt)
                        ).toBundle(),
                        navOptionsAnimation()
                    )
                }

                // featuredProjects
                val featuredProjectsList = data.data.featuredProjectsCountry!!.map { it!!.toProjectModel() }
                setupFeaturedProjectsRv(featuredProjectsList)
                inFeaturedProjects.tvSeeMore.setOnClickListener {
                    findNavController().navigate(
                        R.id.seeMoreProjectsFragment,
                        SeeMoreProjectsFragmentArgs(
                            featuredProjectsList.toTypedArray(),
                            getString(R.string.featured_projects_in_egypt)
                        ).toBundle(),
                        navOptionsAnimation()
                    )
                }

                setupFindPropertiesByCityRv(data.data.propertyInCity!!.map { it!!.toPropertiesByCityModel() })

                val betweenCarouselSliderList = parseBetweenCarouselSliderList(data.data.adds)
                setupSliderBetween(betweenCarouselSliderList)

                // newestProperties
                val newestPropertiesList = data.data.appaerments!!.map { it!!.toPropertyModel() }
                setupNewestPropertiesRv(newestPropertiesList)
                inNewestPropertiesLayout.tvSeeMore.setOnClickListener {
                    findNavController().navigate(
                        R.id.seeMorePropertiesFragment, SeeMorePropertiesFragmentArgs(
                            newestPropertiesList.toTypedArray(),
                            getString(R.string.featured_real_estate_in_egypt)
                        ).toBundle(),
                        navOptionsAnimation()
                    )
                }

                // newestVillas
                val newestVillasList = data.data.villas!!.map { it!!.toPropertyModel() }
                setupNewestVillasRv(newestVillasList)
                inNewestVillasLayout.tvSeeMore.setOnClickListener {
                    findNavController().navigate(
                        R.id.seeMorePropertiesFragment, SeeMorePropertiesFragmentArgs(
                            newestVillasList.toTypedArray(),
                            getString(R.string.featured_real_estate_in_egypt)
                        ).toBundle(),
                        navOptionsAnimation()
                    )
                }

                // newestDuplexes
                val newestDuplexesList = data.data.duplixes!!.map { it!!.toPropertyModel() }
                setupNewestDuplexesRv(newestDuplexesList)
                inNewestDuplexesLayout.tvSeeMore.setOnClickListener {
                    findNavController().navigate(
                        R.id.seeMorePropertiesFragment, SeeMorePropertiesFragmentArgs(
                            newestDuplexesList.toTypedArray(),
                            getString(R.string.featured_real_estate_in_egypt)
                        ).toBundle(),
                        navOptionsAnimation()
                    )
                }

                setupNewsRv(data.data.news!!.map { it!!.toNewsModel() })

                dismissShimmerEffect()
            }

        } catch (e: Exception) {
            dismissShimmerEffect()
            showToast(getString(R.string.invalid_data_parsing))
        }
        Log.e("time", System.currentTimeMillis().toString())
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

    private fun initToolbar() {
        binding.inToolbar.apply {
            toolbarIvMenu.setOnClickListener { viewModelShared.openDrawer.value = true }
            inNotification.root.setOnClickListener {
                findNavController().navigate(R.id.notificationFragment)
            }
        }
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

    private fun setupFeaturedProjectsRv(data: List<ProjectModel>) {
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

        Log.e("propertyId", model.id.toString())
        when (model.type) {
            PropertyType.FOR_SELL.key -> {
                findNavController().navigate(
                    R.id.propertyDetailsSellFragment,
                    PropertyDetailsSellFragmentArgs(model.id.toString()).toBundle(),
                    navOptionsAnimation()
                )
            }

            PropertyType.FOR_RENT.key -> {
                findNavController().navigate(
                    R.id.propertyDetailsRentFragment,
                    PropertyDetailsRentFragmentArgs(model.id.toString()).toBundle(),
                    navOptionsAnimation()
                )
            }

            PropertyType.FOR_BOTH.key -> {
                findNavController().navigate(
                    R.id.propertyDetailsSellAndRentFragment,
                    PropertyDetailsSellAndRentFragmentArgs(model.id.toString()).toBundle(),
                    navOptionsAnimation()
                )
            }
        }
    }

    override fun onFeaturedProjectClick(model: ProjectModel) {
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
        Log.e("propertyId", model.id.toString())
        when (model.type) {
            PropertyType.FOR_SELL.key -> {
                findNavController().navigate(
                    R.id.propertyDetailsSellFragment,
                    PropertyDetailsSellFragmentArgs(model.id.toString()).toBundle(),
                    navOptionsAnimation()
                )
            }

            PropertyType.FOR_RENT.key -> {
                findNavController().navigate(
                    R.id.propertyDetailsRentFragment,
                    PropertyDetailsRentFragmentArgs(model.id.toString()).toBundle(),
                    navOptionsAnimation()
                )
            }

            PropertyType.FOR_BOTH.key -> {
                findNavController().navigate(
                    R.id.propertyDetailsSellAndRentFragment,
                    PropertyDetailsSellAndRentFragmentArgs(model.id.toString()).toBundle(),
                    navOptionsAnimation()
                )
            }
        }
    }


    override fun onNewestVillaClick(model: PropertyModel) {
        Log.e("propertyId", model.id.toString())
        when (model.type) {
            PropertyType.FOR_SELL.key -> {
                findNavController().navigate(
                    R.id.propertyDetailsSellFragment,
                    PropertyDetailsSellFragmentArgs(model.id.toString()).toBundle(),
                    navOptionsAnimation()
                )
            }

            PropertyType.FOR_RENT.key -> {
                findNavController().navigate(
                    R.id.propertyDetailsRentFragment,
                    PropertyDetailsRentFragmentArgs(model.id.toString()).toBundle(),
                    navOptionsAnimation()
                )
            }

            PropertyType.FOR_BOTH.key -> {
                findNavController().navigate(
                    R.id.propertyDetailsSellAndRentFragment,
                    PropertyDetailsSellAndRentFragmentArgs(model.id.toString()).toBundle(),
                    navOptionsAnimation()
                )
            }
        }
    }


    override fun onNewestDuplexesClick(model: PropertyModel) {
        Log.e("propertyId", model.id.toString())
        when (model.type) {
            PropertyType.FOR_SELL.key -> {
                findNavController().navigate(
                    R.id.propertyDetailsSellFragment,
                    PropertyDetailsSellFragmentArgs(model.id.toString()).toBundle(),
                    navOptionsAnimation()
                )
            }

            PropertyType.FOR_RENT.key -> {
                findNavController().navigate(
                    R.id.propertyDetailsRentFragment,
                    PropertyDetailsRentFragmentArgs(model.id.toString()).toBundle(),
                    navOptionsAnimation()
                )
            }

            PropertyType.FOR_BOTH.key -> {
                findNavController().navigate(
                    R.id.propertyDetailsSellAndRentFragment,
                    PropertyDetailsSellAndRentFragmentArgs(model.id.toString()).toBundle(),
                    navOptionsAnimation()
                )
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
        viewModel.getHomeFilteredByCity(model.id.toString())
        binding.inToolbar.tvSpinnerText.text = model.name
    }

}