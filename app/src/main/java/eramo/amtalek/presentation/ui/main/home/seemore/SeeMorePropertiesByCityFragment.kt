package eramo.amtalek.presentation.ui.main.home.seemore

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.data.remote.dto.myHome.allCitys.Data
import eramo.amtalek.databinding.FragmentSeeMorePropertiesByCityBinding
import eramo.amtalek.domain.model.project.AmenityModel
import eramo.amtalek.domain.model.property.CriteriaModel
import eramo.amtalek.domain.search.SearchDataListsModel
import eramo.amtalek.domain.search.SearchModelDto
import eramo.amtalek.presentation.adapters.recyclerview.RvPropertiesByCityAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
import eramo.amtalek.presentation.ui.main.home.HomeMyViewModel
import eramo.amtalek.presentation.ui.search.searchform.SearchFormViewModel
import eramo.amtalek.presentation.ui.search.searchresult.SearchResultFragmentArgs
import eramo.amtalek.util.navOptionsAnimation
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SeeMorePropertiesByCityFragment : BindingFragment<FragmentSeeMorePropertiesByCityBinding>(),
    RvPropertiesByCityAdapter.OnItemClickListener {
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSeeMorePropertiesByCityBinding::inflate

    val viewModel: HomeMyViewModel by viewModels()
    private val searchViewModel: SearchFormViewModel by viewModels()

    @Inject
    lateinit var rvPropertiesByCityAdapter: RvPropertiesByCityAdapter

    private var listOfPurposeItems = ArrayList<CriteriaModel>()
    private var listOfFinishingItems = ArrayList<CriteriaModel>()
    private var listOfTypeItems = ArrayList<CriteriaModel>()
    private var listOfCurrencyItems = ArrayList<CriteriaModel>()
    private var listOfAmenitiesItems = ArrayList<AmenityModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        viewModel.getAllCities()
        searchViewModel.getInitApis()
        lifecycleScope.launch {
            viewModel.allCities.collect { state ->
                Log.e("details in fragment", state?.data.toString())
                initRv(state?.data?: emptyList())
            }
        }
        fetchData()
    }

    private fun fetchData() {

        fetchCurrencies()
        fetchGetPropertyTypes()
        fetchGetPropertyFinishing()
        fetchGetAmenitiesState()
        fetchGetPropertyPurpose()
      //  fetchInitApis()
    }

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
                                listOfCurrencyItems.add(0, CriteriaModel(-1, -1, getString(R.string.currency),image = ""))
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
                                listOfTypeItems.add(0, CriteriaModel(-1, -1, getString(R.string.type), image = ""))
                                for (item in types) {
                                    listOfTypeItems.add(item as CriteriaModel)
                                }
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

    private fun setupToolbar() {
        binding.inToolbar.apply {
            tvTitle.text = getString(R.string.find_your_property_in_the_city)
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun initRv(data: List<Data?>) {
        rvPropertiesByCityAdapter.setListener(this@SeeMorePropertiesByCityFragment)
        binding.rv.adapter = rvPropertiesByCityAdapter
        rvPropertiesByCityAdapter.submitList(data)

        setupSearchRv(data)
    }

    private fun setupSearchRv(data: List<Data?>) {
        binding.inToolbar.etSearch.addTextChangedListener { text ->
            if (text.toString().isEmpty()) {
                rvPropertiesByCityAdapter.submitList(data)
            } else {
                val list = data.filter {
                    it?.titleEn?.lowercase()?.contains(text.toString().lowercase()) ?: true
                }
                rvPropertiesByCityAdapter.submitList(null)
                rvPropertiesByCityAdapter.submitList(list)
            }
        }
    }

    private fun createModel(data: Data): SearchModelDto {
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
            val currencyId =0

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
                city = data.id,
                priority_keys = ""
            )
            return myModel
        }
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
    override fun onCityClick(model: Data) {
        findNavController().navigate(
            R.id.searchResultFragment,
            SearchResultFragmentArgs(
                createModel(model), createListsModel()
            ).toBundle(), navOptionsAnimation()
        )
    }
}