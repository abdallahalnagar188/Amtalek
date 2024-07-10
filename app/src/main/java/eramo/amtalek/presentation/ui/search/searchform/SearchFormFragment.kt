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
import eramo.amtalek.domain.model.property.CriteriaModel
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


    private var isAmenityOpen = false

    private val viewModel by viewModels<SearchFormViewModel>()
    private var selectedLocationId:Int? = null
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
        }
    }

    private fun clickListeners() {
        binding.btnConfirm.setOnClickListener(){
            if (isValidForm()){
                binding.apply {
                   val myModel = createModel()
                    findNavController().navigate(R.id.searchResultFragment,SearchResultFragmentArgs(myModel).toBundle(),
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
            val searchKeyWords = if (etMainKey.text.toString().isEmpty()) null else etMainKey.text.toString()
            val bedrooms = if (etBedroomsNumber.text.toString().isEmpty()) null else etBedroomsNumber.text.toString().toInt()
            val bathrooms = if (etBathroomsNumber.text.toString().isEmpty()) null else etBathroomsNumber.text.toString().toInt()
            val minPrice = if (etMinPrice.text.toString().toInt() ==0) null else etMinPrice.text.toString().toInt()
            val maxPrice = if (etMaxPrice.text.toString().toInt() ==0) null else etMaxPrice.text.toString().toInt()
            val minArea = if (etMinArea.text.toString().toInt() ==0) null else etMinArea.text.toString().toInt()
            val maxArea = if (etMaxArea.text.toString().toInt() ==0) null else etMaxArea.text.toString().toInt()
            val locationId = if(selectedLocationId==-1) null else selectedLocationId
            val purposeId = if(selectedPurposeId==-1) null else selectedPurposeId
            val finishingId = if(selectedFinishingId==-1) null else selectedFinishingId
            val typeId = if(selectedTypeId==-1) null else selectedTypeId
            val currencyId = if(selectedCurrencyId==-1) null else selectedCurrencyId

            val myModel = SearchModelDto(
                searchKeyWords = searchKeyWords,
                locationId = locationId,
                currencyId =currencyId ,
                bathroomsNumber = bathrooms,
                bedroomsNumber = bedrooms,
                propertyTypeId = typeId,
                propertyFinishingId = finishingId,
                minPrice = minPrice,
                maxPrice = maxPrice,
                minArea = minArea,
                maxArea = maxArea,
                purposeId = purposeId,
                amenitiesListIds = if (amenitiesAdapter.selectionList.isEmpty()) null else amenitiesAdapter.selectionList
            )
            return myModel
        }
    }

    private fun isValidForm():Boolean{
        var isValid = true
//        if (binding.etMinArea.text.toString().isEmpty()){
//            binding.etMinArea.setText(0.toString())
//        }
//        if (binding.etMaxArea.text.toString().isEmpty()){
//            binding.etMaxArea.setText(0.toString())
//        }
//        if (binding.etMinPrice.text.toString().isEmpty()){
//
//            binding.etMinPrice.setText(0.toString())
//
//        }
//        if (binding.etMaxPrice.text.toString().isEmpty()){
//
//            binding.etMaxPrice.setText(0.toString())
//        }
//        if (binding.etBathroomsNumber.text.toString().isEmpty()){
//            binding.etBathroomsNumber.setText(0.toString())
//        }
//        if (binding.etBedroomsNumber.text.toString().isEmpty()){
//            binding.etBedroomsNumber.setText(0.toString())
//        }


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
            data.add(0, CriteriaModel(-1,-1, getString(R.string.no_selection)))
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

            // refresh onClick if getting list fail
            finishingSpinner.setOnTouchListener { view, motionEvent ->
                when (motionEvent?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        view.performClick()
                        if (data.isEmpty()) {
                            viewModel.getFinishingTypes()
                        }
                    }
                }
                return@setOnTouchListener true
            }
        }

    }
    private fun setupTypesSpinner(data:MutableList<CriteriaModel>) {
        binding.apply {
            data.add(0, CriteriaModel(-1,-1, getString(R.string.no_selection)))

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

            // refresh onClick if getting list fail
            typeSpinner.setOnTouchListener { view, motionEvent ->
                when (motionEvent?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        view.performClick()
                        if (data.isEmpty()) {
                            viewModel.getPropertyTypes()
                        }
                    }
                }
                return@setOnTouchListener true
            }
        }
    }
    private fun setupPurposeSpinner(data:MutableList<CriteriaModel>) {
        binding.apply {
            data.add(0, CriteriaModel(-1,-1, getString(R.string.no_selection)))

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

            // refresh onClick if getting list fail
            purposeSpinner.setOnTouchListener { view, motionEvent ->
                when (motionEvent?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        view.performClick()
                        if (data.isEmpty()) {
                            viewModel.getPropertyPurpose()
                        }
                    }
                }
                return@setOnTouchListener true
            }
        }
    }
    private fun setupCurrenciesSpinner(data:MutableList<CriteriaModel>) {
        binding.apply {
            data.add(0, CriteriaModel(-1,-1, getString(R.string.no_selection)))

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

            // refresh onClick if getting list fail
            currencySpinner.setOnTouchListener { view, motionEvent ->
                when (motionEvent?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        view.performClick()
                        if (data.isEmpty()) {
                            viewModel.getCurrencies()
                        }
                    }
                }
                return@setOnTouchListener true
            }
        }
    }



}