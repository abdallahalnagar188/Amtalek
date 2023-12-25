package eramo.amtalek.presentation.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.databinding.FragmentFilterCitiesDialogBinding
import eramo.amtalek.domain.model.auth.CityModel
import eramo.amtalek.domain.model.auth.CountryModel
import eramo.amtalek.presentation.adapters.recyclerview.RvSelectCityAdapter
import eramo.amtalek.presentation.adapters.recyclerview.RvSelectCountryAdapter
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeFilterCities
import eramo.amtalek.presentation.viewmodel.navbottom.extension.FilterCitiesDialogViewModel
import eramo.amtalek.util.showToast
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
//class FilterCitiesDialogFragment : DialogFragment(R.layout.fragment_filter_cities_dialog),
class FilterCitiesDialogFragment : BottomSheetDialogFragment(), RvSelectCountryAdapter.OnItemClickListener,
    RvSelectCityAdapter.OnItemClickListener,
    RvHomeFilterCities.OnItemClickListener {

    private var binding: FragmentFilterCitiesDialogBinding? = null
    private lateinit var listener: FilterCitiesDialogOnClickListener

    @Inject
    lateinit var rvHomeFilterCities: RvHomeFilterCities

    /////////////////////////////////////////////////////////

    @Inject
    lateinit var rvSelectCountryAdapter: RvSelectCountryAdapter

    @Inject
    lateinit var rvSelectCityAdapter: RvSelectCityAdapter


    private val viewModel by viewModels<FilterCitiesDialogViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFilterCitiesDialogBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dialog = dialog as BottomSheetDialog
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED

        setupViews()
        listeners()

//        viewModel.getCities("1")
//        fetchCities()
    }

    private fun listeners() {
        selectDefaultCountryAndCity()
    }

    private fun setupViews() {
        initCountriesRv(generateCountries())
        initCitiesRv(generateCities())

        dismissShimmerEffect()
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

//                            setupCitiesRv(state.data ?: emptyList())

//                            binding?.tvTitle?.setOnClickListener {
//                                listener.onFilterCitiesDialogItemClick(state.data?.get(0)!!)
//                            }
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
//        rvHomeFilterCities.setListener(this@FilterCitiesDialogFragment)
        binding?.rvCities?.adapter = rvHomeFilterCities

        rvHomeFilterCities.submitList(data)
    }

    private fun initCountriesRv(data: List<CountryModel>) {
        rvSelectCountryAdapter.setListener(this@FilterCitiesDialogFragment)
        binding?.rvCountries?.adapter = rvSelectCountryAdapter
        rvSelectCountryAdapter.submitList(data)
    }

    private fun initCitiesRv(data: List<CountryModel>) {
        rvSelectCityAdapter.setListener(this@FilterCitiesDialogFragment)
        binding?.rvCities?.adapter = rvSelectCityAdapter
        rvSelectCityAdapter.submitList(data)
    }

    private fun generateCountries(): List<CountryModel> {
        val list = mutableListOf<CountryModel>()

        list.add(
            CountryModel(
                1, "Egypt",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/f/fe/Flag_of_Egypt.svg/220px-Flag_of_Egypt.svg.png"
            )
        )

        list.add(
            CountryModel(
                2, "KSA",
                "https://cdn.britannica.com/79/5779-050-46C999AF/Flag-Saudi-Arabia.jpg"
            )
        )

        list.add(
            CountryModel(
                3, "UAE",
                "https://cdn.pixabay.com/photo/2012/04/10/23/01/united-arab-emirates-26815_640.png"
            )
        )

        return list
    }

    private fun generateCities(): List<CountryModel> {
        val list = mutableListOf<CountryModel>()

        list.add(
            CountryModel(
                1, "Cairo",
                "https://cdn.pixabay.com/photo/2012/04/10/23/01/united-arab-emirates-26815_640.png"
            )
        )

        list.add(
            CountryModel(
                2, "Alex",
                "https://cdn.britannica.com/79/5779-050-46C999AF/Flag-Saudi-Arabia.jpg"
            )
        )

        list.add(
            CountryModel(
                3, "Mansoura",
                "https://cdn.pixabay.com/photo/2012/04/10/23/01/united-arab-emirates-26815_640.png"
            )
        )
        list.add(
            CountryModel(
                1, "Cairo",
                "https://cdn.pixabay.com/photo/2012/04/10/23/01/united-arab-emirates-26815_640.png"
            )
        )

        list.add(
            CountryModel(
                2, "Alex",
                "https://cdn.britannica.com/79/5779-050-46C999AF/Flag-Saudi-Arabia.jpg"
            )
        )

        list.add(
            CountryModel(
                3, "Mansoura",
                "https://cdn.pixabay.com/photo/2012/04/10/23/01/united-arab-emirates-26815_640.png"
            )
        )

        return list
    }

    private fun selectDefaultCountryAndCity() {
        viewLifecycleOwner.lifecycleScope.launch {
            delay(100)
            binding?.rvCountries?.findViewHolderForAdapterPosition(0)?.itemView?.performClick()
            binding?.rvCities?.findViewHolderForAdapterPosition(0)?.itemView?.performClick()
        }
    }

    private fun showShimmerEffect() {
        binding?.apply {

            viewLayout.visibility = View.GONE
//            tvTitle.visibility = View.GONE
            shimmerLayout.visibility = View.VISIBLE
            shimmerLayout.startShimmer()
        }
    }

    private fun dismissShimmerEffect() {
        binding?.apply {

            viewLayout.visibility = View.VISIBLE
//            tvTitle.visibility = View.VISIBLE
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

    override fun onCityClick(model: CountryModel) {
        binding?.btnTvCityName?.text = model.name
    }

    override fun onCountryClick(model: CountryModel) {
        binding?.btnIvCountryFlag?.let {
            Glide.with(requireContext()).load(model.imageUrl).into(it)
        }
    }
}