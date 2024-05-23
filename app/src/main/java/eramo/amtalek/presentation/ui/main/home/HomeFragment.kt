package eramo.amtalek.presentation.ui.main.home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
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
import eramo.amtalek.domain.model.main.home.NewsModelx
import eramo.amtalek.domain.model.main.home.ProjectModel
import eramo.amtalek.domain.model.main.home.PropertiesByCityModel
import eramo.amtalek.domain.model.main.home.PropertyModelx
import eramo.amtalek.presentation.adapters.recyclerview.RvMyFavouritesAdapter
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeFeaturedProjectsAdapter
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeFeaturedRealEstateAdapter
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeFindPropertyByCityAdapter
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeNewestDuplexesAdapter
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeNewestPropertiesAdapter
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeNewestVillasAdapter
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeNewsAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.dialog.FilterCitiesDialogFragment
import eramo.amtalek.presentation.ui.main.home.details.properties.PropertyDetailsSellAndRentFragmentArgs
import eramo.amtalek.presentation.ui.main.home.details.properties.PropertyDetailsSellFragmentArgs
import eramo.amtalek.presentation.ui.main.home.seemore.SeeMoreProjectsFragmentArgs
import eramo.amtalek.presentation.ui.main.home.seemore.SeeMorePropertiesByCityFragmentArgs
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.imaginativeworld.whynotimagecarousel.listener.CarouselListener
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import org.imaginativeworld.whynotimagecarousel.utils.setImage
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BindingFragment<FragmentHomeBinding>(),
    RvMyFavouritesAdapter.OnItemClickListener,
    RvHomeNewsAdapter.OnItemClickListener,
    RvHomeFeaturedRealEstateAdapter.OnItemClickListener,
    RvHomeFindPropertyByCityAdapter.OnItemClickListener,
    RvHomeNewestPropertiesAdapter.OnItemClickListener,
    RvHomeNewestVillasAdapter.OnItemClickListener,
    RvHomeNewestDuplexesAdapter.OnItemClickListener,
    RvHomeFeaturedProjectsAdapter.OnItemClickListener,
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
        if (UserUtil.isUserLogin()) {
            viewModel.getProfile(UserUtil.getUserType(), UserUtil.getUserId())
        }
        Log.e("token", UserUtil.getUserToken())
    }

    override fun onResume() {
        super.onResume()

        binding.inToolbar.FHomeEtSearch.text.clear()
//        binding.root.requestFocus()
        val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.inToolbar.FHomeEtSearch.windowToken, 0)

