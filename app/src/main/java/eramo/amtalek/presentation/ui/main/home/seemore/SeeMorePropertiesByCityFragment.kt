package eramo.amtalek.presentation.ui.main.home.seemore

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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
import eramo.amtalek.presentation.ui.main.home.HomeMyViewModel
import eramo.amtalek.presentation.ui.search.searchresult.SearchResultFragmentArgs
import eramo.amtalek.util.navOptionsAnimation
import eramo.amtalek.util.selectedLocation
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SeeMorePropertiesByCityFragment : BindingFragment<FragmentSeeMorePropertiesByCityBinding>(),
    RvPropertiesByCityAdapter.OnItemClickListener {
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSeeMorePropertiesByCityBinding::inflate

    val viewModel: HomeMyViewModel by viewModels()

    @Inject
    lateinit var rvPropertiesByCityAdapter: RvPropertiesByCityAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        viewModel.getAllCities()
        lifecycleScope.launch {
            viewModel.allCities.collect { state ->
                Log.e("details in fragment", state?.data.toString())
                initRv(state?.data?: emptyList())
            }
        }
            //   fetchData(countryId)
    }

//    private fun setupViews() {
//
//
//        initRv(citiesList.toList())
//    }

    private fun fetchData(countryId: String) {

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
            )
            return myModel
        }
    }

    private var listOfPurposeItems = ArrayList<CriteriaModel>()
    private var listOfFinishingItems = ArrayList<CriteriaModel>()
    private var listOfTypeItems = ArrayList<CriteriaModel>()
    private var listOfCurrencyItems = ArrayList<CriteriaModel>()
    private var listOfAmenitiesItems = ArrayList<AmenityModel>()
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