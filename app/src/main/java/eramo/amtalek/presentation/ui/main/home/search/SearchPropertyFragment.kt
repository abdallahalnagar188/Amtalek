package eramo.amtalek.presentation.ui.main.home.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentSearchPropertyBinding
import eramo.amtalek.domain.model.auth.CityModel
import eramo.amtalek.domain.model.auth.CountryModel
import eramo.amtalek.presentation.adapters.spinner.CitiesSpinnerAdapter
import eramo.amtalek.presentation.adapters.spinner.CountriesSpinnerAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.presentation.viewmodel.navbottom.extension.NotificationViewModel
import eramo.amtalek.util.navOptionsFromBottomAnimation

@AndroidEntryPoint
class SearchPropertyFragment : BindingFragment<FragmentSearchPropertyBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSearchPropertyBinding::inflate

    private val viewModel by viewModels<NotificationViewModel>()
    private val viewModelShared by activityViewModels<SharedViewModel>()

    private var selectedCountryId = -1
    private var selectedCityId = -1


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        listeners()
    }

    private fun setupViews() {
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        setupToolbar()
    }

    private fun setupToolbar() {
        binding.inToolbar.apply {
            tvAdvancedSearch.text = getString(R.string.advanced_search)
            ivClose.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun listeners() {
        binding.apply {
            btnSearch.setOnClickListener {
                findNavController().navigate(R.id.searchPropertyResultFragment, null, navOptionsFromBottomAnimation())
            }
        }
    }

    // -------------------------------------- setupViews -------------------------------------- //
    private fun setupCountriesSpinner(data: List<CountryModel>) {
        binding.apply {
            val countriesSpinnerAdapter = CountriesSpinnerAdapter(requireContext(), data)
            spinnerCountry.adapter = countriesSpinnerAdapter

            spinnerCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val model = parent?.getItemAtPosition(position) as CountryModel
                    selectedCountryId = model.id

//                    viewModel.getCities(model.id.toString())
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

            // refresh onClick if getting list fail
            spinnerCountry.setOnTouchListener { view, motionEvent ->
                when (motionEvent?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        view.performClick()
                        if (data.isEmpty()) {
//                            viewModel.getCountries()
                        }
                    }
                }
                return@setOnTouchListener true
            }
        }
    }

    private fun setupCitiesSpinner(data: List<CityModel>) {
        binding.apply {
            val citiesSpinnerAdapter = CitiesSpinnerAdapter(requireContext(), data)
            spinnerCity.adapter = citiesSpinnerAdapter

            spinnerCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val model = parent?.getItemAtPosition(position) as CityModel

                    selectedCityId = model.id
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

            // refresh onClick if getting list fail
            spinnerCity.setOnTouchListener { view, motionEvent ->
                when (motionEvent?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        view.performClick()
                        if (data.isEmpty()) {
//                            viewModel.getCountries()
                        }
                    }
                }
                return@setOnTouchListener true
            }
        }
    }

}