//        lifecycleScope.launch {
//            delay(1000)
//        binding.inToolbar.FHomeEtSearch.clearFocus()
//        }
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
//                findNavController().navigate(R.id.notificationFragment, null, navOptionsAnimation())
            }
            inToolbar.inMessaging.root.setOnClickListener {
//                findNavController().navigate(R.id.messagingFragment, null, navOptionsAnimation())
            }
        }

        setupSearchFunctionality()
        this@HomeFragment.onBackPressed { pressBackAgainToExist() }
    }

    private fun requestApis() {

    }

    private fun fetchData() {
        fetchGetProfileState()
        fetchUserCityState()
        fetchGetProfileState()

//        fetchHomeState()
//        fetchHomeFilteredByCityState()
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

                val citiesList = data.data.propertyInCity!!.map { it!!.toPropertiesByCityModel() }
                setupFindPropertiesByCityRv(citiesList)
                inPropertiesByCity.tvSeeMore.setOnClickListener {
                    findNavController().navigate(
                        R.id.seeMorePropertiesByCityFragment,
                        SeeMorePropertiesByCityFragmentArgs(citiesList.toTypedArray()).toBundle(),
                        navOptionsAnimation()
                    )
                }

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
                        this.root.setOnClickListener {
                            this@HomeFragment.binding.inToolbar.FHomeEtSearch.clearFocus()
                        }
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
                        this.root.setOnClickListener {
                            this@HomeFragment.binding.inToolbar.FHomeEtSearch.clearFocus()
                        }
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

    private fun setupFeaturedRealEstateRv(data: List<PropertyModelx>) {
        rvHomeFeaturedRealEstateAdapter.setListener(this@HomeFragment)
        binding.inFeaturedRealEstate.rv.adapter = rvHomeFeaturedRealEstateAdapter
        setupFeaturedRealEstateHeaderListener(data)
    }

    private fun setupFeaturedRealEstateHeaderListener(data: List<PropertyModelx>) {

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
    private fun setupNewestPropertiesRv(data: List<PropertyModelx>) {
        rvHomeNewestPropertiesAdapter.setListener(this@HomeFragment)
        binding.inNewestPropertiesLayout.rv.adapter = rvHomeNewestPropertiesAdapter
        setupNewestPropertiesHeaderListener(data)
    }

    private fun setupNewestPropertiesHeaderListener(data: List<PropertyModelx>) {

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

    private fun setupNewestVillasRv(data: List<PropertyModelx>) {
        rvHomeNewestVillasAdapter.setListener(this@HomeFragment)
        binding.inNewestVillasLayout.rv.adapter = rvHomeNewestVillasAdapter
        setupNewestVillasHeaderListener(data)
    }

    private fun setupNewestVillasHeaderListener(data: List<PropertyModelx>) {

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

    private fun setupNewestDuplexesRv(data: List<PropertyModelx>) {
        rvHomeNewestDuplexesAdapter.setListener(this@HomeFragment)
        binding.inNewestDuplexesLayout.rv.adapter = rvHomeNewestDuplexesAdapter
        rvHomeNewestDuplexesAdapter.submitList(data)
        setupNewestDuplexesHeaderListener(data)
    }

    private fun setupNewestDuplexesHeaderListener(data: List<PropertyModelx>) {

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

    private fun setupNewsRv(data: List<NewsModelx>) {
        rvHomeNewsAdapter.setListener(this@HomeFragment)
        binding.inNewsLayout.rv.adapter = rvHomeNewsAdapter
        rvHomeNewsAdapter.submitList(data)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupSearchFunctionality() {
        binding.inToolbar.apply {

            // Search action
            FHomeEtSearch.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val query = FHomeEtSearch.text.toString().trim()

                    if (query.isEmpty()) {
                        showToast(getString(R.string.enter_a_query))
                    } else {
//                        findNavController().navigate(
//                            R.id.searchPropertyResultFragment,
//                            null,
//                            navOptionsFromTopAnimation()
//                        )
                    }

                } else if (actionId == EditorInfo.IME_ACTION_DONE) {
                    viewLifecycleOwner.lifecycleScope.launch {
                        delay(500)
                        FHomeEtSearch.clearFocus()
                    }
                }
                true
            }

            // onTextChange listener
//            FHomeEtSearch.addTextChangedListener(object : TextWatcher {
//                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//                }
//
//                override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
//                    if (charSequence?.isNotEmpty() == true) {
//                        tvSearch.visibility = View.VISIBLE
//                    } else {
//                        tvSearch.visibility = View.GONE
//                    }
//                }
//
//                override fun afterTextChanged(s: Editable?) {
//
//                }
//            })

            // onFocus listener
            FHomeEtSearch.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    tvSearch.visibility = View.VISIBLE
                } else {
                    tvSearch.visibility = View.GONE
                }
            }

            // tvSearch listener
            tvSearch.setOnClickListener {
                findNavController().navigate(
                    R.id.searchPropertyResultFragment,
                    null,
                    navOptionsFromTopAnimation()
                )
            }
        }

        // remove focus onOutsideClick
        binding.root.setOnTouchListener { _, _ ->
            binding.inToolbar.FHomeEtSearch.clearFocus()
            false
        }

        binding.scrollView.setOnTouchListener { _, _ ->
            binding.inToolbar.FHomeEtSearch.clearFocus()
            false
        }
    }

    private fun isTouchInsideView(event: MotionEvent, view: View): Boolean {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val x = event.rawX
        val y = event.rawY
        return x > location[0] && x < location[0] + view.width && y > location[1] && y < location[1] + view.height
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

    override fun onFeaturedRealEstateClick(model: PropertyModelx) {

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
//                findNavController().navigate(
//                    R.id.propertyDetailsRentFragment,
//                    PropertyDetailsRentFragmentArgs(model.id.toString()).toBundle(),
//                    navOptionsAnimation()
//                )
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

    override fun onPropertyByCityClick(model: PropertiesByCityModel) {

    }

    override fun onNewestPropertyClick(model: PropertyModelx) {
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
//                findNavController().navigate(
//                    R.id.propertyDetailsRentFragment,
//                    PropertyDetailsRentFragmentArgs(model.id.toString()).toBundle(),
//                    navOptionsAnimation()
//                )
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

    override fun onNewestVillaClick(model: PropertyModelx) {
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
//                findNavController().navigate(
//                    R.id.propertyDetailsRentFragment,
//                    PropertyDetailsRentFragmentArgs(model.id.toString()).toBundle(),
//                    navOptionsAnimation()
//                )
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

    override fun onNewestDuplexesClick(model: PropertyModelx) {
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
//                findNavController().navigate(
//                    R.id.propertyDetailsRentFragment,
//                    PropertyDetailsRentFragmentArgs(model.id.toString()).toBundle(),
//                    navOptionsAnimation()
//                )
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

    override fun onPropertyClick(model: eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel) {
    }

    override fun onNewsClick(model: NewsModelx) {
        findNavController().navigate(R.id.newsDetailsFragment, null, navOptionsAnimation())
    }

    override fun onFilterCitiesDialogItemClick(model: CityModel) {
        viewModel.getHomeFilteredByCity(model.id.toString())
        binding.inToolbar.tvSpinnerText.text = model.name
    }

}