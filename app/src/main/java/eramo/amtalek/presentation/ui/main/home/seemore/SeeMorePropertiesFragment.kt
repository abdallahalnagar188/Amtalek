package eramo.amtalek.presentation.ui.main.home.seemore

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.data.remote.dto.property.allproperty.DataX
import eramo.amtalek.databinding.FragmentSeeMorePropertiesBinding
import eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel
import eramo.amtalek.presentation.adapters.recyclerview.RvNormalPropertiesAdapter
import eramo.amtalek.presentation.adapters.recyclerview.RvPropertiesAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.main.home.HomeMyViewModel
import eramo.amtalek.presentation.ui.main.home.details.properties.PropertyDetailsFragmentArgs
import eramo.amtalek.util.enum.PropertyType
import eramo.amtalek.util.showToast
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SeeMorePropertiesFragment : BindingFragment<FragmentSeeMorePropertiesBinding>(),
    RvPropertiesAdapter.OnItemClickListener, RvNormalPropertiesAdapter.OnFavClickListener {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSeeMorePropertiesBinding::inflate

    private val viewModel: HomeMyViewModel by viewModels()

    @Inject
    lateinit var rvPropertiesPagingAdapter: RvPropertiesAdapter

    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupRecyclerView()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.allPropertiesPagingData.collect { pagingData ->
                    rvPropertiesPagingAdapter.submitData(pagingData)
                }
            }
        }
        fetchAddRemoveToFavState()
    }

    private fun setupToolbar() {
        binding.inToolbar.apply {
            tvTitle.text = getString(R.string.featured_properties_in_egypt)
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun setupRecyclerView() {
        rvPropertiesPagingAdapter.setListener(this, this)
        binding.rvProperties.adapter = rvPropertiesPagingAdapter
    }


    private fun fetchAddRemoveToFavState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.favState.collect { state ->
                    when (state) {
                        is UiState.Success -> {
                            showToast(state.data?.message.toString())
                            viewModel.allPropertiesPagingData.collect{
                                rvPropertiesPagingAdapter.submitData(it)
                            }
                            // Refresh the PagingData if necessary
                            //rvPropertiesPagingAdapter.refresh()
                        }
                        is UiState.Error -> {
                            val errorMessage = state.message!!.asString(requireContext())
                            showToast(errorMessage)
                        }
                        is UiState.Loading -> {
                            // Handle loading state if necessary
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
        model.id?.let { viewModel.addOrRemoveFav(it) }

    }
}
