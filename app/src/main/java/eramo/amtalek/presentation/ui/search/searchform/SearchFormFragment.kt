package eramo.amtalek.presentation.ui.search.searchform

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentSearchFormBinding
import eramo.amtalek.domain.model.project.AmenityModel
import eramo.amtalek.domain.model.property.CriteriaModel
import eramo.amtalek.domain.search.SearchDataListsModel
import eramo.amtalek.domain.search.SearchModelDto
import eramo.amtalek.presentation.adapters.spinner.CriteriaSpinnerAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
import eramo.amtalek.presentation.ui.drawer.addproperty.fifth.SelectAmenitiesAdapter
import eramo.amtalek.presentation.ui.search.searchform.locationspopup.AllLocationsPopUp
import eramo.amtalek.presentation.ui.search.searchresult.SearchResultFragmentArgs
import eramo.amtalek.util.navOptionsAnimation
import eramo.amtalek.util.selectedLocation
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

@AndroidEntryPoint
class SearchFormFragment : BindingFragment<FragmentSearchFormBinding>() {
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSearchFormBinding::inflate

    @Inject
    lateinit var amenitiesAdapter: SelectAmenitiesAdapter

    private var selectedPurposeId:Int? =null
    private var selectedFinishingId:Int? = null
    private var selectedTypeId:Int? = null
    private var selectedCurrencyId:Int? = null

    private var listOfPurposeItems = ArrayList<CriteriaModel>()
    private var listOfFinishingItems = ArrayList<CriteriaModel>()
    private var listOfTypeItems = ArrayList<CriteriaModel>()
    private var listOfCurrencyItems = ArrayList<CriteriaModel>()
    private var listOfAmenitiesItems = ArrayList<AmenityModel>()

    private var isAmenityOpen = false

