package eramo.amtalek.presentation.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentSignupBinding
import eramo.amtalek.domain.model.auth.CityModel
import eramo.amtalek.domain.model.auth.CountryModel
import eramo.amtalek.presentation.adapters.spinner.CitiesSpinnerAdapter
import eramo.amtalek.presentation.adapters.spinner.CountriesSpinnerAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
import eramo.amtalek.presentation.viewmodel.auth.SignUpViewModel
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.navOptionsAnimation
import eramo.amtalek.util.state.UiState

@AndroidEntryPoint
class SignUpFragment : BindingFragment<FragmentSignupBinding>() {

    override val isRefreshingEnabled: Boolean get() = false
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSignupBinding::inflate

    private val viewModel by viewModels<SignUpViewModel>()

    private var selectedCountryId = -1
    private var selectedCityId = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        listeners()

        requestData()
        fetchData()
    }

    private fun setupViews() {
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)

        setupGenderSwitch()

        setupTermsAndConditionsCheckBox()
    }

    private fun requestData() {
        viewModel.getCountries()
    }

    private fun fetchData() {
        fetchCountries()
        fetchCities()
    }

    private fun listeners() {
        binding.apply {
            FSignUpTvTerms.setOnClickListener {
                findNavController().navigate(R.id.termsAndConditionsFragment, null, navOptionsAnimation())
            }
            FSignUpBtnRegisterNow.setOnClickListener {
                findNavController().navigate(R.id.otpSignUpFragment, null, navOptionsAnimation())
            }
            FSignUpTvLogin.setOnClickListener {
                findNavController().navigate(R.id.loginFragment, null, navOptionsAnimation())
            }
        }
    }

    // -------------------------------------- setupViews -------------------------------------- //
    private fun setupCountriesSpinner(data: List<CountryModel>) {
        val countriesSpinnerAdapter = CountriesSpinnerAdapter(requireContext(), data)
        binding.FSignUpCountriesSpinner.adapter = countriesSpinnerAdapter

        binding.FSignUpCountriesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val model = parent?.getItemAtPosition(position) as CountryModel
                selectedCountryId = model.id

                viewModel.getCities(model.id.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private fun setupCitiesSpinner(data:List<CityModel>) {
        val citiesSpinnerAdapter = CitiesSpinnerAdapter(requireContext(), data)
        binding.FSignUpCitiesSpinner.adapter = citiesSpinnerAdapter

        binding.FSignUpCountriesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val model = parent?.getItemAtPosition(position) as CityModel

                selectedCityId = model.id
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private fun setupGenderSwitch() {
        binding.apply {

            // Default
            FSignUpTvGenderAvatarMale.setTextColor(ContextCompat.getColor(requireContext(), R.color.amtalek_blue))
            FSignUpIvGenderAvatarMale.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.amtalek_blue),
                android.graphics.PorterDuff.Mode.SRC_IN
            )

            // male onClickListener
            FSignUpIvGenderAvatarMale.setOnClickListener {
                FSignUpTvGenderAvatarMale.setTextColor(ContextCompat.getColor(requireContext(), R.color.amtalek_blue))
                FSignUpIvGenderAvatarMale.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.amtalek_blue),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )

                FSignUpTvGenderAvatarFemale.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_gray))
                FSignUpIvGenderAvatarFemale.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.text_gray),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )

            }

            FSignUpTvGenderAvatarMale.setOnClickListener {
                FSignUpTvGenderAvatarMale.setTextColor(ContextCompat.getColor(requireContext(), R.color.amtalek_blue))
                FSignUpIvGenderAvatarMale.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.amtalek_blue),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )

                FSignUpTvGenderAvatarFemale.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_gray))
                FSignUpIvGenderAvatarFemale.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.text_gray),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
            }

            // female onClickListener
            FSignUpIvGenderAvatarFemale.setOnClickListener {
                FSignUpTvGenderAvatarFemale.setTextColor(ContextCompat.getColor(requireContext(), R.color.amtalek_blue))
                FSignUpIvGenderAvatarFemale.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.amtalek_blue),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )

                FSignUpTvGenderAvatarMale.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_gray))
                FSignUpIvGenderAvatarMale.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.text_gray),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
            }

            FSignUpTvGenderAvatarFemale.setOnClickListener {
                FSignUpTvGenderAvatarFemale.setTextColor(ContextCompat.getColor(requireContext(), R.color.amtalek_blue))
                FSignUpIvGenderAvatarFemale.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.amtalek_blue),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )

                FSignUpTvGenderAvatarMale.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_gray))
                FSignUpIvGenderAvatarMale.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.text_gray),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
        }
    }

    private fun setupTermsAndConditionsCheckBox() {
        binding.apply {

            // default
            FSignUpBtnRegisterNow.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.button_background_long_faded)

            // listener
            FSignUpCbAgree.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    FSignUpBtnRegisterNow.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.button_background_long)
                    FSignUpBtnRegisterNow.isEnabled = true
                } else {
                    FSignUpBtnRegisterNow.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.button_background_long_faded)
                    FSignUpBtnRegisterNow.isEnabled = false
                }

            }
        }
    }

    // -------------------------------------- fetchData -------------------------------------- //
    private fun fetchCountries() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.countriesState.collect { state ->
                    when (state) {
                        is UiState.Loading -> {
                            LoadingDialog.showDialog()
                        }

                        is UiState.Success -> {
                            LoadingDialog.dismissDialog()

                            setupCountriesSpinner(state.data ?: emptyList())
                        }

                        is UiState.Error -> {
                            LoadingDialog.dismissDialog()
                        }

                        else -> {}
                    }

                }
            }
        }
    }

    private fun fetchCities() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.citiesState.collect { state ->
                    when (state) {
                        is UiState.Loading -> {
                            LoadingDialog.showDialog()
                        }

                        is UiState.Success -> {
                            LoadingDialog.dismissDialog()

                            setupCitiesSpinner(state.data ?: emptyList())
                        }

                        is UiState.Error -> {
                            LoadingDialog.dismissDialog()
                        }

                        else -> {}
                    }

                }
            }
        }
    }

}