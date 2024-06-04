package eramo.amtalek.presentation.ui.dialog.filtercitydialogfragment

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentFilterCitiesDialogBinding
import eramo.amtalek.domain.model.auth.CityModel
import eramo.amtalek.domain.model.auth.CountryModel
import eramo.amtalek.presentation.adapters.recyclerview.RvSelectCityAdapter
import eramo.amtalek.presentation.adapters.recyclerview.RvSelectCountryAdapter
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeFilterCities
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
import eramo.amtalek.presentation.viewmodel.navbottom.extension.FilterCitiesDialogViewModel
import eramo.amtalek.util.LocalUtil
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.navOptionsAnimation
import eramo.amtalek.util.showToast
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FilterCitiesDialogFragment : BottomSheetDialogFragment(), RvSelectCountryAdapter.OnItemClickListener,
    RvSelectCityAdapter.OnItemClickListener{

    private  lateinit var binding: FragmentFilterCitiesDialogBinding


    private var cityId = -1
    private var countryId = -1
    private var cityNameEn=""
    private var cityNameAr=""


    /////////////////////////////////////////////////////////

    @Inject
    lateinit var rvSelectCountryAdapter: RvSelectCountryAdapter

    @Inject
    lateinit var rvSelectCityAdapter: RvSelectCityAdapter


    private val viewModel by viewModels<FilterCitiesDialogViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_filter_cities_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dialog = dialog as BottomSheetDialog
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED

        binding = FragmentFilterCitiesDialogBinding.bind(view)
        viewModel.getCountries()


        setupViews()
        listeners()
        fetchCities()
        fetchCountries()
    }



    private fun listeners() {
        binding.btnContinue.setOnClickListener(){
            UserUtil.saveUserCityFiltrationId(cityId.toString())
            UserUtil.saveUserCountryFiltrationTitleId(countryId.toString())
            UserUtil.saveUserCityFiltrationTitleEn(cityNameEn)
            UserUtil.saveUserCityFiltrationTitleAr(cityNameAr)
            dismiss()
            findNavController().navigate(R.id.loadingFragment)
        }
    }
    private fun fetchCities() {
        viewLifecycleOwner.lifecycleScope.launch {
                viewModel.citiesState.collect { state ->
                    when (state) {
                        is UiState.Loading -> {
                            LoadingDialog.showDialog()
                        }
                        is UiState.Success -> {
                           initCitiesRv(state.data!!)
                            LoadingDialog.dismissDialog()
                        }
                        is UiState.Error -> {
                            LoadingDialog.dismissDialog()
                            val errorMessage = state.message!!.asString(requireContext())
                            showToast(errorMessage)
                        }
                        else -> {}
                    }
            }
        }
    }
    private fun fetchCountries() {
        viewLifecycleOwner.lifecycleScope.launch {
                viewModel.countriesState.collect { state ->
                    when (state) {
                        is UiState.Loading -> {
                            LoadingDialog.showDialog()
                        }
                        is UiState.Success -> {
                            initCountriesRv(state.data!!)
                            LoadingDialog.dismissDialog()
                        }
                        is UiState.Error -> {
                            LoadingDialog.dismissDialog()
                            val errorMessage = state.message!!.asString(requireContext())
                            showToast(errorMessage)
                        }
                        else -> {}
                    }
                }
        }
    }

    private fun initCountriesRv(data:List<CountryModel>) {
        rvSelectCountryAdapter.setListener(this@FilterCitiesDialogFragment)
        binding.rvCountries?.adapter = rvSelectCountryAdapter
        rvSelectCountryAdapter.submitList(data)
        selectDefaultCountry()
    }

    private fun initCitiesRv(data:List<CityModel>) {
        rvSelectCityAdapter.setListener(this@FilterCitiesDialogFragment)
        binding.rvCities?.adapter = rvSelectCityAdapter
        rvSelectCityAdapter.submitList(data)
        selectDefaultCity()
    }

    private fun selectDefaultCountry() {
        viewLifecycleOwner.lifecycleScope.launch {
            delay(100)
            binding.rvCountries?.findViewHolderForAdapterPosition(0)?.itemView?.performClick()
        }
    }
    private fun selectDefaultCity() {
        viewLifecycleOwner.lifecycleScope.launch {
            delay(100)
            binding.rvCities?.findViewHolderForAdapterPosition(0)?.itemView?.performClick()
        }
    }
    fun setupViews(){
        dismissShimmerEffect()
    }
    private fun showShimmerEffect() {
        binding?.apply {
            viewLayout.visibility = View.GONE
            shimmerLayout.visibility = View.VISIBLE
            shimmerLayout.startShimmer()
        }
    }

    private fun dismissShimmerEffect() {
        binding.apply {
            viewLayout.visibility = View.VISIBLE
            shimmerLayout.visibility = View.GONE
            shimmerLayout.stopShimmer()
        }
    }

    override fun onCityClick(model: CityModel) {
        if (LocalUtil.isEnglish()){
            binding.btnTvCityName?.text = model.titleEn

        }else{
            binding.btnTvCityName?.text = model.titleAr

        }
        binding.btnIvCountryFlag.let {
            Glide.with(requireContext()).load(model.image).into(it)
        }
        cityId = model.id
        cityNameEn = model.titleEn
        cityNameAr = model.titleAr
        binding.btnContinue.isEnabled = true
    }

    override fun onCountryClick(model: CountryModel) {
        viewModel.getCities(model.id.toString())
        countryId = model.id

    }
}