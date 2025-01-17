package eramo.amtalek.presentation.ui.main.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.data.remote.dto.property.newResponse.poperty_types.Data
import eramo.amtalek.databinding.FragmentHomeBinding
import eramo.amtalek.databinding.ItemSliderTopBinding
import eramo.amtalek.domain.model.drawer.myfavourites.ProjectModel
import eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel
import eramo.amtalek.domain.model.home.HomeExtraSectionsModel
import eramo.amtalek.domain.model.home.cities.CitiesModel
import eramo.amtalek.domain.model.home.news.NewsModel
import eramo.amtalek.domain.model.home.slider.SliderModel
import eramo.amtalek.domain.model.project.AmenityModel
import eramo.amtalek.domain.model.property.CriteriaModel
import eramo.amtalek.domain.search.SearchDataListsModel
import eramo.amtalek.domain.search.SearchModelDto
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeFeaturedProjectsAdapter
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeFeaturedRealEstateAdapter
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeFifthExtraSectionAdapter
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeFindPropertyByCityAdapter
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeThirdExtraSectionAdapter
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeFirstExtraSectionAdapter
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeFourthExtraSectionAdapter
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeMostViewedPropertiesAdapter
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeSecondExtraSectionAdapter
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeNewsAdapter
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeNormalPropertiesAdapter
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeSearchByPropertyTypeAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
import eramo.amtalek.presentation.ui.interfaces.FavClickListener
import eramo.amtalek.presentation.ui.main.home.details.NewsDetailsFragmentArgs
import eramo.amtalek.presentation.ui.main.home.details.projects.MyProjectDetailsFragmentArgs
import eramo.amtalek.presentation.ui.main.home.details.properties.PropertyDetailsFragmentArgs
import eramo.amtalek.presentation.ui.search.searchform.SearchFormViewModel
import eramo.amtalek.presentation.ui.search.searchresult.SearchResultFragmentArgs
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.util.LocalUtil
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.navOptionsAnimation
import eramo.amtalek.util.navOptionsFromBottomAnimation
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
    RvHomeFeaturedRealEstateAdapter.OnItemClickListener,
    RvHomeFeaturedProjectsAdapter.OnItemClickListener,
    RvHomeFindPropertyByCityAdapter.OnItemClickListener,
    RvHomeNormalPropertiesAdapter.OnItemClickListenerNormalProperties,
    RvHomeMostViewedPropertiesAdapter.OnItemClickListenerMostViewedProperties,
    RvHomeFirstExtraSectionAdapter.OnItemClickListenerFirstSection,
    RvHomeSecondExtraSectionAdapter.OnItemClickListenerSecondSection,
    RvHomeThirdExtraSectionAdapter.OnItemClickListenerThirdSection,
    RvHomeFourthExtraSectionAdapter.OnItemClickListenerFourthSection,
    RvHomeFifthExtraSectionAdapter.OnItemClickListenerFifthSection,
    RvHomeNewsAdapter.OnItemClickListener,
    RvHomeSearchByPropertyTypeAdapter.OnItemClickListener,
    FavClickListener {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentHomeBinding::inflate

    private val viewModel by viewModels<HomeMyViewModel>()

    private val viewModelShared: SharedViewModel by activityViewModels()

    private val searchViewModel by viewModels<SearchFormViewModel>()
    private fun createModel(model: CitiesModel): SearchModelDto {
        binding.apply {
            val searchKeyWords = ""
            val bedrooms = ""
            val bathrooms = ""
            val minPrice = ""
            val maxPrice = ""
            val minArea = ""
            val maxArea = ""
            val locationId = ""
            val locationName = ""
            val purposeId = ""
            val finishingId = ""
            val typeId = ""
            val currencyId = 0


            val myModel = SearchModelDto(
                searchKeyWords = searchKeyWords,
                locationId = locationId,
                locationName = locationName,
                currencyId = currencyId,
                bathroomsNumber = bathrooms,
                bedroomsNumber = bedrooms,
                propertyTypeId = typeId,
                propertyFinishingId = finishingId,
                minPrice = minPrice,
                maxPrice = maxPrice,
                minArea = minArea,
                maxArea = maxArea,
                purposeId = purposeId,
                priceArrangeKeys = "asc",
                amenitiesListIds = "",
                city = model.id,
                priority_keys = ""
            )
            return myModel
        }
    }

    private var listOfPurposeItems = ArrayList<CriteriaModel>()
    private var listOfFinishingItems = ArrayList<CriteriaModel>()
    private var listOfTypeItems = ArrayList<CriteriaModel>()
    private var listOfCurrencyItems = ArrayList<CriteriaModel>()
    private var listOfAmenitiesItems = ArrayList<AmenityModel>()



    private var backPressedTime: Long = 0
    // ---------------------------------------------------------------------------

    @Inject
    lateinit var rvHomeFeaturedRealEstateAdapter: RvHomeFeaturedRealEstateAdapter

    @Inject
    lateinit var rvHomeSearchByPropertyTypeAdapter: RvHomeSearchByPropertyTypeAdapter

    @Inject
    lateinit var rvHomeFeaturedProjectsAdapter: RvHomeFeaturedProjectsAdapter

    @Inject
    lateinit var rvHomeFindPropertyByCityAdapter: RvHomeFindPropertyByCityAdapter

    @Inject
    lateinit var rvHomeNormalPropertiesAdapter: RvHomeNormalPropertiesAdapter

    @Inject
    lateinit var rvHomeMostViewedPropertiesAdapter: RvHomeMostViewedPropertiesAdapter

    // ---------------------------------------------------------------------------
    @Inject
    lateinit var rvHomeFirstExtraSectionAdapter: RvHomeFirstExtraSectionAdapter

    @Inject
    lateinit var rvHomeSecondExtraSectionAdapter: RvHomeSecondExtraSectionAdapter

    @Inject
    lateinit var rvHomeThirdExtraSectionAdapter: RvHomeThirdExtraSectionAdapter

    @Inject
    lateinit var rvHomeFourthExtraSectionAdapter: RvHomeFourthExtraSectionAdapter

    @Inject
    lateinit var rvHomeFifthExtraSectionAdapter: RvHomeFifthExtraSectionAdapter
    // ---------------------------------------------------------------------------

    @Inject
    lateinit var rvHomeNewsAdapter: RvHomeNewsAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        listeners()
        requestApis()
        fetchData()
        handleRefresh()
//        if (UserUtil.isUserLogin()) {
//            viewModel.getProfile(UserUtil.getUserType(), UserUtil.getUserId())
//        }
        searchViewModel.getInitApis()


    }

    private fun createListsModel(): SearchDataListsModel {
        val data = SearchDataListsModel(
            listOfTypesItems = listOfTypeItems,
            listOfCurrencyItems = listOfCurrencyItems,
            listOfFinishingItems = listOfFinishingItems,
            listOfPurposeItems = listOfPurposeItems,
            listOfAmenitiesItems = listOfAmenitiesItems
        )
        return data
    }
    private fun createModelForNormalProperties(): SearchModelDto {
        binding.apply {
            val searchKeyWords = ""
            val bedrooms = ""
            val bathrooms = ""
            val minPrice = ""
            val maxPrice = ""
            val minArea = ""
            val maxArea = ""
            val locationId = ""
            val locationName = ""
            val purposeId = ""
            val finishingId = ""
            val typeId = ""
            val currencyId = 0



            val myModel = SearchModelDto(
                searchKeyWords = searchKeyWords,
                locationId = locationId,
                locationName = locationName,
                currencyId = currencyId,
                bathroomsNumber = bathrooms,
                bedroomsNumber = bedrooms,
                propertyTypeId = "",
                propertyFinishingId = finishingId,
                minPrice = minPrice,
                maxPrice = maxPrice,
                minArea = minArea,
                maxArea = maxArea,
                purposeId = purposeId,
                priceArrangeKeys = "asc",
                amenitiesListIds = "",
                city = 0,
                priority_keys = "normal"
            )
            return myModel
        }
    }
    private fun createModelForFeaturedProperties(): SearchModelDto {
        binding.apply {
            val searchKeyWords = ""
            val bedrooms = ""
            val bathrooms = ""
            val minPrice = ""
            val maxPrice = ""
            val minArea = ""
            val maxArea = ""
            val locationId = ""
            val locationName = ""
            val purposeId = ""
            val finishingId = ""
            val typeId = ""
            val currencyId = 0



            val myModel = SearchModelDto(
                searchKeyWords = searchKeyWords,
                locationId = locationId,
                locationName = locationName,
                currencyId = currencyId,
                bathroomsNumber = bathrooms,
                bedroomsNumber = bedrooms,
                propertyTypeId = typeId.toString(),
                propertyFinishingId = finishingId,
                minPrice = minPrice,
                maxPrice = maxPrice,
                minArea = minArea,
                maxArea = maxArea,
                purposeId = purposeId,
                priceArrangeKeys = "asc",
                amenitiesListIds = "",
                city = 0,
                priority_keys = "featured"
            )
            return myModel
        }
    }
    private fun createModelSearchByTypeProperties(model: CriteriaModel): SearchModelDto {
        binding.apply {
            val searchKeyWords = ""
            val bedrooms = ""
            val bathrooms = ""
            val minPrice = ""
            val maxPrice = ""
            val minArea = ""
            val maxArea = ""
            val locationId = ""
            val locationName = ""
            val purposeId = ""
            val finishingId = ""
            val typeId = model.id.toString()
            val currencyId = 0



            val myModel = SearchModelDto(
                searchKeyWords = searchKeyWords,
                locationId = locationId,
                locationName = locationName,
                currencyId = currencyId,
                bathroomsNumber = bathrooms,
                bedroomsNumber = bedrooms,
                propertyTypeId = typeId,
                propertyFinishingId = finishingId,
                minPrice = minPrice,
                maxPrice = maxPrice,
                minArea = minArea,
                maxArea = maxArea,
                purposeId = purposeId,
                priceArrangeKeys = "asc",
                amenitiesListIds = "",
                city = 0,
                priority_keys = ""
            )
            return myModel
        }
    }

    private fun createModelForSections(model: HomeExtraSectionsModel): SearchModelDto {
        binding.apply {
            val searchKeyWords = ""
            val bedrooms = ""
            val bathrooms = ""
            val minPrice = ""
            val maxPrice = ""
            val minArea = ""
            val maxArea = ""
            val locationId = ""
            val locationName = ""
            val purposeId = ""
            val finishingId = ""
            val typeId = model.propertyTypeId
            val currencyId = 0

            val myModel = SearchModelDto(
                searchKeyWords = searchKeyWords,
                locationId = locationId,
                locationName = locationName,
                currencyId = currencyId,
                bathroomsNumber = bathrooms,
                bedroomsNumber = bedrooms,
                propertyTypeId = typeId.toString(),
                propertyFinishingId = finishingId,
                minPrice = minPrice,
                maxPrice = maxPrice,
                minArea = minArea,
                maxArea = maxArea,
                purposeId = purposeId,
                priceArrangeKeys = "asc",
                amenitiesListIds = "",
                city = 0,
                priority_keys = ""
            )
            return myModel
        }
    }
    private fun handleRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            requestApis()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onResume() {
        super.onResume()

        binding.inToolbar.FHomeEtSearch.text.clear()
        binding.root.requestFocus()
        val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.inToolbar.FHomeEtSearch.windowToken, 0)

        lifecycleScope.launch {
            delay(1000)
            binding.inToolbar.FHomeEtSearch.clearFocus()
        }
    }

    private fun setupViews() {
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        initToolbar()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun listeners() {
        //  setupListeners(viewModel.homeSliderState.value.data?.get(0)?.url.toString())

        binding.apply {
            inToolbar.spinnerLayout.setOnClickListener {
                findNavController().navigate(R.id.filterCitiesDialogFragment, null, navOptionsAnimation())
            }
            inToolbar.FHomeEtSearch.setOnClickListener() {
                findNavController().navigate(R.id.searchFormFragment, null, navOptionsFromTopAnimation())

            }
            inToolbar.inNotification.root.setOnClickListener {
                if (UserUtil.isUserLogin()) {
                    findNavController().navigate(R.id.notificationFragment, null, navOptionsAnimation())
                } else {
                    findNavController().navigate(R.id.loginDialog, null, navOptionsAnimation())
                }

            }
            inToolbar.inMessaging.root.setOnClickListener {
                if (UserUtil.isUserLogin()) {
                    findNavController().navigate(R.id.messagingChatFragment, null, navOptionsAnimation())
                } else {
                    findNavController().navigate(R.id.loginDialog, null, navOptionsAnimation())
                }

            }
        }

//        setupSearchFunctionality()
        this@HomeFragment.onBackPressed { pressBackAgainToExist() }
    }

    private fun requestApis() {
        viewModel.getHomeApis(UserUtil.getUserCountryFiltrationTitleId(), UserUtil.getUserCityFiltrationTitleId())
    }

    private fun fetchData() {
        fetchUserCityState()
        fetchGetHomeApis()
        fetchGetHomeFeaturedProperties()
        fetchGetHomeProjects()
        fetchGetHomeFilterByCity()
        fetchGetHomeSlider()
        fetchClickedAds()
        fetchGetHomeMostViewedProperties()
        fetchHomeNormalProperties()
        fetchGetHomeExtraSections()
        fetchGetNews()
        //fetchAddRemoveToFavState()
        fetchGetPropertyTypes()
        fetchGetPropertyFinishing()
        fetchGetAmenitiesState()
        fetchGetPropertyPurpose()
        fetchCurrencies()
    }


    // -------------------------------------- fetchData -------------------------------------- //

    private fun fetchGetPropertyPurpose(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                searchViewModel.propertyPurposeState.collect(){
                    when(it){
                        is UiState.Loading->{
                            LoadingDialog.showDialog()
                        }
                        is UiState.Success->{
                            val types = it.data
                            if (types != null) {
                                listOfPurposeItems.clear()
                                listOfPurposeItems.add(0, CriteriaModel(-1, -1, getString(R.string.purpose), image = ""))
                                for (item in types) {
                                    listOfPurposeItems.add(item as CriteriaModel)
                                }

//                                listOfPurposeItems = types as ArrayList<CriteriaModel>
                            }
                            LoadingDialog.dismissDialog()
                        }
                        is UiState.Error->{

                            LoadingDialog.dismissDialog()
                        }
                        is UiState.Empty->Unit
                    }
                }
            }
        }
    }
    private fun fetchGetPropertyFinishing() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchViewModel.propertyFinishingState.collect { it ->
                    when (it) {
                        is UiState.Loading -> {
                            LoadingDialog.showDialog()
                        }

                        is UiState.Success -> {
                            val types = it.data
                            if (types != null) {
                                // Clear the existing list
                                listOfFinishingItems.clear()

                                // Add a default item at the beginning
                                listOfFinishingItems.add(0, CriteriaModel(-1, -1, getString(R.string.finishing), image = ""))

                                // Add the retrieved items to the list
                                for (item in types) {
                                    listOfFinishingItems.add(item as CriteriaModel)
                                }

                            }
                            LoadingDialog.dismissDialog()
                        }

                        is UiState.Error -> {
                            LoadingDialog.dismissDialog()
                        }

                        is UiState.Empty -> Unit
                    }
                }
            }
        }
    }

    private fun fetchCurrencies() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchViewModel.currenciesState.collect() {
                    when (it) {
                        is UiState.Loading -> {
                            LoadingDialog.showDialog()
                        }

                        is UiState.Success -> {
                            val currencies = it.data
                            Log.e("ahh", currencies.toString())
                            if (currencies != null) {
                                listOfCurrencyItems.clear()
                                listOfCurrencyItems.add(0, CriteriaModel(-1, -1, getString(R.string.currency), image = ""))
                                for (item in currencies) {
                                    listOfCurrencyItems.add(item as CriteriaModel)
                                }
                                //  listOfCurrencyItems = it.data as ArrayList<CriteriaModel>

                                // setupCurrenciesSpinner(currencies.toMutableList())
                            }
                            LoadingDialog.dismissDialog()
                        }

                        is UiState.Error -> {
                            LoadingDialog.dismissDialog()
                        }

                        is UiState.Empty -> Unit
                    }
                }
            }
        }
    }
    private fun fetchGetPropertyTypes() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchViewModel.propertyTypesState.collect() {
                    when (it) {
                        is UiState.Loading -> {
                            LoadingDialog.showDialog()
                        }

                        is UiState.Success -> {
                            val types = it.data
                            if (types != null) {
                                listOfTypeItems.clear()
                                listOfTypeItems.add(0, CriteriaModel(-1, -1, getString(R.string.typee), image = ""))
                                for (item in types) {
                                    listOfTypeItems.add(item as CriteriaModel)
                                }
                                setupSearchFunctionality(data = it.data)
                                // listOfTypeItems = it.data as ArrayList<CriteriaModel>
                                // setupTypesSpinner(types.toMutableList())
                            }
                            LoadingDialog.dismissDialog()
                        }

                        is UiState.Error -> {

                            LoadingDialog.dismissDialog()
                        }

                        is UiState.Empty -> Unit
                    }
                }
            }
        }

    }
    private fun fetchGetAmenitiesState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchViewModel.propertyAmenitiesState.collect() {
                    when (it) {
                        is UiState.Success -> {
                            //  amenitiesAdapter.saveData(it.data!!)

                            listOfAmenitiesItems = it.data as ArrayList<AmenityModel>

//                            binding.amenitiesRv.setHasFixedSize(true)
//                            binding.amenitiesRv.adapter = amenitiesAdapter
                            LoadingDialog.dismissDialog()
                        }

                        is UiState.Error -> {
                            Toast.makeText(
                                requireContext(),
                                it.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                            LoadingDialog.dismissDialog()
                        }

                        is UiState.Loading -> {
                            LoadingDialog.showDialog()
                        }

                        is UiState.Empty -> Unit
                    }
                }
            }
        }
    }
    private fun fetchGetHomeApis() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.initScreenState.collect() { state ->
                    when (state) {
                        is UiState.Success -> {
                            dismissShimmerEffect()
                        }

                        is UiState.Loading -> {
                            showShimmerEffect()
                        }

                        is UiState.Error -> {
                            dismissShimmerEffect()
                            val errorMessage = state.message!!.asString(requireContext())
                        }

                        is UiState.Empty -> Unit
                    }
                }
            }
        }
    }

    private fun fetchAddRemoveToFavState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.favState.collect() { state ->
                    when (state) {

                        is UiState.Success -> {
                            viewModel.getHomeApis("1", "1")
                            showToast(state.data?.message.toString())
                            LoadingDialog.dismissDialog()

                            //showToast(state.data?.message.toString())
                        }

                        is UiState.Error -> {
                            val errorMessage = state.message!!.asString(requireContext())
                            showToast(errorMessage)
                            LoadingDialog.dismissDialog()
                        }

                        is UiState.Loading -> {

                        }

                        else -> {}
                    }
                }
            }
        }
    }

    private fun fetchGetHomeFeaturedProperties() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.homeFeaturedPropertiesState.collect { state ->
                    when (state) {
                        is UiState.Success -> {
                            val data = state.data?.get(0)?.propertiesList
                            if (state.data?.get(0)?.title == "Featured Properties in in Egypt") {
                                binding.inFeaturedRealEstate.tvTitle.text = "Featured Properties in Egypt"
                            }else{
                                binding.inFeaturedRealEstate.tvTitle.text = state.data?.get(0)?.title
                            }
                            if (!data.isNullOrEmpty()) {
                                setupFeaturedRealEstateRv(data)
                            } else {
                                binding.inFeaturedRealEstate.root.visibility = View.GONE
                            }
                        }

                        is UiState.Error -> {
                            val errorMessage = state.message!!.asString(requireContext())
                            showToast(errorMessage)
                        }

                        is UiState.Loading -> {
                            // You can show a loading indicator here if needed
                        }

                        else -> {
                            // Handle any other unexpected states or do nothing
                        }
                    }
                }
            }
        }
    }


    private fun fetchGetHomeProjects() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.homeProjectsState.collect() { state ->
                    when (state) {
                        is UiState.Success -> {
                            val data = state.data?.get(0)?.projects
                            binding.inFeaturedProjects.tvTitle.text = state.data?.get(0)?.title
                            if (!data.isNullOrEmpty()) {
                                setupFeaturedProjectsRv(data)
                            } else {
                                binding.inFeaturedProjects.root.visibility = View.GONE
                            }
                        }

                        is UiState.Error -> {
                            val errorMessage = state.message!!.asString(requireContext())
                            showToast(errorMessage)
                        }

                        is UiState.Loading -> {
                        }

                        else -> {}
                    }

                }
            }
        }
    }

    private fun fetchGetHomeFilterByCity() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.homeFilterByCityState.collect() { state ->
                    when (state) {
                        is UiState.Success -> {
                            val data = state.data?.get(0)?.cities
                            binding.inPropertiesByCity.tvTitle.text = state.data?.get(0)?.title
                            if (!data.isNullOrEmpty()) {
                                setupFindPropertiesByCityRv(data)
                            } else {
                                binding.inPropertiesByCity.root.visibility = View.GONE
                            }
                        }

                        is UiState.Error -> {
                            val errorMessage = state.message!!.asString(requireContext())
                            showToast(errorMessage)
                        }

                        is UiState.Loading -> {
                        }

                        else -> {}
                    }

                }
            }
        }
    }

    private fun fetchClickedAds() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.clickOnAd.collect() { state ->
                    when (state) {
                        is UiState.Success -> {
                            val data = state.data
                            Log.e("ads", data.toString())
                        }

                        is UiState.Error -> {
                            val errorMessage = state.message!!.asString(requireContext())
                            showToast(errorMessage)
                        }

                        is UiState.Loading -> {
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    private fun fetchGetHomeSlider() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.homeSliderState.collect { state ->
                    when (state) {
                        is UiState.Success -> {
                            val dataState = state.data
                            if (!dataState.isNullOrEmpty()) {
                                binding.carouselSliderBetween.visibility = View.VISIBLE
                                binding.carouselSliderBetweenDots.visibility = View.VISIBLE
                                setupSliderBetween(parseBetweenCarouselSliderList(dataState))
                                // setupListeners(dataState[0].url)

                                Log.e("images ", dataState.toString())
                                Log.e("url ", dataState[0].url)

                            } else {
                                binding.carouselSliderBetween.visibility = View.GONE
                                binding.carouselSliderBetweenDots.visibility = View.GONE
                            }
                        }

                        is UiState.Error -> {
                            val errorMessage = state.message!!.asString(requireContext())
                            showToast(errorMessage)
                        }

                        is UiState.Loading -> {
                        }

                        else -> {}
                    }
                }
            }
        }
    }


    private fun fetchHomeNormalProperties() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.homeNormalPropertiesState.collect() { state ->
                    when (state) {
                        is UiState.Success -> {
                            val data = state.data?.get(0)?.propertiesList
                            binding.inNormalPropertiesLayout.tvTitle.text = state.data?.get(0)?.title
                            if (!data.isNullOrEmpty()) {
                                setupNormalPropertiesRv(data)
                            } else {
                                binding.inNormalPropertiesLayout.root.visibility = View.GONE
                            }
                        }

                        is UiState.Error -> {
                            val errorMessage = state.message!!.asString(requireContext())
                            showToast(errorMessage)
                        }

                        is UiState.Loading -> {
                        }

                        else -> {}
                    }

                }
            }
        }
    }

    private fun fetchGetHomeMostViewedProperties() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.homeMostViewedPropertiesState.collect() { state ->
                    when (state) {
                        is UiState.Success -> {
                            val data = state.data?.get(0)?.propertiesList
                            binding.inMostViewedPropertiesLayout.tvTitle.text = state.data?.get(0)?.title
                            if (!data.isNullOrEmpty()) {
                                setupMostViewedPropertiesRv(data)
                            } else {
                                binding.inMostViewedPropertiesLayout.root.visibility = View.GONE
                            }
                        }

                        is UiState.Error -> {
                            val errorMessage = state.message!!.asString(requireContext())
                            showToast(errorMessage)
                        }

                        is UiState.Loading -> {
                        }

                        else -> {}
                    }

                }
            }
        }
    }


    private fun fetchGetHomeExtraSections() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.homeExtraSectionsState.collect() { state ->
                    when (state) {
                        is UiState.Success -> {
                            val data = state.data
                            setupExtraViews(data)
                        }

                        is UiState.Error -> {
                            val errorMessage = state.message!!.asString(requireContext())
                            showToast(errorMessage)
                        }

                        is UiState.Loading -> {
                        }

                        else -> {}
                    }

                }
            }
        }
    }

    private fun fetchGetNews() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.homeNewsState.collect() { state ->
                    when (state) {

                        is UiState.Success -> {
                            val data = state.data?.get(0)?.news
                            binding.inNewsLayout.tvTitle.text = state.data?.get(0)?.title
                            if (!data.isNullOrEmpty()) {
                                setupNewsRv(data)
                            } else {
                                binding.inNewsLayout.root.visibility = View.GONE
                            }

                        }

                        is UiState.Error -> {


                            val errorMessage = state.message!!.asString(requireContext())
                            showToast(errorMessage)
                        }

                        is UiState.Loading -> {
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

    //------------------------------------------------------------------------------------------//
    private lateinit var sliderModels: List<SliderModel>
    private lateinit var carouselItems: List<CarouselItem>

    private fun parseBetweenCarouselSliderList(data: List<SliderModel>?): ArrayList<CarouselItem> {
        val list = ArrayList<CarouselItem>()
        sliderModels = data ?: emptyList()

        val headers = mutableMapOf<String, String>()
        headers["header_key"] = "header_value"

        for (i in sliderModels) {
            list.add(
                CarouselItem(
                    imageUrl = i.image,
                    headers = headers
                )
            )
        }
        carouselItems = list
        return list
    }


    private fun setupSliderBetween(dataState: ArrayList<CarouselItem>) {
        binding.apply {
            carouselSliderBetween.registerLifecycle(viewLifecycleOwner.lifecycle)
            carouselSliderBetween.setData(dataState)

            if (dataState.size == 1) {
                carouselSliderBetween.infiniteCarousel = false
                carouselSliderBetween.autoPlay = false
                carouselSliderBetween.imageScaleType = ImageView.ScaleType.CENTER_INSIDE
            } else {
                carouselSliderBetween.infiniteCarousel = true
                carouselSliderBetween.autoPlay = true
                carouselSliderBetween.imageScaleType = ImageView.ScaleType.CENTER_INSIDE
            }

            carouselSliderBetween.carouselListener = object : CarouselListener {
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
                    val sliderModel = sliderModels[position]

                    currentBinding.apply {
                        imageView13.setImage(item)
                        imageView13.scaleType = ImageView.ScaleType.CENTER_CROP

                        // Access the SliderModel variables here
                        val id = sliderModel.id
                        val imageUrl = sliderModel.image
                        val type = sliderModel.type
                        val inFrame = sliderModel.inFrame
                        val url = sliderModel.url

                        // Use these variables as needed
                        this.root.setOnClickListener {
                            if (url.isNotEmpty()) {
                                setupListeners(url)
                                viewModel.clickedOnAd(id.toString())
                            }
                        }

                        this@HomeFragment.binding.inToolbar.FHomeEtSearch.clearFocus()
                    }
                }
            }
        }
    }

    private fun setupListeners(url: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
        startActivity(intent)
    }


//    private fun setupListeners(url: String) {
//        binding.carouselSliderBetween.setOnClickListener {
//            // The URL you want to open
//            val urls = url
//
//            // Create an Intent to open the web browser
//            val intent = Intent(Intent.ACTION_VIEW).apply {
//                data = Uri.parse(urls)
//            }
//
//            // Start the Intent
//            startActivity(intent)
//        }
//
//    }


    private fun initToolbar() {
        binding.inToolbar.apply {
            toolbarIvMenu.setOnClickListener { viewModelShared.openDrawer.value = true }
            inNotification.root.setOnClickListener {
                findNavController().navigate(R.id.notificationFragment)
            }

            if (UserUtil.getUserType() == "broker") {
                inMessaging.root.visibility = View.GONE
                inNotification.root.visibility = View.GONE

            } else {
                inMessaging.root.visibility = View.VISIBLE
                inNotification.root.visibility = View.VISIBLE
            }

        }


        if (LocalUtil.isEnglish()) {
            if (UserUtil.getCityFiltrationTitleEn() == "") {
                binding.inToolbar.tvSpinnerText.text = getString(R.string.egypt)
            } else {
                binding.inToolbar.tvSpinnerText.text = UserUtil.getCityFiltrationTitleEn()
            }
        } else {
            if (UserUtil.getCityFiltrationTitleAr() == "") {
                binding.inToolbar.tvSpinnerText.text = getString(R.string.egypt)

            } else {
                binding.inToolbar.tvSpinnerText.text = UserUtil.getCityFiltrationTitleAr()

            }
        }
        if (LocalUtil.isEnglish()) {
            binding.inToolbar.toolbarIvLogo.setImageDrawable(context?.getDrawable(R.drawable.top_logo_en))

        } else {
            binding.inToolbar.toolbarIvLogo.setImageDrawable(context?.getDrawable(R.drawable.top_logo_ar))
        }

    }
    private fun setupSearchFunctionality(data: List<CriteriaModel>) {
        rvHomeSearchByPropertyTypeAdapter.setListener(this@HomeFragment)
        binding.inSearchByPropertyType.rv.adapter = rvHomeSearchByPropertyTypeAdapter
        rvHomeSearchByPropertyTypeAdapter.submitList(data)
        binding.inSearchByPropertyType.tvTitle.text = getString(R.string.search_by_property_type)
        binding.inSearchByPropertyType.tvTitle.setTypeface(binding.inSearchByPropertyType.tvTitle.typeface, Typeface.BOLD)
    }

    private fun setupFeaturedRealEstateRv(data: List<PropertyModel>) {
        rvHomeFeaturedRealEstateAdapter.setListener(this@HomeFragment, this@HomeFragment)
        binding.inFeaturedRealEstate.rv.adapter = rvHomeFeaturedRealEstateAdapter
        rvHomeFeaturedRealEstateAdapter.submitList(data)
        binding.inFeaturedRealEstate.tvTitle.setTypeface(binding.inFeaturedRealEstate.tvTitle.typeface, Typeface.BOLD)


        //       binding.inFeaturedRealEstate.root.startAnimation(AnimationUtils.loadAnimation(context,R.anim.anim_swipe_slow))
        binding.inFeaturedRealEstate.tvSeeMore.setOnClickListener {
//            Log.e("a7a", createModelForFeaturedProperties().toString())
            findNavController().navigate(
                R.id.searchResultFragment,
                data?.get(0)?.let { createModelForFeaturedProperties() }?.let { it2 ->
                    SearchResultFragmentArgs(
                        it2, createListsModel()
                    ).toBundle()
                }, navOptionsAnimation()
            )
        }

    }


    private fun setupFeaturedProjectsRv(data: List<ProjectModel>) {
        rvHomeFeaturedProjectsAdapter.setListener(this@HomeFragment)
        binding.inFeaturedProjects.rv.adapter = rvHomeFeaturedProjectsAdapter
        rvHomeFeaturedProjectsAdapter.submitList(data)
        binding.inFeaturedProjects.tvTitle.setTypeface(binding.inFeaturedProjects.tvTitle.typeface, Typeface.BOLD)

//        binding.inFeaturedProjects.root.startAnimation(AnimationUtils.loadAnimation(context,R.anim.anim_swipe_slow))
        binding.inFeaturedProjects.tvSeeMore.setOnClickListener {
            findNavController().navigate(R.id.seeMoreProjectsFragment)
        }
    }


    private fun setupFindPropertiesByCityRv(data: List<CitiesModel>) {
        rvHomeFindPropertyByCityAdapter.setListener(this@HomeFragment)
        binding.inPropertiesByCity.rv.adapter = rvHomeFindPropertyByCityAdapter
        rvHomeFindPropertyByCityAdapter.submitList(data)
        binding.inPropertiesByCity.tvTitle.text = getString(R.string.find_your_property_in_the_city)
        binding.inPropertiesByCity.tvTitle.setTypeface(binding.inPropertiesByCity.tvTitle.typeface, Typeface.BOLD)

//        binding.inPropertiesByCity.root.startAnimation(AnimationUtils.loadAnimation(context,R.anim.anim_swipe_slow))
        binding.inPropertiesByCity.tvSeeMore.setOnClickListener {
            findNavController().navigate(R.id.seeMorePropertiesByCityFragment)
        }
    }

    private fun setupNormalPropertiesRv(data: List<PropertyModel>) {
        rvHomeNormalPropertiesAdapter.setListener(this@HomeFragment, this@HomeFragment)
        binding.inNormalPropertiesLayout.rv.adapter = rvHomeNormalPropertiesAdapter
        rvHomeNormalPropertiesAdapter.submitList(data)
        binding.inNormalPropertiesLayout.tvTitle.setTypeface(binding.inNormalPropertiesLayout.tvTitle.typeface, Typeface.BOLD)

        binding.inNormalPropertiesLayout.tvSeeMore.setOnClickListener {

            findNavController().navigate(
                R.id.searchResultFragment,
                data?.get(0)?.let { createModelForNormalProperties() }?.let { it2 ->
                    SearchResultFragmentArgs(
                        it2, createListsModel()
                    ).toBundle()
                }, navOptionsAnimation()
            )
        }

//        binding.inNormalPropertiesLayout.root.startAnimation(AnimationUtils.loadAnimation(context,R.anim.anim_swipe_slow))

    }

    private fun setupMostViewedPropertiesRv(data: List<PropertyModel>) {
        rvHomeMostViewedPropertiesAdapter.setListener(this@HomeFragment, this@HomeFragment)
        binding.inMostViewedPropertiesLayout.rv.adapter = rvHomeMostViewedPropertiesAdapter
        rvHomeMostViewedPropertiesAdapter.submitList(data)
        binding.inMostViewedPropertiesLayout.tvTitle.setTypeface(binding.inMostViewedPropertiesLayout.tvTitle.typeface, Typeface.BOLD)
//        binding.inMostViewedPropertiesLayout.root.startAnimation(AnimationUtils.loadAnimation(context,R.anim.anim_swipe_slow))

    }

    private fun setupExtraViews(data: List<HomeExtraSectionsModel>?) {
        ////// first
        binding.inFirstExtraSectionLayout.tvTitle.text = data?.get(0)?.title
        if (data?.get(0)?.sections?.isNotEmpty() == true) {
            setupHomeFirstExtraSectionRv(data[0].sections!!)

        } else {
            binding.inFirstExtraSectionLayout.root.visibility = View.GONE
        }
        binding.inFirstExtraSectionLayout.tvSeeMore.setOnClickListener {
            findNavController().navigate(
                R.id.searchResultFragment,
                data?.get(0)?.let { it1 -> createModelForSections(it1) }?.let { it2 ->
                    SearchResultFragmentArgs(
                        it2, createListsModel()
                    ).toBundle()
                }, navOptionsAnimation()
            )
        }
        ///// second
        binding.inSecondExtraSectionLayout.tvTitle.text = data?.get(1)?.title
        if (data?.get(1)?.sections?.isNotEmpty() == true) {
            setupHomeSecondExtraSectionRv(data[1].sections!!)

        } else {
            binding.inSecondExtraSectionLayout.root.visibility = View.GONE
        }
        binding.inSecondExtraSectionLayout.tvSeeMore.setOnClickListener {
            findNavController().navigate(
                R.id.searchResultFragment,
                data?.get(1)?.let { it1 -> createModelForSections(it1) }?.let { it2 ->
                    SearchResultFragmentArgs(
                        it2, createListsModel()
                    ).toBundle()
                }, navOptionsAnimation()
            )
        }
        ///// third
        binding.inThirdExtraSectionLayout.tvTitle.text = data?.get(2)?.title
        if (data?.get(2)?.sections?.isNotEmpty() == true) {
            setupHomeThirdExtraSectionRv(data[2].sections!!)

        } else {
            binding.inThirdExtraSectionLayout.root.visibility = View.GONE
        }
        binding.inThirdExtraSectionLayout.tvSeeMore.setOnClickListener {
            findNavController().navigate(
                R.id.searchResultFragment,
                data?.get(2)?.let { it1 -> createModelForSections(it1) }?.let { it2 ->
                    SearchResultFragmentArgs(
                        it2, createListsModel()
                    ).toBundle()
                }, navOptionsAnimation()
            )
        }
        ///// fourth
        binding.inFourthExtraSectionLayout.tvTitle.text = data?.get(3)?.title
        if (data?.get(3)?.sections?.isNotEmpty() == true) {
            setupHomeFourthExtraSectionRv(data[3].sections!!)

        } else {
            binding.inFourthExtraSectionLayout.root.visibility = View.GONE
        }
        binding.inFourthExtraSectionLayout.tvSeeMore.setOnClickListener {
            findNavController().navigate(
                R.id.searchResultFragment,
                data?.get(3)?.let { it1 -> createModelForSections(it1) }?.let { it2 ->
                    SearchResultFragmentArgs(
                        it2, createListsModel()
                    ).toBundle()
                }, navOptionsAnimation()
            )
        }
        ///// fifth
        binding.inFifthExtraSectionLayout.tvTitle.text = data?.get(4)?.title
        if (data?.get(4)?.sections?.isNotEmpty() == true) {
            setupHomeFifthExtraSectionRv(data[4].sections!!)

        } else {
            binding.inFifthExtraSectionLayout.root.visibility = View.GONE
        }
        binding.inFifthExtraSectionLayout.tvSeeMore.setOnClickListener {
            findNavController().navigate(
                R.id.searchResultFragment,
                data?.get(4)?.let { it1 -> createModelForSections(it1) }?.let { it2 ->
                    SearchResultFragmentArgs(
                        it2, createListsModel()
                    ).toBundle()
                }, navOptionsAnimation()
            )
        }
    }


    // -------------------------------------------------------------------------------------------------------------- //
    private fun setupHomeFirstExtraSectionRv(data: List<PropertyModel>) {
        rvHomeFirstExtraSectionAdapter.setListener(this, this)
        binding.inFirstExtraSectionLayout.rv.adapter = rvHomeFirstExtraSectionAdapter
        rvHomeFirstExtraSectionAdapter.submitList(data)
        binding.inFirstExtraSectionLayout.tvTitle.setTypeface(binding.inFirstExtraSectionLayout.tvTitle.typeface, Typeface.BOLD)
//        binding.inFirstExtraSectionLayout.root.startAnimation(AnimationUtils.loadAnimation(context,R.anim.anim_swipe_slow))

    }


    private fun setupHomeSecondExtraSectionRv(data: List<PropertyModel>) {
        rvHomeSecondExtraSectionAdapter.setListener(this, this)
        binding.inSecondExtraSectionLayout.rv.adapter = rvHomeSecondExtraSectionAdapter
        rvHomeSecondExtraSectionAdapter.submitList(data)
        binding.inSecondExtraSectionLayout.tvTitle.setTypeface(binding.inSecondExtraSectionLayout.tvTitle.typeface, Typeface.BOLD)
//        binding.inSecondExtraSectionLayout.root.startAnimation(AnimationUtils.loadAnimation(context,R.anim.anim_swipe_slow))

    }


    private fun setupHomeThirdExtraSectionRv(data: List<PropertyModel>) {
        rvHomeThirdExtraSectionAdapter.setListener(this, this)
        binding.inThirdExtraSectionLayout.rv.adapter = rvHomeThirdExtraSectionAdapter
        rvHomeThirdExtraSectionAdapter.submitList(data)
//        binding.inThirdExtraSectionLayout.root.startAnimation(AnimationUtils.loadAnimation(context,R.anim.anim_swipe_slow))

    }

    private fun setupHomeFourthExtraSectionRv(data: List<PropertyModel>) {
        rvHomeFourthExtraSectionAdapter.setListener(this, this)
        binding.inFourthExtraSectionLayout.rv.adapter = rvHomeFourthExtraSectionAdapter
        rvHomeFourthExtraSectionAdapter.submitList(data)
        binding.inFourthExtraSectionLayout.tvTitle.setTypeface(binding.inFourthExtraSectionLayout.tvTitle.typeface, Typeface.BOLD)
//        binding.inFourthExtraSectionLayout.root.startAnimation(AnimationUtils.loadAnimation(context,R.anim.anim_swipe_slow))

    }

    private fun setupHomeFifthExtraSectionRv(data: List<PropertyModel>) {
        rvHomeFifthExtraSectionAdapter.setListener(this, this)
        binding.inFifthExtraSectionLayout.rv.adapter = rvHomeFifthExtraSectionAdapter
        rvHomeFifthExtraSectionAdapter.submitList(data)
        binding.inFifthExtraSectionLayout.tvTitle.setTypeface(binding.inFifthExtraSectionLayout.tvTitle.typeface, Typeface.BOLD)
//        binding.inFifthExtraSectionLayout.root.startAnimation(AnimationUtils.loadAnimation(context,R.anim.anim_swipe_slow))

    }


    private fun setupNewsRv(data: List<NewsModel>) {
        rvHomeNewsAdapter.setListener(this@HomeFragment)
        binding.inNewsLayout.rv.adapter = rvHomeNewsAdapter
        rvHomeNewsAdapter.submitList(data)
        binding.inNewsLayout.tvTitle.setTypeface(binding.inNewsLayout.tvTitle.typeface, Typeface.BOLD)
//        binding.inNewsLayout.root.startAnimation(AnimationUtils.loadAnimation(context,R.anim.anim_swipe_slow))
        binding.inNewsLayout.tvSeeMore.setOnClickListener {
            findNavController().navigate(R.id.seeMoreNewsFragment)
        }

    }

//    @SuppressLint("ClickableViewAccessibility")
//    private fun setupSearchFunctionality() {
//        binding.inToolbar.apply {
//
//            // Search action
//            FHomeEtSearch.setOnEditorActionListener { _, actionId, _ ->
//                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    val query = FHomeEtSearch.text.toString().trim()
//
//                    if (query.isEmpty()) {
//                        showToast(getString(R.string.enter_a_query))
//                    } else {
//                        findNavController().navigate(
//                            R.id.searchPropertyResultFragment,
//                            null,
//                            navOptionsFromTopAnimation()
//                        )
//                    }
//
//                } else if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    viewLifecycleOwner.lifecycleScope.launch {
//                        delay(500)
//                        FHomeEtSearch.clearFocus()
//                    }
//                }
//                true
//            }
//
//            // onTextChange listener
////            FHomeEtSearch.addTextChangedListener(object : TextWatcher {
////                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
////
////                }
////
////                override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
////                    if (charSequence?.isNotEmpty() == true) {
////                        tvSearch.visibility = View.VISIBLE
////                    } else {
////                        tvSearch.visibility = View.GONE
////                    }
////                }
////
////                override fun afterTextChanged(s: Editable?) {
////
////                }
////            })
//
//            // onFocus listener
//            FHomeEtSearch.setOnFocusChangeListener { _, hasFocus ->
//                if (hasFocus) {
//                    tvSearch.visibility = View.VISIBLE
//                } else {
//                    tvSearch.visibility = View.GONE
//                }
//            }
//
//            // tvSearch listener
//            tvSearch.setOnClickListener {
//                findNavController().navigate(
//                    R.id.searchPropertyResultFragment,
//                    null,
//                    navOptionsFromTopAnimation()
//                )
//            }
//        }
//
//        // remove focus onOutsideClick
//        binding.root.setOnTouchListener { _, _ ->
//            binding.inToolbar.FHomeEtSearch.clearFocus()
//            false
//        }
//
//        binding.scrollView.setOnTouchListener { _, _ ->
//            binding.inToolbar.FHomeEtSearch.clearFocus()
//            false
//        }
//    }

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

    override fun onFeaturedRealEstateClick(model: PropertyModel) {
        findNavController().navigate(
            R.id.propertyDetailsFragment,
            PropertyDetailsFragmentArgs(model.listingNumber).toBundle()
        )
    }

    override fun onFeaturedProjectClick(model: ProjectModel) {
        findNavController().navigate(
            R.id.myProjectDetailsFragment,
            MyProjectDetailsFragmentArgs(model.listingNumber,model.datePosted).toBundle()
        )
    }



    override fun onPropertyByCityClick(model: CitiesModel) {
        findNavController().navigate(
            R.id.searchResultFragment,
            SearchResultFragmentArgs(
                createModel(model), createListsModel()
            ).toBundle(), navOptionsAnimation()
        )
        // Toast.makeText(requireContext(), model.title, Toast.LENGTH_SHORT).show()
    }


    override fun onItemClicked1(model: PropertyModel) {
        findNavController().navigate(
            R.id.propertyDetailsFragment,
            PropertyDetailsFragmentArgs(model.listingNumber).toBundle()
        )
    }

    override fun onItemClicked2(model: PropertyModel) {
        findNavController().navigate(
            R.id.propertyDetailsFragment,
            PropertyDetailsFragmentArgs(model.listingNumber).toBundle()
        )
    }

    override fun onItemClicked3(model: PropertyModel) {
        findNavController().navigate(
            R.id.propertyDetailsFragment,
            PropertyDetailsFragmentArgs(model.listingNumber).toBundle()
        )
    }

    override fun onItemClicked4(model: PropertyModel) {
        findNavController().navigate(
            R.id.propertyDetailsFragment,
            PropertyDetailsFragmentArgs(model.listingNumber).toBundle()
        )
    }

    override fun onItemClicked5(model: PropertyModel) {
        findNavController().navigate(
            R.id.propertyDetailsFragment,
            PropertyDetailsFragmentArgs(model.listingNumber).toBundle()
        )
    }

    override fun onNewsClick(model: NewsModel) {
        findNavController().navigate(
            R.id.newsDetailsFragment,
            NewsDetailsFragmentArgs(model.id.toString()).toBundle()
        )
    }

    override fun onMostViewedPropertiesClicked(model: PropertyModel) {
        findNavController().navigate(
            R.id.propertyDetailsFragment,
            PropertyDetailsFragmentArgs(model.listingNumber).toBundle()
        )
    }

    override fun onNormalPropertyClicked(model: PropertyModel) {
        findNavController().navigate(
            R.id.propertyDetailsFragment,
            PropertyDetailsFragmentArgs(model.listingNumber).toBundle()
        )
    }

    override fun onFavClick(model: PropertyModel) {
        if (UserUtil.isUserLogin()){
            viewModel.addOrRemoveFav(model.id)
            fetchAddRemoveToFavState()

        }else{
            findNavController().navigate(R.id.loginDialog)
        }


    }

    override fun onItemClick(model: CriteriaModel) {
        findNavController().navigate(
            R.id.searchResultFragment,
            createModelSearchByTypeProperties(model).let { it2 ->
                SearchResultFragmentArgs(
                    it2, createListsModel()
                ).toBundle()
            }, navOptionsAnimation()
        )
    }
    // ------------------------------------------------------------------------------------------------------------------------------------ //


}