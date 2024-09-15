package eramo.amtalek.presentation.ui.search.searchform

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentSearchFormBinding
import eramo.amtalek.domain.model.project.AmenityModel
import eramo.amtalek.domain.model.property.CriteriaModel
import eramo.amtalek.domain.search.SearchDataListsModel
import eramo.amtalek.domain.search.SearchModelDto
import eramo.amtalek.presentation.adapters.recyclerview.home.RvSearchByFinsingInSearchFormAdapter
import eramo.amtalek.presentation.adapters.recyclerview.home.RvSearchByPropertyTypeSearchResultAdapter
import eramo.amtalek.presentation.adapters.spinner.CriteriaSpinnerAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
import eramo.amtalek.presentation.ui.drawer.addproperty.fifth.SelectAmenitiesAdapter
import eramo.amtalek.presentation.ui.search.searchform.locationspopup.AllLocationsPopUp
import eramo.amtalek.presentation.ui.search.searchresult.SearchResultFragmentArgs
import eramo.amtalek.util.navOptionsAnimation
import eramo.amtalek.util.selectedLocation
import eramo.amtalek.util.showToast
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchFormFragment : BindingFragment<FragmentSearchFormBinding>(), RvSearchByPropertyTypeSearchResultAdapter.OnItemClickListener {
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSearchFormBinding::inflate

    @Inject
    lateinit var amenitiesAdapter: SelectAmenitiesAdapter

    private var selectedPurposeId: Int? = null
    private var selectedFinishingId: Int? = null
    private var selectedTypeId: Int? = null
    private var selectedCurrencyId: Int? = null
    private var selectedBedrooms: Int? = null
    private var selectedBathrooms: Int? = null


    private var listOfPurposeItems = ArrayList<CriteriaModel>()
    private var listOfFinishingItems = ArrayList<CriteriaModel>()
    private var listOfTypeItems = ArrayList<CriteriaModel>()
    private var listOfCurrencyItems = ArrayList<CriteriaModel>()
    private var listOfAmenitiesItems = ArrayList<AmenityModel>()

    @Inject
    lateinit var rvSearchResultsPropertiesAdapter: RvSearchByPropertyTypeSearchResultAdapter

    @Inject
    lateinit var rvSearchByFinsingInSearchFormAdapter: RvSearchByFinsingInSearchFormAdapter

    private var isAmenityOpen = false

    private val viewModel by viewModels<SearchFormViewModel>()
    private var selectedLocationId: Int? = null
    private var selectedLocationName: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolBar()
        setupListOfNumbers()
        clickListeners()
        setupObservers()
        fetchData()
        initViews()
        getApis()
        binding.btnCancel.setOnClickListener {
            selectedLocationId = -1
            selectedLocationName = getString(R.string.location)
            binding.locationValue.text = getString(R.string.location)
            viewModel.getSearchFilltration(
                propertyType = "",
                purpose = "",
                finishing = "",
                currency = "",
                amenities = emptyList(),
                minPrice = "",
                maxPrice = "",
                minArea = "",
                maxArea = "",
                minBedrooms = "",
                minBathrooms = ""
            )
        }
    }

    private fun getApis() {
        viewModel.getInitApis()
    }

    private fun initViews() {
        binding.apply {
            binding.etMaxPrice.setText(0.toString())
            binding.etMinPrice.setText(0.toString())
            binding.etMaxArea.setText(0.toString())
            binding.etMinArea.setText(0.toString())
        }


    }

    private fun fetchData() {
        fetchInitApis()
        fetchGetPropertyTypes()
        fetchGetPropertyFinishing()
        fetchGetAmenitiesState()
        fetchGetPropertyPurpose()
        fetchCurrencies()
        fetchTotalProperties()
    }


    private fun fetchTotalProperties(
//        propertyType: String?,
//        purpose: String?,
//        finishing: String?,
//        currency: String?,
//        amenities: List<Int> = emptyList(), // List of integers for amenities
//        minPrice: String?,
//        maxPrice: String?,
//        minArea: String?,
//        maxArea: String?,
//        minBedrooms: String?,
//        minBathrooms: String?
    ) {
        viewModel.getSearchFilltration(
            propertyType = selectedTypeId?.toString() ?: "",
            purpose = ((if (selectedPurposeId == -1) "" else selectedPurposeId)?.toString())?:"",
            finishing =((if (selectedFinishingId == -1 ) "" else selectedFinishingId)?.toString())?:"",
            currency = ((if(selectedCurrencyId == -1) "" else selectedCurrencyId)?.toString())?:"",
          //   amenities = listOfAmenitiesItems.map { it.id.toString() }, // List of integers for amenities,
            minPrice = if (binding.etMinPrice.text.toString().isNotEmpty() && binding.etMinPrice.text.toString().toInt() != 0)
                binding.etMinPrice.text.toString()
            else
                "",
            maxPrice = if (binding.etMaxPrice.text.toString().isNotEmpty() && binding.etMaxPrice.text.toString().toInt() != 0)
                binding.etMaxPrice.text.toString()
            else
                "",
            minArea = if (binding.etMinArea.text.toString().isNotEmpty() && binding.etMinArea.text.toString().toInt() != 0)
                binding.etMinArea.text.toString()
            else
                "",
            maxArea = if (binding.etMaxArea.text.toString().isNotEmpty() && binding.etMaxArea.text.toString().toInt() != 0)
                binding.etMaxArea.text.toString()
            else
                "",
            minBedrooms = selectedBedrooms?.toString() ?: "",
            minBathrooms = selectedBathrooms?.toString() ?: ""
        )

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchFilltrationState.collect() {
                    when(it) {
                        is Resource.Loading -> {
                            LoadingDialog.showDialog()
                        }
                        is Resource.Success -> {
                            LoadingDialog.dismissDialog()
                            if (it.data?.data?.totalProps != null) {
                                binding.tvDescription.text = it.data.data?.totalProps.toString()
                                Log.e("ahh", it.data.data?.totalProps.toString())
                            } else {
                                binding.tvDescription.text = "0"

                                }
                        }
                        is Resource.Error -> {
                            LoadingDialog.dismissDialog()
                        }
                    }
                }
            }
        }
    }

    private fun fetchCurrencies() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currenciesState.collect() {
                    when (it) {
                        is UiState.Loading -> {
                            LoadingDialog.showDialog()
                        }

                        is UiState.Success -> {
                            val currencies = it.data
                            Log.e("ahh", currencies.toString())
                            if (currencies != null) {
                                setupCurrenciesSpinner(currencies.toMutableList())
                            }
                            LoadingDialog.dismissDialog()
                        }

                        is UiState.Error -> {
                            showToast("Error fetching currencies")
                            LoadingDialog.dismissDialog()
                        }

                        is UiState.Empty -> Unit
                    }
                }
            }
        }
    }

    private fun setupObservers() {
        selectedLocation.observe(viewLifecycleOwner) { location ->
            if (location.id != -1) {
                // If a valid location is chosen, update the UI and variables
                binding.locationValue.text = location.title
                selectedLocationId = location.id
                selectedLocationName = location.title
            } else {
                // Reset the location to a default value if no city is selected
                clearSelectedLocation()
            }
        }
    }

    // Method to reset the selected location
    private fun clearSelectedLocation() {
        selectedLocationId = -1 // Reset to an invalid ID
        selectedLocationName = "" // Clear the name
        binding.locationValue.text = getString(R.string.all_locations) // Set default text in UI
    }

    private fun setupSearchFunctionality(data: List<CriteriaModel>) {

        rvSearchResultsPropertiesAdapter.setListener(object : RvSearchByPropertyTypeSearchResultAdapter.OnItemClickListener {
            override fun onItemClick(model: CriteriaModel) {

                val selectedIndex = data.indexOfFirst { it.id == model.id }


                val previousPosition = rvSearchResultsPropertiesAdapter.selectedPosition

                if (selectedIndex == previousPosition) {
                    rvSearchResultsPropertiesAdapter.selectedPosition = -1
                    rvSearchResultsPropertiesAdapter.notifyItemChanged(previousPosition)
                } else {
                    rvSearchResultsPropertiesAdapter.selectedPosition = selectedIndex


                    if (previousPosition != RecyclerView.NO_POSITION) {
                        rvSearchResultsPropertiesAdapter.notifyItemChanged(previousPosition)
                    }
                    rvSearchResultsPropertiesAdapter.notifyItemChanged(selectedIndex)
                }
                selectedTypeId =
                    if (selectedIndex == rvSearchResultsPropertiesAdapter.selectedPosition) {
                        model.id
                    } else {
                        null
                    }
                fetchTotalProperties(
//                    propertyType = model.id.toString(),
//                    minPrice = null,
//                    maxPrice = null,
//                    minArea = "",
//                    maxArea = "",
//                    minBedrooms = "",
//                    minBathrooms = "",
//                    amenities = emptyList(),
//                    currency = "",
//                    finishing = "",
//                    purpose = ""

                )
            }
        })

        // Set the adapter to the RecyclerView and submit the data
        binding.inSearchByPropertyType.rv.adapter = rvSearchResultsPropertiesAdapter
        rvSearchResultsPropertiesAdapter.submitList(data)
        binding.inSearchByPropertyType.tvTitle.visibility = View.VISIBLE
    }

    private fun setupSearchRvFinshing(data: List<CriteriaModel>) {

        rvSearchByFinsingInSearchFormAdapter.setListener(object : RvSearchByFinsingInSearchFormAdapter.OnItemClickListener {
            override fun onItemClick(model: CriteriaModel) {

                val selectedIndex = data.indexOfFirst { it.id == model.id }


                val previousPosition = rvSearchByFinsingInSearchFormAdapter.selectedPosition

                if (selectedIndex == previousPosition) {
                    rvSearchByFinsingInSearchFormAdapter.selectedPosition = -1
                    rvSearchByFinsingInSearchFormAdapter.notifyItemChanged(previousPosition)
                } else {
                    rvSearchByFinsingInSearchFormAdapter.selectedPosition = selectedIndex


                    if (previousPosition != RecyclerView.NO_POSITION) {
                        rvSearchByFinsingInSearchFormAdapter.notifyItemChanged(previousPosition)
                    }
                    rvSearchByFinsingInSearchFormAdapter.notifyItemChanged(selectedIndex)
                }
                selectedFinishingId =
                    if (selectedIndex == rvSearchByFinsingInSearchFormAdapter.selectedPosition) {
                        model.id
                    } else {
                        null
                    }
                fetchTotalProperties(
//                    finishing = model.id.toString(),
//                    minPrice = null,
//                    maxPrice = null,
//                    minArea = null,
//                    maxArea = null,
//                    minBedrooms = null,
//                    minBathrooms = null,
//                    amenities = emptyList(),
//                    currency = null,
//                    propertyType = null,
//                    purpose = null

                )
            }
        })

        // Set the adapter to the RecyclerView and submit the data
        binding.rvFinishing.adapter = rvSearchByFinsingInSearchFormAdapter
        rvSearchByFinsingInSearchFormAdapter.submitList(data)
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

    private fun clickListeners() {
        binding.btnConfirm.setOnClickListener() {
            if (isValidForm()) {
                binding.apply {
                    val myModel = createModel()
                    val listsModel = createListsModel()
                    findNavController().navigate(
                        R.id.searchResultFragment,
                        SearchResultFragmentArgs(
                            searchQuery = myModel,
                            dataLists = listsModel
                        ).toBundle(),
                        navOptionsAnimation()
                    )
                }
            }
        }
        binding.locationSpinner.setOnClickListener() {
            val couponDialog = AllLocationsPopUp()
            couponDialog.show(
                activity?.supportFragmentManager!!,
                "extra"
            )
        }
        // hide and show amenities spinner on click on any part of the view
        binding.amenitiesSpinner.setOnClickListener() {
            if (!isAmenityOpen) {
                binding.amenitesArrow.rotationX = 180f
                binding.amenitiesRv.visibility = View.VISIBLE
                isAmenityOpen = true
            } else {
                binding.amenitesArrow.rotationX = 0f
                binding.amenitiesRv.visibility = View.GONE
                isAmenityOpen = false
            }
        }
    }

    private fun setupListOfNumbers() {
        val numbersOfBedrooms = (1..5).toList() + listOf(0) // Add 0 at the end
        val numbersOfBathrooms = (1..4).toList() + listOf(0) // Add 0 at the end


        // Set up both RecyclerViews using a helper function
        setupRecyclerView(binding.rvBedrooms, numbersOfBedrooms) { selectedNumber ->
            selectedBedrooms = selectedNumber ?: 0 // Update the selected number of bedrooms, default to 0 if null
            fetchTotalProperties(
//                minBedrooms = selectedNumber.toString(),
//                minBathrooms = null,
//                maxPrice = null,
//                minArea = null,
//                maxArea = null,
//                propertyType = null,
//                purpose = null,
//                finishing = null,
//                currency = null,
//                amenities = emptyList(),
//                minPrice = null
            )
        }

        setupRecyclerView(binding.rvBathrooms, numbersOfBathrooms) { selectedNumber ->
            selectedBathrooms = selectedNumber ?: 0 // Update the selected number of bathrooms, default to 0 if null
            fetchTotalProperties(
//                minBathrooms = selectedNumber.toString(),
//                minBedrooms = null,
//                maxPrice = null,
//                minArea = null,
//                maxArea = null,
//                propertyType = null,
//                purpose = null,
//                finishing = null,
//                currency = null,
//                amenities = emptyList(),
//                minPrice = null
            )
        }
    }


    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        numbers: List<Int>,
        onClick: (Int?) -> Unit
    ) {
        val adapter = NumberAdapter(numbers, onClick)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }


    private fun createModel(): SearchModelDto {
        binding.apply {
            val searchKeyWords = if (etMainKey.text.toString().isEmpty()) "" else etMainKey.text.toString()

            // Use selected bedrooms and bathrooms numbers
            val bedrooms = selectedBedrooms?.toString() ?: "" // Use the selected value, or default to an empty string
            val bathrooms = selectedBathrooms?.toString() ?: "" // Use the selected value, or default to an empty string

            val minPrice = if (etMinPrice.text.toString().toInt() == 0) "" else etMinPrice.text.toString()
            val maxPrice = if (etMaxPrice.text.toString().toInt() == 0) "" else etMaxPrice.text.toString()
            val minArea = if (etMinArea.text.toString().toInt() == 0) "" else etMinArea.text.toString()
            val maxArea = if (etMaxArea.text.toString().toInt() == 0) "" else etMaxArea.text.toString()
            val locationId = if (locationValue.text.toString() == getString(R.string.location)) "" else selectedLocationId
            val locationName =
                if (locationValue.text.toString() == getString(R.string.location)) getString(R.string.location) else selectedLocationName
            val purposeId = if (selectedPurposeId == -1) "" else selectedPurposeId
            val finishingId = selectedFinishingId?.toString() ?: "0"
            val typeId = selectedTypeId?.toString() ?: "0"
            val currencyId = if (selectedCurrencyId == -1) -1 else selectedCurrencyId

            val myModel = SearchModelDto(
                searchKeyWords = searchKeyWords,
                locationId = locationId.toString(),
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
                purposeId = purposeId.toString(),
                priceArrangeKeys = "asc",
                amenitiesListIds = if (amenitiesAdapter.selectionList.isEmpty()) null else "${amenitiesAdapter.selectionList}",
                city = 0,
                priority_keys = ""
            )
            Log.e("Yarab", amenitiesAdapter.selectionList.toString())
            return myModel
        }
    }


    private fun isValidForm(): Boolean {
        var isValid = true
        if (binding.etMinArea.text.toString().toInt() > binding.etMaxArea.text.toString().toInt()) {
            isValid = false
            Toast.makeText(requireContext(), getString(R.string.min_area_must_be_greater_than_max_area), Toast.LENGTH_SHORT).show()
        }
        if (binding.etMinPrice.text.toString().toInt() > binding.etMaxPrice.text.toString().toInt()) {
            isValid = false
            Toast.makeText(requireContext(), getString(R.string.min_price_must_be_greater_than_max_price), Toast.LENGTH_SHORT).show()
        }
        return isValid
    }

    private fun setupToolBar() {
        binding.inToolbar.apply {
            tvTitle.text = getString(R.string.find_your_property)
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun fetchInitApis() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.initScreenState.collect() { state ->
                    when (state) {
                        is UiState.Loading -> {
                            LoadingDialog.showDialog()
                        }

                        is UiState.Success -> {
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

    private fun fetchGetPropertyFinishing() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.propertyFinishingState.collect() {
                    when (it) {
                        is UiState.Loading -> {
                            LoadingDialog.showDialog()
                        }

                        is UiState.Success -> {
                            val types = it.data
                            setupFinishingSpinner(types!!.toMutableList())
                            setupSearchRvFinshing(types)
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
                viewModel.propertyTypesState.collect() {
                    when (it) {
                        is UiState.Loading -> {
                            LoadingDialog.showDialog()
                        }

                        is UiState.Success -> {
                            val types = it.data
                            if (types != null) {
                                setupTypesSpinner(types.toMutableList())
                                setupSearchFunctionality(types)
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
                viewModel.propertyAmenitiesState.collect() {
                    when (it) {
                        is UiState.Success -> {
                            amenitiesAdapter.saveData(it.data!!)

                            listOfAmenitiesItems = it.data as ArrayList<AmenityModel>

                            binding.amenitiesRv.setHasFixedSize(true)
                            binding.amenitiesRv.adapter = amenitiesAdapter
                            LoadingDialog.dismissDialog()
                        }

                        is UiState.Error -> {
                            Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
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

    private fun fetchGetPropertyPurpose() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.propertyPurposeState.collect() {
                    when (it) {
                        is UiState.Loading -> {
                            LoadingDialog.showDialog()
                        }

                        is UiState.Success -> {
                            val types = it.data
                            setupPurposeSpinner(types!!.toMutableList())
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

    private fun setupFinishingSpinner(data: MutableList<CriteriaModel>) {
        binding.apply {
            data.add(0, CriteriaModel(-1, -1, getString(R.string.finishing), image = ""))
            listOfFinishingItems.clear()
            for (item in data) {
                listOfFinishingItems.add(item)
            }

            val criteriaSpinnerAdapter = CriteriaSpinnerAdapter(requireContext(), data)
            finishingSpinner.adapter = criteriaSpinnerAdapter

            finishingSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val model = parent?.getItemAtPosition(position) as CriteriaModel
                    selectedFinishingId = model.id
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        }
    }

    private fun setupTypesSpinner(data: MutableList<CriteriaModel>) {
        binding.apply {
            data.add(0, CriteriaModel(-1, -1, getString(R.string.typee), image = ""))
            listOfTypeItems.clear()
            for (item in data) {
                listOfTypeItems.add(item)
            }
            val criteriaSpinnerAdapter = CriteriaSpinnerAdapter(requireContext(), data)
            typeSpinner.adapter = criteriaSpinnerAdapter

            typeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val model = parent?.getItemAtPosition(position) as CriteriaModel
                    selectedTypeId = model.id
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        }
    }

    private fun setupPurposeSpinner(data: MutableList<CriteriaModel>) {
        binding.apply {
            data.add(0, CriteriaModel(-1, -1, getString(R.string.purpose), image = ""))
            listOfPurposeItems.clear()
            for (item in data) {
                listOfPurposeItems.add(item)
            }
            val criteriaSpinnerAdapter = CriteriaSpinnerAdapter(requireContext(), data)
            purposeSpinner.adapter = criteriaSpinnerAdapter

            purposeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val model = parent?.getItemAtPosition(position) as CriteriaModel
                    selectedPurposeId = model.id
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

        }
    }

    private fun setupCurrenciesSpinner(data: MutableList<CriteriaModel>) {
        binding.apply {
            data.add(0, CriteriaModel(-1, -1, getString(R.string.currency), image = ""))
            listOfCurrencyItems.clear()
            for (item in data) {
                listOfCurrencyItems.add(item)
            }
            val criteriaSpinnerAdapter = CriteriaSpinnerAdapter(requireContext(), data)
            currencySpinner.adapter = criteriaSpinnerAdapter

            currencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val model = parent?.getItemAtPosition(position) as CriteriaModel
                    selectedCurrencyId = model.id
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        }
    }

    override fun onItemClick(model: CriteriaModel) {


    }


}