package eramo.amtalek.presentation.ui.search.searchresult

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentSearchResultBinding
import eramo.amtalek.databinding.ItemSliderTopBinding
import eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel
import eramo.amtalek.domain.model.home.slider.SliderModel
import eramo.amtalek.domain.model.project.AmenityModel
import eramo.amtalek.domain.model.property.CriteriaModel
import eramo.amtalek.domain.search.SearchDataListsModel
import eramo.amtalek.presentation.adapters.spinner.CriteriaSpinnerSmallAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.dialog.FilterDialogFragment
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
import eramo.amtalek.presentation.ui.drawer.addproperty.fifth.SelectAmenitiesAdapter
import eramo.amtalek.presentation.ui.interfaces.FavClickListener
import eramo.amtalek.presentation.ui.main.home.HomeMyViewModel
import eramo.amtalek.presentation.ui.main.home.details.properties.PropertyDetailsFragmentArgs
import eramo.amtalek.presentation.ui.search.searchform.SearchFormViewModel
import eramo.amtalek.presentation.ui.search.searchform.locationspopup.AllLocationsPopUp
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.itemsCount
import eramo.amtalek.util.selectedLocation
import eramo.amtalek.util.showToast
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.imaginativeworld.whynotimagecarousel.listener.CarouselListener
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import org.imaginativeworld.whynotimagecarousel.utils.setImage
import javax.inject.Inject

