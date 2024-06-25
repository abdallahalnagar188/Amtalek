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
import eramo.amtalek.presentation.adapters.spinner.CriteriaSpinnerAdapter
import eramo.amtalek.presentation.adapters.spinner.RegionsSpinnerAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
import eramo.amtalek.util.LocalUtil
import eramo.amtalek.util.navOptionsAnimation
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddPropertyThirdFragment : BindingFragment<FragmentAddPropertyThirdBinding>(){
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentAddPropertyThirdBinding::inflate
    private val viewModel by viewModels<AddPropertyThirdFragmentViewModel>()
    private var selectedCountryId = -1
    private var selectedCityId = -1
    private var selectedRegionId = -1
    private var selectedSubRegionId = -1
    private var selectedPurposeId = -1
    private var selectedCategoryId = -1
    private var selectedFinishingId = -1
    private var selectedFloorFinishingId = -1
    private var selectedTypeId = -1

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
        binding.autoCompletePriorityType.setSelection(0)
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
                            var types = it.data
                            setupCategoriesSpinner(types!!.toMutableList())
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
                            setupFloorFinishingSpinner(types!!.toMutableList())
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
//                            val typesArray:ArrayList<CountryModel> = ArrayList()
                            setupCountriesSpinner(types!!.toMutableList())
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
//                            setupCitySpinners(types!!,binding.autoCompleteType)
                            if (types != null) {
                                setupCitiesSpinner(types.toMutableList())
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
                            if (types != null) {
                                setupRegionsSpinner(types.toMutableList())
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
                            if (types != null) {
                                setupSubRegionsSpinner(types.toMutableList())
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
    private fun setupCountriesSpinner(data: MutableList<CountryModel>) {
        binding.apply {
            data.add(0, CountryModel(-1, imageUrl = "", name = getString(R.string.no_selection) ))
            val countriesSpinnerAdapter = CountriesSpinnerAdapter(requireContext(), data)
            countrySpinner.adapter = countriesSpinnerAdapter

            countrySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val model = parent?.getItemAtPosition(position) as CountryModel
                    selectedCountryId = model.id
                    viewModel.getCities(model.id.toString())
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

            // refresh onClick if getting list fail
            countrySpinner.setOnTouchListener { view, motionEvent ->
                when (motionEvent?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        view.performClick()
                        if (data.isEmpty()) {
                            viewModel.getCountries()
                        }
                    }
                }
                return@setOnTouchListener true
            }
        }
    }
    private fun setupCitiesSpinner(data: MutableList<CityModel>) {
        binding.apply {
            data.add(0, CityModel(id=-1, titleEn = getString(R.string.no_selection), titleAr = getString(R.string.no_selection), image = "", properties = -1, rentProperties = -1, saleProperties = -1 ))
            val citiesSpinnerAdapter = CitiesSpinnerAdapter(requireContext(), data)
            citySpinner.adapter = citiesSpinnerAdapter

            citySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val model = parent?.getItemAtPosition(position) as CityModel

                    selectedCityId = model.id
                    viewModel.getRegions(model.id.toString())

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

            // refresh onClick if getting list fail
            citySpinner.setOnTouchListener { view, motionEvent ->
                when (motionEvent?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        view.performClick()
                        if (data.isEmpty()) {
                            viewModel.getCountries()
                        }
                    }
                }
                return@setOnTouchListener true
            }
        }
    }
    private fun setupRegionsSpinner(data: MutableList<RegionModel>) {
        binding.apply {
            data.add(0, RegionModel(id=-1, name = getString(R.string.no_selection)))
            val regionsSpinnerAdapter = RegionsSpinnerAdapter(requireContext(), data)
            regionSpinner.adapter = regionsSpinnerAdapter

            regionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val model = parent?.getItemAtPosition(position) as RegionModel
                    selectedRegionId = model.id
                    viewModel.getSubRegions(model.id.toString())
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

            // refresh onClick if getting list fail
            regionSpinner.setOnTouchListener { view, motionEvent ->
                when (motionEvent?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        view.performClick()
                        if (data.isEmpty()) {
                            viewModel.getCountries()
                        }
                    }
                }
                return@setOnTouchListener true
            }
        }
    }
    private fun setupSubRegionsSpinner(data: MutableList<RegionModel>) {
        binding.apply {
            data.add(0, RegionModel(id=-1, name = getString(R.string.no_selection)))
            val regionsSpinnerAdapter = RegionsSpinnerAdapter(requireContext(), data)
            subRegionSpinner.adapter = regionsSpinnerAdapter

            subRegionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val model = parent?.getItemAtPosition(position) as RegionModel
                    selectedSubRegionId = model.id
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

            // refresh onClick if getting list fail
            subRegionSpinner.setOnTouchListener { view, motionEvent ->
                when (motionEvent?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        view.performClick()
                        if (data.isEmpty()) {
                            viewModel.getCountries()
                        }
                    }
                }
                return@setOnTouchListener true
            }
        }
    }
    private fun setupSpinners(data:List<CriteriaModel>) {
        binding.apply {
            val criteriaSpinnerAdapter = CriteriaSpinnerAdapter(requireContext(), data)
            subRegionSpinner.adapter = criteriaSpinnerAdapter

            subRegionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val model = parent?.getItemAtPosition(position) as RegionModel
                    selectedSubRegionId = model.id
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

            // refresh onClick if getting list fail
            subRegionSpinner.setOnTouchListener { view, motionEvent ->
                when (motionEvent?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        view.performClick()
                        if (data.isEmpty()) {
                            viewModel.getCountries()
                        }
                    }
                }
                return@setOnTouchListener true
            }
        }

    }
    private fun setupCategoriesSpinner(data:MutableList<CriteriaModel>) {
        binding.apply {
            data.add(0, CriteriaModel(-1,-1, getString(R.string.no_selection)))
            val criteriaSpinnerAdapter = CriteriaSpinnerAdapter(requireContext(), data)
            categorySpinner.adapter = criteriaSpinnerAdapter
            categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val model = parent?.getItemAtPosition(position) as CriteriaModel
                    selectedCategoryId = model.id
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
            // refresh onClick if getting list fail
            categorySpinner.setOnTouchListener { view, motionEvent ->
                when (motionEvent?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        view.performClick()
                        if (data.isEmpty()) {
                            viewModel.getCountries()
                        }
                    }
                }
                return@setOnTouchListener true
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
                            viewModel.getCountries()
                        }
                    }
                }
                return@setOnTouchListener true
            }
        }

    }
    private fun setupFloorFinishingSpinner(data:MutableList<CriteriaModel>) {
        binding.apply {
            data.add(0, CriteriaModel(-1,-1, getString(R.string.no_selection)))

            val criteriaSpinnerAdapter = CriteriaSpinnerAdapter(requireContext(), data)
            floorFinishingTypeSpinner.adapter = criteriaSpinnerAdapter

            floorFinishingTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val model = parent?.getItemAtPosition(position) as CriteriaModel
                    selectedFloorFinishingId = model.id
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

            // refresh onClick if getting list fail
            floorFinishingTypeSpinner.setOnTouchListener { view, motionEvent ->
                when (motionEvent?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        view.performClick()
                        if (data.isEmpty()) {
                            viewModel.getCountries()
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
                            viewModel.getCountries()
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
                            viewModel.getCountries()
                        }
                    }
                }
                return@setOnTouchListener true
            }
        }
    }
    private fun requestApis() {
        viewModel.getInitApis()
    }

    private fun clickListeners() {
        binding.btnNext.setOnClickListener {
            if (formValidation()){
                findNavController().navigate(R.id.addPropertyFourthFragment,null, navOptionsAnimation())
            }else{
                return@setOnClickListener
            }
        }
    }

    private fun setUpViews() {
        binding.autoCompletePriorityType.inputType = InputType.TYPE_NULL
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
            if (selectedPurposeId==-1){
               isValid = false
            }
            if (selectedCategoryId==-1){
                isValid = false
            }
            if (autoCompletePriorityType.text.toString()==getString(R.string.no_selection)){
                isValid = false
            }
            if (selectedFinishingId==-1){
                isValid = false
            }
            if (selectedFloorFinishingId==-1){
                isValid = false
            }
            if (selectedTypeId==-1){
                isValid = false
            }
            if (selectedCountryId==-1){
                isValid = false
            }
            if (selectedCityId==-1){
                isValid = false
            }
            if (selectedRegionId==-1){
                isValid = false
            }
            if (selectedSubRegionId==-1){
                isValid = false
            }
        }
        if (!isValid){
            Toast.makeText(requireContext(), getString(R.string.please_complete_all_fields), Toast.LENGTH_SHORT).show()
        }
        return isValid
    }

}