    private val viewModel by viewModels<SearchFormViewModel>()
    private var selectedLocationId:Int? = null
    private var selectedLocationName:String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolBar()
        clickListeners()
        setupObservers()
        fetchData()
        initViews()
        getApis()
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
    }

    private fun fetchCurrencies() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.currenciesState.collect(){
                    when(it){
                        is UiState.Loading->{
                            LoadingDialog.showDialog()
                        }
                        is UiState.Success->{
                            val currencies = it.data
                            Log.e("ahh", currencies.toString(), )
                            if (currencies != null) {
                                setupCurrenciesSpinner(currencies.toMutableList())
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

    private fun setupObservers() {
      selectedLocation.observe(viewLifecycleOwner){
            binding.locationValue.text = it.title
            selectedLocationId = it.id
            selectedLocationName = it.title
          Log.e("id", it.id.toString(), )
        }
    }

    private fun createListsModel(): SearchDataListsModel {
       val data =  SearchDataListsModel(
            listOfTypesItems = listOfTypeItems,
            listOfCurrencyItems = listOfCurrencyItems,
            listOfFinishingItems = listOfFinishingItems,
            listOfPurposeItems = listOfPurposeItems,
           listOfAmenitiesItems = listOfAmenitiesItems
        )
        return data
    }

    private fun clickListeners() {
        binding.btnConfirm.setOnClickListener(){
            if (isValidForm()){
                binding.apply {
                   val myModel = createModel()
                    val listsModel = createListsModel()
                    findNavController().navigate(R.id.searchResultFragment,
                        SearchResultFragmentArgs(searchQuery = myModel,
                                dataLists = listsModel
                        ).toBundle(),
                        navOptionsAnimation()
                    )

                }
            }
        }
        binding.locationSpinner.setOnClickListener(){
            val couponDialog = AllLocationsPopUp()
            couponDialog.show(
                activity?.supportFragmentManager!!,
                "extra"
            )
        }
        // hide and show amenities spinner on click on any part of the view
        binding.amenitiesSpinner.setOnClickListener(){
            if (!isAmenityOpen){
                binding.amenitesArrow.rotationX = 180f
                binding.amenitiesRv.visibility = View.VISIBLE
                isAmenityOpen = true
            }else{
                binding.amenitesArrow.rotationX = 0f
                binding.amenitiesRv.visibility = View.GONE
                isAmenityOpen = false
            }
        }
    }

    private fun createModel(): SearchModelDto {
        binding.apply {
            val searchKeyWords = if (etMainKey.text.toString().isEmpty()) "" else etMainKey.text.toString()
            val bedrooms = if (etBedroomsNumber.text.toString().isEmpty()) "" else etBedroomsNumber.text.toString()
            val bathrooms = if (etBathroomsNumber.text.toString().isEmpty()) "" else etBathroomsNumber.text.toString()
            val minPrice = if (etMinPrice.text.toString().toInt() ==0) "" else etMinPrice.text.toString()
            val maxPrice = if (etMaxPrice.text.toString().toInt() ==0) "" else etMaxPrice.text.toString()
            val minArea = if (etMinArea.text.toString().toInt() ==0) "" else etMinArea.text.toString()
            val maxArea = if (etMaxArea.text.toString().toInt() ==0) "" else etMaxArea.text.toString()
            val locationId = if(locationValue.text.toString()==getString(R.string.location)) "" else selectedLocationId
            val locationName = if(locationValue.text.toString()==getString(R.string.location)) getString(R.string.location) else selectedLocationName
            val purposeId = if(selectedPurposeId==-1) "" else selectedPurposeId
            val finishingId = if(selectedFinishingId==-1) "" else selectedFinishingId
            val typeId = if(selectedTypeId==-1) "" else selectedTypeId
            val currencyId = if(selectedCurrencyId==-1) -1 else selectedCurrencyId

            val myModel = SearchModelDto(
                searchKeyWords = searchKeyWords,
                locationId = locationId.toString(),
                locationName = locationName,
                currencyId =currencyId ,
                bathroomsNumber = bathrooms,
                bedroomsNumber = bedrooms,
                propertyTypeId = typeId.toString(),
                propertyFinishingId = finishingId.toString(),
                minPrice = minPrice,
                maxPrice = maxPrice,
                minArea = minArea,
                maxArea = maxArea,
                purposeId = purposeId.toString(),
                priceArrangeKeys = "asc",
                amenitiesListIds = if (amenitiesAdapter.selectionList.isEmpty()) null else "${amenitiesAdapter.selectionList}"
            )
            Log.e("Yarab", amenitiesAdapter.selectionList.toString(), )
            return myModel
        }
    }

    private fun isValidForm():Boolean{
        var isValid = true
        if (binding.etMinArea.text.toString().toInt()>binding.etMaxArea.text.toString().toInt()){
            isValid = false
            Toast.makeText(requireContext(), getString(R.string.min_area_must_be_greater_than_max_area), Toast.LENGTH_SHORT).show()
        }
        if (binding.etMinPrice.text.toString().toInt()>binding.etMaxPrice.text.toString().toInt()){
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
    private fun fetchInitApis(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.initScreenState.collect(){state->
                    when(state){
                        is UiState.Loading->{
                            LoadingDialog.showDialog()
                        }
                        is UiState.Success->{
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
    private fun fetchGetPropertyFinishing(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.propertyFinishingState.collect(){
                    when(it){
                        is UiState.Loading->{
                            LoadingDialog.showDialog()
                        }
                        is UiState.Success->{
                            val types = it.data
                            setupFinishingSpinner(types!!.toMutableList())
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
    private fun fetchGetPropertyTypes(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.propertyTypesState.collect(){
                    when(it){
                        is UiState.Loading->{
                            LoadingDialog.showDialog()
                        }
                        is UiState.Success->{
                            val types = it.data
                            if (types != null) {
                                setupTypesSpinner(types.toMutableList())
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
    private fun fetchGetAmenitiesState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.propertyAmenitiesState.collect(){
                    when (it){
                        is UiState.Success->{
                            amenitiesAdapter.saveData(it.data!!)

                            listOfAmenitiesItems = it.data as ArrayList<AmenityModel>

                            binding.amenitiesRv.setHasFixedSize(true)
                            binding.amenitiesRv.adapter = amenitiesAdapter
                            LoadingDialog.dismissDialog()
                        }
                        is UiState.Error->{
                            Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                            LoadingDialog.dismissDialog()
                        }
                        is UiState.Loading->{
                            LoadingDialog.showDialog()
                        }
                        is UiState.Empty->Unit
                    }
                }
            }
        }
    }
    private fun fetchGetPropertyPurpose(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.propertyPurposeState.collect(){
                    when(it){
                        is UiState.Loading->{
                            LoadingDialog.showDialog()
                        }
                        is UiState.Success->{
                            val types = it.data
                            setupPurposeSpinner(types!!.toMutableList())
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

    private fun setupFinishingSpinner(data:MutableList<CriteriaModel>) {
        binding.apply {
            data.add(0, CriteriaModel(-1,-1, getString(R.string.finishing)))
            listOfFinishingItems.clear()
            for (item in data){
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
    private fun setupTypesSpinner(data:MutableList<CriteriaModel>) {
        binding.apply {
            data.add(0, CriteriaModel(-1,-1, getString(R.string.typee)))
            listOfTypeItems.clear()
            for (item in data){
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
    private fun setupPurposeSpinner(data:MutableList<CriteriaModel>) {
        binding.apply {
            data.add(0, CriteriaModel(-1,-1, getString(R.string.purpose)))
            listOfPurposeItems.clear()
            for (item in data){
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
    private fun setupCurrenciesSpinner(data:MutableList<CriteriaModel>) {
        binding.apply {
            data.add(0, CriteriaModel(-1,-1, getString(R.string.currency)))
            listOfCurrencyItems.clear()
            for (item in data){
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



}