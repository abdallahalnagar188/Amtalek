package eramo.amtalek.presentation.ui.search.searchresult

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentSearchResultBinding
import eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.interfaces.FavClickListener
import eramo.amtalek.presentation.ui.main.home.details.properties.PropertyDetailsFragmentArgs
import eramo.amtalek.presentation.ui.search.searchform.SearchFormViewModel
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

    val viewModel by viewModels<SearchFormViewModel>()



    @Inject
    lateinit var propertiesAdapter: RvSearchResultsPropertiesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolBar()
        initViews()
        fetchData()
        requestData()
        Log.e("MPdededede", model.toString(), )
    }

    private fun requestData() {
        viewModel.search(
            city = null,
            propertyType = null,
            minPrice = null,
            maxPrice = null,
            minArea = null,
            maxArea = null,
            minBeds = null,
            minBathes =null,
            country = null,
            currency = null,
            finishing = null,
            keyword = null,
            priceArrangeKeys = null,
            purpose = null,
            region = null,
            subRegion = null,
        )
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

}