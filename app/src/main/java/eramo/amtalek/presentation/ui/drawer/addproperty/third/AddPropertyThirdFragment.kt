package eramo.amtalek.presentation.ui.drawer.addproperty.third

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentAddPropertyThirdBinding
import eramo.amtalek.domain.model.auth.CityModel
import eramo.amtalek.domain.model.auth.CountriesModel
import eramo.amtalek.domain.model.auth.CountryModel
import eramo.amtalek.domain.model.auth.RegionModel
import eramo.amtalek.domain.model.home.cities.CitiesModel
import eramo.amtalek.domain.model.property.CriteriaModel
import eramo.amtalek.presentation.adapters.spinner.CitiesSpinnerAdapter
import eramo.amtalek.presentation.adapters.spinner.CountriesSpinnerAdapter
import eramo.amtalek.presentation.adapters.spinner.RegionsSpinnerAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
import eramo.amtalek.util.LocalUtil
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddPropertyThirdFragment : BindingFragment<FragmentAddPropertyThirdBinding>(){
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentAddPropertyThirdBinding::inflate
    private val viewModel by viewModels<AddPropertyThirdFragmentViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        clickListeners()
        requestApis()
        fetchApis()
        setUpPrioritySpinner()
    }

    private fun setUpPrioritySpinner() {
        val offerTypes = resources.getStringArray(R.array.priority_type)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, offerTypes)
        binding.autoCompletePriorityType.setAdapter(arrayAdapter)
    }

    private fun fetchApis() {
        fetchInitApis()
        fetchGetPropertyCategories()
        fetchGetPropertyFinishing()
        fetchGetPropertyPurpose()
        fetchGetPropertyTypes()
        fetchGetFloorFinishingTypes()
        fetchGetCountries()
        fetchGetCities()
        fetchGetRegions()
        fetchGetSubRegions()
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
    private fun fetchGetPropertyCategories(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.propertyCategoriesState.collect(){
                    when(it){
                        is UiState.Success->{
                            val types = it.data
                            setupSpinners(types!!,binding.autoCompleteCategoryType)
                            LoadingDialog.dismissDialog()
                        }
                        is UiState.Loading->{
                            LoadingDialog.showDialog()
                        }
                        is UiState.Error->{
                            Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
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
                            setupSpinners(types!!,binding.autoCompleteFinishing)
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
    private fun fetchGetFloorFinishingTypes(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.propertyFloorFinishingState.collect(){
                    when(it){
                        is UiState.Loading->{
                            LoadingDialog.showDialog()
                        }
                        is UiState.Success->{
                            val types = it.data
                            setupSpinners(types!!,binding.autoCompleteFloorFinishingType)
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
                            setupSpinners(types!!,binding.autoCompletePurpose)
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
                            setupSpinners(types!!,binding.autoCompleteType)
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
    private fun fetchGetCountries(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.countriesState.collect(){
                    when(it){
                        is UiState.Loading->{
                            LoadingDialog.showDialog()
                        }
                        is UiState.Success->{
                            val types = it.data
                            setupCountrySpinners(types!!,binding.autoCompleteCountry)
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
    private fun fetchGetCities(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.citiesState.collect(){
                    when(it){
                        is UiState.Loading->{
                            LoadingDialog.showDialog()
                        }
                        is UiState.Success->{
                            val types = it.data
                            setupCitySpinners(types!!,binding.autoCompleteType)
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
    private fun fetchGetRegions(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.regionsState.collect(){
                    when(it){
                        is UiState.Loading->{
                            LoadingDialog.showDialog()
                        }
                        is UiState.Success->{
                            val types = it.data
                            setupRegionSpinners(types!!,binding.autoCompleteType)
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
    private fun fetchGetSubRegions(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.subRegionsState.collect(){
                    when(it){
                        is UiState.Loading->{
                            LoadingDialog.showDialog()
                        }
                        is UiState.Success->{
                            val types = it.data
                            setupSubRegionSpinners(types!!,binding.autoCompleteType)
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

    private fun setupSpinners(types: List<CriteriaModel>, port: AutoCompleteTextView) {
        val typesArray:ArrayList<String> = ArrayList()
        for (item in types){
            typesArray.add(item.title)
        }
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, typesArray)
        port.setAdapter(arrayAdapter)
    }
    private fun setupCountrySpinners(types: List<CountryModel>, port: AutoCompleteTextView) {
        val typesArray:ArrayList<String> = ArrayList()
        for (item in types){
            typesArray.add(item.name)
        }
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, typesArray)
        port.setAdapter(arrayAdapter)
    }
    private fun setupCitySpinners(types: List<CityModel>, port: AutoCompleteTextView) {
        val typesArray:ArrayList<String> = ArrayList()

        for (item in types){
            if (LocalUtil.isEnglish()){
                typesArray.add(item.titleEn)
            }else{
                typesArray.add(item.titleAr)
            }
        }
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, typesArray)
        port.setAdapter(arrayAdapter)
    }
    private fun setupRegionSpinners(types: List<RegionModel>, port: AutoCompleteTextView) {
        val typesArray:ArrayList<String> = ArrayList()
        for (item in types){
            typesArray.add(item.name)
        }
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, typesArray)
        port.setAdapter(arrayAdapter)
    }
    private fun setupSubRegionSpinners(types: List<RegionModel>, port: AutoCompleteTextView) {
        val typesArray:ArrayList<String> = ArrayList()
        for (item in types){
            typesArray.add(item.name)
        }
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, typesArray)
        port.setAdapter(arrayAdapter)
    }

    private fun requestApis() {
        viewModel.getInitApis()
    }

    private fun clickListeners() {
        binding.btnNext.setOnClickListener {
            if (formValidation()){
                
            }else{
                return@setOnClickListener
            }
        }
    }

    private fun setUpViews() {


        binding.autoCompleteSubRegion.inputType = InputType.TYPE_NULL
        binding.autoCompleteRegion.inputType = InputType.TYPE_NULL
        binding.autoCompleteCity.inputType = InputType.TYPE_NULL
        binding.autoCompleteCountry.inputType = InputType.TYPE_NULL
        binding.autoCompletePurpose.inputType = InputType.TYPE_NULL
        binding.autoCompletePriorityType.inputType = InputType.TYPE_NULL
        binding.autoCompleteFinishing.inputType = InputType.TYPE_NULL
        binding.autoCompleteFloorFinishingType.inputType = InputType.TYPE_NULL
        binding.autoCompleteType.inputType = InputType.TYPE_NULL
        binding.autoCompleteCategoryType.inputType = InputType.TYPE_NULL

        setupToolBar()
    }
    private fun setupToolBar() {
        binding.inToolbar.apply {
            tvTitle.text = getString(R.string.add_property_3_5)
            ivBack.setOnClickListener { findNavController().popBackStack()}
        }
    }

    private fun formValidation():Boolean{
        var isValid = true
        binding.apply {
            if (autoCompletePurpose.text.toString().isEmpty()){
                purposeSpinner.error = getString(R.string.please_select_purpose)
               isValid = false
            }
            if (autoCompleteCategoryType.text.toString().isEmpty()){
                categorySpinner.error = getString(R.string.please_select_category_type)
                isValid = false
            }
            if (autoCompletePriorityType.text.toString().isEmpty()){
                prioritySpinner.error = getString(R.string.please_select_priority_type)
                isValid = false
            }
            if (autoCompleteFinishing.text.toString().isEmpty()){
                finishingSpinner.error = getString(R.string.please_select_finishing)
                isValid = false
            }
            if (autoCompleteFloorFinishingType.text.toString().isEmpty()){
                floorFinishingTypeSpinner.error = getString(R.string.please_select_floor_finishing_type)
                isValid = false
            }
            if (autoCompleteType.text.toString().isEmpty()){
                typeSpinner.error = getString(R.string.please_select_type)
                isValid = false
            }
            if (autoCompleteCountry.text.toString().isEmpty()){
                countrySpinner.error = getString(R.string.please_select_country)
                isValid = false
            }
            if (autoCompleteCity.text.toString().isEmpty()){
                citySpinner.error = getString(R.string.please_select_city)
                isValid = false
            }
            if (autoCompleteRegion.text.toString().isEmpty()){
                regionSpinner.error = getString(R.string.please_select_region)
                isValid = false
            }
            if (autoCompleteSubRegion.text.toString().isEmpty()){
                subRegionSpinner.error = getString(R.string.please_select_sub_region)
                isValid = false
            }
            autoCompletePurpose.setOnItemClickListener { parent, view, position, id ->
                purposeSpinner.error = null
            }
            autoCompleteCategoryType.setOnItemClickListener { parent, view, position, id ->
                categorySpinner.error = null

            }
            autoCompletePriorityType.setOnItemClickListener { parent, view, position, id ->
                prioritySpinner.error = null

            }
            autoCompleteFinishing.setOnItemClickListener { parent, view, position, id ->
                finishingSpinner.error = null

            }
            autoCompleteFloorFinishingType.setOnItemClickListener { parent, view, position, id ->
                floorFinishingTypeSpinner.error = null

            }
            autoCompleteType.setOnItemClickListener { parent, view, position, id ->
                typeSpinner.error = null

            }
            autoCompleteCountry.setOnItemClickListener { parent, view, position, id ->
                val countriesModel = parent.getItemAtPosition(position)
                Toast.makeText(requireContext(), countriesModel.toString(), Toast.LENGTH_SHORT).show()
                viewModel.getCities(countriesModel.toString())
                countrySpinner.error = null
            }
            autoCompleteCity.setOnItemClickListener { parent, view, position, id ->
                citySpinner.error = null

            }
            autoCompleteRegion.setOnItemClickListener { parent, view, position, id ->
                regionSpinner.error = null

            }
            autoCompleteSubRegion.setOnItemClickListener { parent, view, position, id ->
                subRegionSpinner.error = null

            }
        }
        return isValid
    }

}