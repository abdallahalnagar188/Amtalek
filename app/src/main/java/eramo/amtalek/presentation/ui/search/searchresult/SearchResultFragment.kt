package eramo.amtalek.presentation.ui.search.searchresult

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentSearchResultBinding
import eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel
import eramo.amtalek.domain.model.property.CriteriaModel
import eramo.amtalek.domain.search.SearchDataListsModel
import eramo.amtalek.presentation.adapters.spinner.CriteriaSpinnerAdapter
import eramo.amtalek.presentation.adapters.spinner.CriteriaSpinnerSmallAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.interfaces.FavClickListener
import eramo.amtalek.presentation.ui.main.home.details.properties.PropertyDetailsFragmentArgs
import eramo.amtalek.presentation.ui.search.searchform.SearchFormViewModel
import eramo.amtalek.util.UserUtil
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchResultFragment : BindingFragment<FragmentSearchResultBinding>()
    ,RvSearchResultsPropertiesAdapter.OnItemClickListener,
    FavClickListener {
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSearchResultBinding::inflate


    val args by navArgs<SearchResultFragmentArgs>()
    val model get() = args.searchQuery
    private val dataLists get() = args.dataLists

    val viewModel by viewModels<SearchFormViewModel>()

    private var selectedPurposeId:Int? =null
    private var selectedFinishingId:Int? = null
    private var selectedTypeId:Int? = null
    private var selectedCurrencyId:Int? = null

    @Inject
    lateinit var propertiesAdapter: RvSearchResultsPropertiesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolBar()
        initViews()
        fetchData()
        requestData()
        setupSpinners(dataLists)
        Log.e("model", model.toString(), )
        Log.e("country", UserUtil.getUserCountryFiltrationTitleId(), )
        Log.e("city", UserUtil.getCityFiltrationId(), )
        Log.e("amenty", model.amenitiesListIds.toString(), )
    }

    private fun setupSpinners(dataLists: SearchDataListsModel) {
       setupFinishingSpinner(dataLists.listOfFinishingItems.toMutableList())
        setupCurrenciesSpinner(dataLists.listOfCurrencyItems.toMutableList())
        setupTypesSpinner(dataLists.listOfTypesItems.toMutableList())
        setupPurposeSpinner(dataLists.listOfPurposeItems.toMutableList())
    }

    private fun requestData() {
        viewModel.search(
            city = UserUtil.getCityFiltrationId(),
            propertyType = model.propertyTypeId,
            minPrice = model.minPrice,
            maxPrice = model.maxPrice,
            minArea = model.minArea,
            maxArea = model.maxArea,
            minBeds = model.bedroomsNumber,
            minBathes =model.bathroomsNumber,
            country = UserUtil.getUserCountryFiltrationTitleId(),
            currency = if (model.currencyId==-1) null else model.currencyId,
            finishing = model.propertyFinishingId,
            keyword = model.searchKeyWords,
            priceArrangeKeys = "asc",
            purpose = model.purposeId,
            region = model.locationId,
            subRegion = null,
            amenitiesListIds = if (model.amenitiesListIds?.isEmpty() == true) "" else model.amenitiesListIds.toString())
    }

    private fun fetchData() {
        lifecycleScope.launch {
            viewModel.searchState.collect(){data->
                data?.let {
                    propertiesAdapter.submitData(viewLifecycleOwner.lifecycle, data)
                }
            }
        }
    }

    private fun initViews() {
        binding.rvProperties.adapter = propertiesAdapter
        propertiesAdapter.setListener(this@SearchResultFragment,this@SearchResultFragment)
    }

    private fun setupToolBar() {
        binding.inToolbar.apply {
            tvTitle.text = getString(R.string.find_your_property)
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }


    override fun onFavClick(model: PropertyModel) {
        viewModel.addOrRemoveFav(model.id)
    }

    override fun onPropertyClicks(model: PropertyModel) {
        findNavController().navigate(R.id.propertyDetailsFragment,
            PropertyDetailsFragmentArgs(model.listingNumber).toBundle())

    }
    private fun setupFinishingSpinner(data:MutableList<CriteriaModel>) {
        binding.apply {

            val criteriaSpinnerAdapter = CriteriaSpinnerSmallAdapter(requireContext(), data)
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

            val criteriaSpinnerAdapter = CriteriaSpinnerSmallAdapter(requireContext(), data)
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

            val criteriaSpinnerAdapter = CriteriaSpinnerSmallAdapter(requireContext(), data)
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

            val criteriaSpinnerAdapter = CriteriaSpinnerSmallAdapter(requireContext(), data)
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