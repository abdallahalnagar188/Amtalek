package eramo.amtalek.presentation.ui.search.searchresult

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentSearchPropertyResultBinding
import eramo.amtalek.databinding.FragmentSearchResultBinding
import eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel
import eramo.amtalek.presentation.adapters.recyclerview.offers.RvHotOffersForBothPropertiesAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.interfaces.FavClickListener
import eramo.amtalek.presentation.ui.search.searchform.SearchFormViewModel
import eramo.amtalek.presentation.viewmodel.navbottom.extension.SearchViewModel
import javax.inject.Inject

@AndroidEntryPoint
class SearchResultFragment : BindingFragment<FragmentSearchResultBinding>(),RvHotOffersForBothPropertiesAdapter.OnItemClickListener,
    FavClickListener {
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSearchResultBinding::inflate


    val args by navArgs<SearchResultFragmentArgs>()
    val model get() = args.searchQuery

    val viewModel by viewModels<SearchFormViewModel>()



    @Inject
    lateinit var propertiesAdapter: RvHotOffersForBothPropertiesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolBar()
        initViews()
        fetchData()
    }

    private fun fetchData() {
//        lifecycleScope.launchWhenStarted {
//            viewModel.searchState.collect(){alo->
//                val data = a
//                propertiesAdapter.submitList(alo)
//            }
//        }
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

    override fun onPropertyClickForBoth(model: PropertyModel) {
        //
    }

    override fun onFavClick(model: PropertyModel) {
        //
    }

}