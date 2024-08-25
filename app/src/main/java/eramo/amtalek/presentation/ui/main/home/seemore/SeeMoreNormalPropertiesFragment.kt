package eramo.amtalek.presentation.ui.main.home.seemore

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.data.remote.dto.property.allproperty.DataX
import eramo.amtalek.databinding.FragmentSeeMorePropertiesBinding
import eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel
import eramo.amtalek.domain.model.main.home.PropertyModelx
import eramo.amtalek.presentation.adapters.recyclerview.RvNormalPropertiesAdapter
import eramo.amtalek.presentation.adapters.recyclerview.RvSimilarPropertiesAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.main.home.HomeMyViewModel
import eramo.amtalek.presentation.ui.main.home.details.properties.PropertyDetailsFragmentArgs
import eramo.amtalek.presentation.ui.main.home.details.properties.PropertyDetailsSellAndRentFragmentArgs
import eramo.amtalek.presentation.ui.main.home.details.properties.PropertyDetailsSellFragmentArgs
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.enum.PropertyType
import eramo.amtalek.util.navOptionsAnimation
import eramo.amtalek.util.showToast
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SeeMoreNormalPropertiesFragment : BindingFragment<FragmentSeeMorePropertiesBinding>(),
    RvNormalPropertiesAdapter.OnItemClickListener,RvNormalPropertiesAdapter.OnFavClickListener {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSeeMorePropertiesBinding::inflate


    val viewModel: HomeMyViewModel by viewModels()
    @Inject
    lateinit var rvPropertiesAdapter: RvNormalPropertiesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getHomeNormalProperties(UserUtil.getCityFiltrationId())
        setupRecyclerView()
        setupToolbar()
        fetchProperties()
        fetchAddRemoveToFavState()
    }


    private fun setupToolbar() {
        binding.inToolbar.apply {
            tvTitle.text = getString(R.string.all_normal_propertties)
 //           tvTitle.text = "Featured Properties in Egypt"
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

//    private fun fetchHomeNormalProperties() {
//        viewLifecycleOwner.lifecycleScope.launch {
//            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.homeNormalPropertiesState.collect() { state ->
//                    when (state) {
//                        is UiState.Success -> {
//                            val data = state.data?.get(0)?.propertiesList
//                            setupRv(data?: emptyList())
//                            setupViews(data?: emptyList())
//                            binding.inToolbar.tvTitle.text = state.data?.get(0)?.title
//                            if (!data.isNullOrEmpty()) {
//                                setupRv(data)
//                            } else {
//                                binding.rvProperties.rootView.visibility = View.GONE
//                            }
//                        }
//
//                        is UiState.Error -> {
//                            val errorMessage = state.message!!.asString(requireContext())
//                            showToast(errorMessage)
//                        }
//
//                        is UiState.Loading -> {
//                        }
//
//                        else -> {}
//                    }
//
//                }
//            }
//        }
//    }

    private fun setupRecyclerView() {
        rvPropertiesAdapter.setListener(this,this)
        binding.rvProperties.adapter = rvPropertiesAdapter
    }

    private fun fetchProperties() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.allNormalPropertyPagingFlow.collectLatest { pagingData ->
                    rvPropertiesAdapter.submitData(pagingData)
                    rvPropertiesAdapter.notifyDataSetChanged()

                }
            }
        }
    }

    private fun fetchAddRemoveToFavState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.favState.collect() { state ->
                    when (state) {

                        is UiState.Success -> {
                            showToast(state.data?.message.toString())

                            // viewModel.getAllProperty()
                        }

                        is UiState.Error -> {
                            val errorMessage = state.message!!.asString(requireContext())
                            showToast(errorMessage)
                        }

                        is UiState.Loading -> {
                        }

                        else -> {}
                    }
                }
            }
        }


    }

    override fun onPropertyClick(model: DataX) {
        when (model.forWhat) {
            PropertyType.FOR_SELL.key,
            PropertyType.FOR_RENT.key,
            PropertyType.FOR_BOTH.key -> {
                findNavController().navigate(
                    R.id.propertyDetailsFragment,
                    model.listingNumber?.let { PropertyDetailsFragmentArgs(it).toBundle() }
                )
            }
        }
    }


    override fun onFavClick(model: DataX) {
        viewModel.addOrRemoveFav(model.id?:0)
    }
}