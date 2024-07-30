package eramo.amtalek.presentation.ui.main.home.seemore

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navOptions
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.data.remote.dto.myHome.allCitys.Data
import eramo.amtalek.databinding.FragmentSeeMorePropertiesByCityBinding
import eramo.amtalek.domain.model.main.home.PropertiesByCityModel
import eramo.amtalek.presentation.adapters.recyclerview.RvPropertiesByCityAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.main.home.HomeMyViewModel
import eramo.amtalek.presentation.ui.main.home.details.projects.MyProjectDetailsFragmentArgs
import eramo.amtalek.presentation.ui.main.home.details.properties.PropertyDetailsFragmentArgs
import eramo.amtalek.presentation.ui.search.searchresult.SearchResultFragmentArgs
import eramo.amtalek.util.navOptionsAnimation
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SeeMorePropertiesByCityFragment : BindingFragment<FragmentSeeMorePropertiesByCityBinding>(),
    RvPropertiesByCityAdapter.OnItemClickListener {
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSeeMorePropertiesByCityBinding::inflate

    private val args by navArgs<SeeMorePropertiesByCityFragmentArgs>()
    private val citiesList get() = args.citiesList
    val viewModel: HomeMyViewModel by viewModels()

    val argsTwo by navArgs<SearchResultFragmentArgs>()
    val searchQuery get() = argsTwo.searchQuery
    private val dataLists get() = argsTwo.dataLists

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


    override fun onCityClick(model: Data) {
//        findNavController().navigate(
//            R.id.searchResultFragment,
//            SearchResultFragmentArgs(
//                searchQuery,
//                dataLists
//            ).toBundle(), navOptionsAnimation()
//        )
    }
}