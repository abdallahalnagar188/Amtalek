package eramo.amtalek.presentation.ui.search.searchresult

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.data.remote.dto.search.searchresponse.SearchResponse
import eramo.amtalek.databinding.FragmentSearchResultBinding
import eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel
import eramo.amtalek.domain.model.project.AmenityModel
import eramo.amtalek.domain.model.property.CriteriaModel
import eramo.amtalek.domain.search.SearchDataListsModel
import eramo.amtalek.presentation.adapters.spinner.CriteriaSpinnerSmallAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.drawer.addproperty.fifth.SelectAmenitiesAdapter
import eramo.amtalek.presentation.ui.interfaces.FavClickListener
import eramo.amtalek.presentation.ui.main.home.details.properties.PropertyDetailsFragmentArgs
import eramo.amtalek.presentation.ui.search.searchform.SearchFormViewModel
import eramo.amtalek.presentation.ui.search.searchform.locationspopup.AllLocationsPopUp
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.selectedLocation
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
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
        val data = dataLists
        setupSpinners(data)
        clickListeners()
        setupObservers()
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
        }
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
    }

    private fun setUpAmenities(amenitiesList: MutableList<AmenityModel>) {
        binding.amenitiesRv.adapter = amenitiesAdapter
        amenitiesAdapter.saveData(amenitiesList)
    }
    //first time request data
    private fun requestData() {
        viewModel.search(
            city = UserUtil.getCityFiltrationId(),
            propertyType = searchQuery.propertyTypeId,
            minPrice = searchQuery.minPrice,
            maxPrice = searchQuery.maxPrice,
            minArea = searchQuery.minArea,
            maxArea = searchQuery.maxArea,
            minBeds = searchQuery.bedroomsNumber,
            minBathes = searchQuery.bathroomsNumber,
            country = UserUtil.getUserCountryFiltrationTitleId(),
            currency = if (searchQuery.currencyId == -1) null else searchQuery.currencyId,
            finishing = searchQuery.propertyFinishingId,
            keyword = searchQuery.searchKeyWords,
            priceArrangeKeys = "asc",
            purpose = searchQuery.purposeId,
            region = searchQuery.locationId,
            subRegion = null,
            amenitiesListIds = amenitiesAdapter.selectionList.toString()

        )
        Log.e("query", searchQuery.toString(), )
    }



    private fun setupObservers() {
        selectedLocation.observe(viewLifecycleOwner) {
            binding.locationValue.text = it.title
            selectedLocationId = it.id
            searchQuery.locationId = selectedLocationId.toString()
            selectedLocationName = it.title
            requestData()
        }
    }

    private fun fetchData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchState.collect() { data ->
                    data?.let {
                        propertiesAdapter.submitData(viewLifecycleOwner.lifecycle, data)
                    }
                }

            }
        }
    }

    private fun initViews() {
        binding.rvProperties.adapter = propertiesAdapter
        propertiesAdapter.setListener(this@SearchResultFragment, this@SearchResultFragment)
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
        viewModel.addOrRemoveFav(model.id)
    }

    override fun onPropertyClicks(model: PropertyModel) {
        findNavController().navigate(
            R.id.propertyDetailsFragment,
            PropertyDetailsFragmentArgs(model.listingNumber).toBundle()
        )

    }

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