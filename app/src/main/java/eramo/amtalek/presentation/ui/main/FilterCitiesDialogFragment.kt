package eramo.amtalek.presentation.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentFilterCitiesDialogBinding
import eramo.amtalek.domain.model.auth.CityModel
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeFilterCities
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
import eramo.amtalek.presentation.viewmodel.navbottom.extension.FilterCitiesDialogViewModel
import eramo.amtalek.util.showToast
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.delay
import javax.inject.Inject

@AndroidEntryPoint
class FilterCitiesDialogFragment : DialogFragment(R.layout.fragment_filter_cities_dialog),RvHomeFilterCities.OnItemClickListener {
    private var binding: FragmentFilterCitiesDialogBinding? = null

    @Inject
    lateinit var rvHomeFilterCities: RvHomeFilterCities

    private val viewModel by viewModels<FilterCitiesDialogViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFilterCitiesDialogBinding.bind(view)

        viewModel.getCities("1")

        fetchCities()
    }

    private fun fetchCities() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.citiesState.collect { state ->
                    when (state) {
                        is UiState.Loading -> {
                            showShimmerEffect()

                        }

                        is UiState.Success -> {
                            dismissShimmerEffect()
                            setupCitiesRv(state.data ?: emptyList())
                        }

                        is UiState.Error -> {
                            dismissShimmerEffect()
                            val errorMessage = state.message!!.asString(requireContext())
                            showToast(errorMessage)
                        }

                        else -> {}
                    }

                }
            }
        }
    }

    private fun setupCitiesRv(data:List<CityModel>){
        rvHomeFilterCities.setListener(this@FilterCitiesDialogFragment)
        binding?.rvCities?.adapter = rvHomeFilterCities

        rvHomeFilterCities.submitList(data)
    }

    private fun showShimmerEffect() {
        binding?.apply {
            shimmerLayout.startShimmer()

            viewLayout.visibility = View.GONE
            shimmerLayout.visibility = View.VISIBLE
        }
    }

    private fun dismissShimmerEffect() {
        binding?.apply {
            shimmerLayout.stopShimmer()

            viewLayout.visibility = View.VISIBLE
            shimmerLayout.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onItemClick(model: CityModel) {


    }
}