@AndroidEntryPoint
class SearchResultFragment : BindingFragment<FragmentSearchResultBinding>(),
    RvSearchResultsPropertiesAdapter.OnItemClickListener,
    FavClickListener {
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSearchResultBinding::inflate


    val args by navArgs<SearchResultFragmentArgs>()
    val searchQuery get() = args.searchQuery
    private val dataLists get() = args.dataLists

    private var selectedLocationId: Int? = null
    private var selectedLocationName: String? = null

    val viewModel by viewModels<SearchFormViewModel>()
    private val searchResultViewModel by viewModels<SearchResultViewModel>()
    private val homeMyViewModel:HomeMyViewModel by viewModels()

    private var selectedPurposeId: Int? = null
    private var selectedFinishingId: Int? = null
    private var selectedTypeId: Int? = null
    private var selectedCurrencyId: Int? = null

    private var isUserInteracting = false
    @Inject
    lateinit var amenitiesAdapter: SelectAmenitiesAdapter

    @Inject
    lateinit var propertiesAdapter: RvSearchResultsPropertiesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolBar()
        initViews()
        fetchData()
        requestData()
        clickListeners()
        setupObservers()
        val data = dataLists
        setupSpinners(data)
        setupScrollListener()
    }

    override fun onResume() {
        super.onResume()
        binding.autoCompleteFilterType.setText("")
    }
    private fun clickListeners() {
        binding.apply {
            locationSpinner.setOnClickListener() {
                val couponDialog = AllLocationsPopUp()
                couponDialog.show(
                    activity?.supportFragmentManager!!,
                    "extra"
                )
            }
            btnFilter.setOnClickListener {
                val couponDialog = FilterDialogFragment()
                couponDialog.show(
                    activity?.supportFragmentManager!!,
                    "extra"
                )
            }
            amenitiesSpinner.setOnClickListener(){
                aminitiesCardView.visibility = View.VISIBLE
            }
            btnConfirm.setOnClickListener {
                aminitiesCardView.visibility = View.GONE
                val selectedAmenities = amenitiesAdapter.selectionList
                searchQuery.amenitiesListIds = selectedAmenities.toString()
                requestData()
            }
            btnCancel.setOnClickListener {
                aminitiesCardView.visibility = View.GONE
            }
            autoCompleteFilterType.setOnItemClickListener { parent, view, position, id ->
                if (position==0){
                    searchQuery.priceArrangeKeys = "asc"
                    requestData()
                    binding.autoCompleteFilterType.setText("")

                }else if (position==1){
                    searchQuery.priceArrangeKeys = "desc"
                    requestData()
                    binding.autoCompleteFilterType.setText("")

                }

            }



        }
    }
    private fun fetchGetHomeSlider() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchResultViewModel.searchResultSliderState.collect() { state ->
                    when (state) {

                        is UiState.Success -> {
                            val data = state.data
                            if (!data.isNullOrEmpty()) {
                                binding.carouselSliderBetween.visibility = View.VISIBLE
                                //   binding.carouselSliderBetweenDots.visibility = View.VISIBLE
                                setupSliderBetween(parseBetweenCarouselSliderList(data))
                            } else {
                                binding.carouselSliderBetween.visibility = View.GONE
                                //   binding.carouselSliderBetweenDots.visibility = View.GONE

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
                                homeMyViewModel.clickedOnAd(id.toString())
                            }
                        }

                        this@SearchResultFragment .binding.inToolbar
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

    private fun setupSpinners(data: SearchDataListsModel) {

        val finishingList = data.listOfFinishingItems.toMutableList()
        val typeList = data.listOfTypesItems.toMutableList()
        val purposeList = data.listOfPurposeItems.toMutableList()
        val currencyList = data.listOfCurrencyItems.toMutableList()
        val amenitiesList = data.listOfAmenitiesItems.toMutableList()

        setUpAmenities(amenitiesList)
        setupFinishingSpinner(finishingList)
        setupCurrenciesSpinner(currencyList)
        setupTypesSpinner(typeList)
        setupPurposeSpinner(purposeList)
        setUpFilterSpinner()
    }

    private fun requestData() {
        searchResultViewModel.getSearchResultSlider()
        val cityFromArgs = if(searchQuery.city==0) UserUtil.getCityFiltrationId() else searchQuery.city
        viewModel.search(
            city = cityFromArgs.toString(),
            propertyType = if(searchQuery.propertyTypeId=="-1"|| searchQuery.propertyTypeId=="") "" else searchQuery.propertyTypeId,
            minPrice = searchQuery.minPrice,
            maxPrice = searchQuery.maxPrice,
            minArea = searchQuery.minArea,
            maxArea = searchQuery.maxArea,
            minBeds = searchQuery.bedroomsNumber,
            minBathes = searchQuery.bathroomsNumber,
            country = UserUtil.getUserCountryFiltrationTitleId(),
            currency = if (searchQuery.currencyId == -1) null else searchQuery.currencyId,
            finishing = if(searchQuery.propertyFinishingId=="-1"|| searchQuery.propertyFinishingId=="") "" else searchQuery.propertyFinishingId,
            keyword = searchQuery.searchKeyWords,
            priceArrangeKeys = if (searchQuery.priceArrangeKeys == "asc") "asc" else searchQuery.priceArrangeKeys,
            purpose =if(searchQuery.purposeId=="-1"|| searchQuery.purposeId=="") "" else searchQuery.purposeId,
            region = if (searchQuery.locationId == "-1") "" else searchQuery.locationId,
            subRegion = null,
            amenitiesListIds = amenitiesAdapter.selectionList.toString(),
            priority_keys = searchQuery.priority_keys,
        )
        LoadingDialog.showDialog()
    }



    private fun setupObservers() {
        selectedLocation.observe(viewLifecycleOwner) {
            binding.locationValue.text = it.title
            selectedLocationId = it.id
            searchQuery.locationId = selectedLocationId.toString()
            selectedLocationName = it.title
            requestData()
        }
        itemsCount.observe(viewLifecycleOwner) {
            binding.tvTotalPropsValue.text = "(${it})"
        }

    }

    private fun fetchAddRemoveToFavState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.favState.collect() { state ->
                    when (state) {

                        is UiState.Success -> {
                            showToast(state.data?.message.toString())

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
    private fun fetchData() {
        fetchGetHomeSlider()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchState.collect() { data ->
                    data?.let {
                        propertiesAdapter.submitData(viewLifecycleOwner.lifecycle, data)
                        delay(1000)
                        binding.rvProperties.scrollToPosition(0)
                        LoadingDialog.dismissDialog()

                    }
                }
            }
        }
        fetchAddRemoveToFavState()
    }


    private fun initViews() {
        binding.rvProperties.adapter = propertiesAdapter
        propertiesAdapter.setListener(this@SearchResultFragment, this@SearchResultFragment)

        // Listen to the load states to handle loading and error states
        propertiesAdapter.addLoadStateListener { loadStates ->
            when (loadStates.refresh) {
                is LoadState.Loading -> {
                    // Optionally show a loading dialog or spinner
                    LoadingDialog.showDialog()
                }

                is LoadState.NotLoading -> {
                    LoadingDialog.dismissDialog()
                }

                is LoadState.Error -> {
                    // Handle error, e.g., show a message
                    LoadingDialog.dismissDialog()
                }
            }
        }
    }
    private fun setupScrollListener() {
        binding.nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
            val lastChild = binding.nestedScrollView.getChildAt(binding.nestedScrollView.childCount - 1)
            val diff = lastChild.bottom - (binding.nestedScrollView.height + scrollY)

            if (diff <= 200) {
                // Trigger loading more data when near the bottom
                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.searchState.collect() { data ->
                            data?.let {
                                propertiesAdapter.submitData(viewLifecycleOwner.lifecycle, data)
                                delay(1000)
                                binding.rvProperties.scrollToPosition(0)
                                LoadingDialog.dismissDialog()
                            }
                        }
                    }
                }
            }
        })
    }
    private fun setupToolBar() {
        binding.inToolbar.apply {
            tvTitle.text = getString(R.string.find_your_property)
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }


    override fun onFavClick(model: PropertyModel) {
        if (UserUtil.isUserLogin()){
            viewModel.addOrRemoveFav(model.id)

        }else{
            findNavController().navigate(R.id.loginDialog)
        }

    }

    override fun onPropertyClicks(model: PropertyModel) {
        findNavController().navigate(
            R.id.propertyDetailsFragment,
            PropertyDetailsFragmentArgs(model.listingNumber).toBundle()
        )

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupFinishingSpinner(data: MutableList<CriteriaModel>) {
        binding.apply {

            val criteriaSpinnerAdapter = CriteriaSpinnerSmallAdapter(requireContext(), data)
            finishingSpinner.adapter = criteriaSpinnerAdapter

            if (searchQuery.propertyFinishingId != "") {
                val selectedIndex =
                    data.indexOfFirst { it.id == searchQuery.propertyFinishingId?.toInt() }
                if (selectedIndex != -1) {
                    finishingSpinner.setSelection(selectedIndex)
                }
            }

            finishingSpinner.setOnTouchListener { _, _ ->
                isUserInteracting = true
                false
            }

            finishingSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (isUserInteracting) {
                        val model = parent?.getItemAtPosition(position) as CriteriaModel
                        selectedFinishingId = model.id
                        searchQuery.propertyFinishingId = selectedFinishingId.toString()
                        requestData()
                        isUserInteracting = false // Reset the flag after handling the user interaction
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

        }
    }

    private fun setupTypesSpinner(data: MutableList<CriteriaModel>) {
        binding.apply {

            val criteriaSpinnerAdapter = CriteriaSpinnerSmallAdapter(requireContext(), data)
            typeSpinner.adapter = criteriaSpinnerAdapter


            if (searchQuery.propertyTypeId != "") {
                val selectedIndex =
                    data.indexOfFirst { it.id == searchQuery.propertyTypeId?.toInt() }
                if (selectedIndex != -1) {
                    typeSpinner.setSelection(selectedIndex)
                }
            }

            typeSpinner.setOnTouchListener { _, _ ->
                isUserInteracting = true
                false
            }

            typeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (isUserInteracting) {
                        val model = parent?.getItemAtPosition(position) as CriteriaModel
                        selectedTypeId = model.id
                        searchQuery.propertyTypeId = selectedTypeId.toString()
                        requestData()
                        isUserInteracting =
                            false // Reset the flag after handling the user interaction
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

        }
    }



    private fun setupPurposeSpinner(data: MutableList<CriteriaModel>) {
        binding.apply {

            val criteriaSpinnerAdapter = CriteriaSpinnerSmallAdapter(requireContext(), data)
            purposeSpinner.adapter = criteriaSpinnerAdapter

            if (searchQuery.purposeId != "") {
                val selectedIndex = data.indexOfFirst { it.id == searchQuery.purposeId?.toInt() }
                if (selectedIndex != -1) {
                    purposeSpinner.setSelection(selectedIndex)
                }
            }
            purposeSpinner.setOnTouchListener { _, _ ->
                isUserInteracting = true
                false
            }
            purposeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (isUserInteracting) {
                        val model = parent?.getItemAtPosition(position) as CriteriaModel
                        selectedPurposeId = model.id
                        searchQuery.purposeId = selectedPurposeId.toString()
                        requestData()
                        isUserInteracting =
                            false // Reset the flag after handling the user interaction
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }


        }
    }

    private fun setUpFilterSpinner() {
        val filterTypes = resources.getStringArray(R.array.filter)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, filterTypes)
        binding.autoCompleteFilterType.setAdapter(arrayAdapter)
        binding.autoCompleteFilterType.inputType = InputType.TYPE_NULL
    }

    private fun setUpAmenities(amenitiesList: MutableList<AmenityModel>) {
        binding.amenitiesRv.adapter = amenitiesAdapter
        amenitiesAdapter.saveData(amenitiesList)
    }
    private fun setupCurrenciesSpinner(data: MutableList<CriteriaModel>) {
        binding.apply {
            val criteriaSpinnerAdapter = CriteriaSpinnerSmallAdapter(requireContext(), data)
            currencySpinner.adapter = criteriaSpinnerAdapter

            if (searchQuery.currencyId != -1) {
                val selectedIndex = data.indexOfFirst { it.id == searchQuery.currencyId }
                if (selectedIndex != -1) {
                    currencySpinner.setSelection(selectedIndex)
                }
            }
            currencySpinner.setOnTouchListener { _, _ ->
                isUserInteracting = true
                false
            }
            currencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (isUserInteracting) {
                        val model = parent?.getItemAtPosition(position) as CriteriaModel
                        selectedCurrencyId = model.id
                        searchQuery.currencyId = selectedCurrencyId
                        requestData()

                        isUserInteracting =
                            false // Reset the flag after handling the user interaction
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }
        }
    }
}