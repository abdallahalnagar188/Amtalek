package eramo.amtalek.presentation.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.databinding.FragmentFilterCitiesDialogBinding
import eramo.amtalek.domain.model.auth.CityModel
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeFilterCities
import eramo.amtalek.presentation.viewmodel.navbottom.extension.FilterCitiesDialogViewModel
import eramo.amtalek.util.showToast
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.delay
import javax.inject.Inject

@AndroidEntryPoint
//class FilterCitiesDialogFragment : DialogFragment(R.layout.fragment_filter_cities_dialog),
class FilterCitiesDialogFragment : BottomSheetDialogFragment(),
    RvHomeFilterCities.OnItemClickListener {

    private var binding: FragmentFilterCitiesDialogBinding? = null
    private lateinit var listener: FilterCitiesDialogOnClickListener

    @Inject
    lateinit var rvHomeFilterCities: RvHomeFilterCities

    private val viewModel by viewModels<FilterCitiesDialogViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFilterCitiesDialogBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                            delay(1000)
                            dismissShimmerEffect()

                            setupCitiesRv(state.data ?: emptyList())

                            binding?.tvTitle?.setOnClickListener {
                                listener.onFilterCitiesDialogItemClick(state.data?.get(0)!!)
                            }
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

    private fun setupCitiesRv(data: List<CityModel>) {
        rvHomeFilterCities.setListener(this@FilterCitiesDialogFragment)
        binding?.rvCities?.adapter = rvHomeFilterCities

        rvHomeFilterCities.submitList(data)
    }

    private fun showShimmerEffect() {
        binding?.apply {

            viewLayout.visibility = View.GONE
            tvTitle.visibility = View.GONE
            shimmerLayout.visibility = View.VISIBLE
            shimmerLayout.startShimmer()
        }
    }

    private fun dismissShimmerEffect() {
        binding?.apply {

            viewLayout.visibility = View.VISIBLE
            tvTitle.visibility = View.VISIBLE
            shimmerLayout.visibility = View.GONE
            shimmerLayout.stopShimmer()
        }
    }

    fun setListener(listener: FilterCitiesDialogOnClickListener) {
        this.listener = listener
    }

    interface FilterCitiesDialogOnClickListener {
        fun onFilterCitiesDialogItemClick(model: CityModel)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onItemClick(model: CityModel) {
        listener.onFilterCitiesDialogItemClick(model)
        this@FilterCitiesDialogFragment.dismiss()
    }
}