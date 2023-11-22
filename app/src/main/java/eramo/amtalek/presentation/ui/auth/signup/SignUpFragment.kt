package eramo.amtalek.presentation.ui.auth.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.MotionEvent
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
import eramo.amtalek.util.API_SUCCESS_CODE
import eramo.amtalek.util.SIGN_UP_GENDER_FEMALE
import eramo.amtalek.util.SIGN_UP_GENDER_MALE
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.navOptionsAnimation
import eramo.amtalek.util.setTextViewDrawableColor
import eramo.amtalek.util.showToast
import eramo.amtalek.util.state.UiState


@AndroidEntryPoint
class SignUpFragment : BindingFragment<FragmentSignupBinding>() {

    override val isRefreshingEnabled: Boolean get() = false
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSignupBinding::inflate

    private val viewModel by viewModels<SignUpViewModel>()

    private var selectedCountryId = -1
    private var selectedCityId = -1
    private lateinit var selectedGender: String

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
    }

    private fun listeners() {
        setupGenderSwitch()

        // remove error on text changed
        passwordEditTextsListener()
        setupTermsAndConditionsCheckBox()

        binding.apply {
            FSignUpTvTerms.setOnClickListener {
                findNavController().navigate(R.id.termsAndConditionsFragment, null, navOptionsAnimation())
            }
            FSignUpBtnRegisterNow.setOnClickListener {
                validateAndSignUp()
            }
            FSignUpTvLogin.setOnClickListener {
                findNavController().navigate(R.id.loginFragment, null, navOptionsAnimation())
            }
        }
    }

    private fun requestData() {
        viewModel.getCountries()
    }

    private fun fetchData() {
        fetchCountries()
        fetchCities()

        fetchRegisterState()
        fetchSendingVerificationCodeEmailState()
    }


    // -------------------------------------- setupViews -------------------------------------- //
    private fun setupCountriesSpinner(data: List<CountryModel>) {
        binding.apply {
            val countriesSpinnerAdapter = CountriesSpinnerAdapter(requireContext(), data)
            FSignUpCountriesSpinner.adapter = countriesSpinnerAdapter

            FSignUpCountriesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val model = parent?.getItemAtPosition(position) as CountryModel
                    selectedCountryId = model.id

                    viewModel.getCities(model.id.toString())
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

            // refresh onClick if getting list fail
            FSignUpCountriesSpinner.setOnTouchListener { view, motionEvent ->
                when (motionEvent?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        view.performClick()
                        if (data.isEmpty()) {
                            viewModel.getCountries()
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
            FSignUpCitiesSpinner.adapter = citiesSpinnerAdapter

            FSignUpCitiesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val model = parent?.getItemAtPosition(position) as CityModel

                    selectedCityId = model.id
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

            // refresh onClick if getting list fail
            FSignUpCitiesSpinner.setOnTouchListener { view, motionEvent ->
                when (motionEvent?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        view.performClick()
                        if (data.isEmpty()) {
                            viewModel.getCountries()
                        }
                    }
                }
                return@setOnTouchListener true
            }
        }
    }

    private fun setupGenderSwitch() {
        binding.genderSelectionLayout.apply {

            // Default
            selectedGender = SIGN_UP_GENDER_MALE

            // male color
            btnMale.setTextColor(ContextCompat.getColor(requireContext(), R.color.amtalek_blue))
            btnMale.setTextViewDrawableColor(R.color.amtalek_blue)

            // female color
            btnFemale.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_gray))
            btnFemale.setTextViewDrawableColor(R.color.text_gray)

            // male onClickListener
            btnMale.setOnClickListener {
                selectedGender = SIGN_UP_GENDER_MALE

                // male color
                btnMale.setTextColor(ContextCompat.getColor(requireContext(), R.color.amtalek_blue))
                btnMale.setTextViewDrawableColor(R.color.amtalek_blue)

                // female color
                btnFemale.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_gray))
                btnFemale.setTextViewDrawableColor(R.color.text_gray)

            }

            // female onClickListener
            btnFemale.setOnClickListener {
                selectedGender = SIGN_UP_GENDER_FEMALE

                // male color
                btnMale.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_gray))
                btnMale.setTextViewDrawableColor(R.color.text_gray)

                // female color
                btnFemale.setTextColor(ContextCompat.getColor(requireContext(), R.color.amtalek_blue))
                btnFemale.setTextViewDrawableColor(R.color.amtalek_blue)

            }

        }
    }

    private fun setupTermsAndConditionsCheckBox() {
        binding.apply {

            // default
            FSignUpBtnRegisterNow.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.button_background_long_faded)
            FSignUpBtnRegisterNow.isEnabled = false

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
                            val errorMessage = state.message!!.asString(requireContext())
                            showToast(errorMessage)

                            /**
                            initialize spinners onConnection fail
                            with empty lists to enable refresh onClick
                             **/
                            setupCountriesSpinner(emptyList())
                            setupCitiesSpinner(emptyList())
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
                            val errorMessage = state.message!!.asString(requireContext())
                            showToast(errorMessage)
                        }

                        else -> {}
                    }

                }
            }
        }
    }

    private fun fetchRegisterState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.registerState.collect { state ->
                    when (state) {

                        is UiState.Success -> {
                            LoadingDialog.dismissDialog()

                            if (state.data?.status == API_SUCCESS_CODE) {
                                viewModel.sendVerificationCodeEmail()
                            } else {
                                showToast(getString(R.string.something_went_wrong))
                            }
                        }

                        is UiState.Error -> {
                            LoadingDialog.dismissDialog()
                            val errorMessage = state.message!!.asString(requireContext())
                            showToast(errorMessage)
                        }

                        is UiState.Loading -> {
                            LoadingDialog.showDialog()
                        }

                        else -> {}
                    }

                }
            }
        }
    }

    private fun fetchSendingVerificationCodeEmailState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.sendVerificationCodeEmailState.collect { state ->
                    when (state) {

                        is UiState.Success -> {
                            LoadingDialog.dismissDialog()

                            if (state.data?.status == API_SUCCESS_CODE) {
                                showToast(
                                    getString(R.string.success) + "\n" + state.data.message
                                )

                                findNavController().navigate(
                                    R.id.otpSignUpFragment,
                                    OtpSignUpFragmentArgs(viewModel.registeredEmail!!).toBundle(),
                                    navOptionsAnimation()
                                )

                            } else {
                                showToast(getString(R.string.something_went_wrong))
                            }
                        }

                        is UiState.Error -> {
                            LoadingDialog.dismissDialog()
                            val errorMessage = state.message!!.asString(requireContext())
                            showToast(errorMessage)
                        }

                        is UiState.Loading -> {
                            LoadingDialog.showDialog()
                        }

                        else -> {}
                    }

                }
            }
        }
    }

    private fun validateAndSignUp() {
        binding.apply {


            val firstName = FSignUpEtFirstName.text.toString().trim()
            val lastname = FSignUpEtLastName.text.toString().trim()
            val mobileNumber = FSignUpEtMobileNumber.text.toString().trim()
            val email = FSignUpEtEmail.text.toString().trim()

            val password = FSignUpEtPassword.text.toString().trim()
            val rePassword = FSignUpEtRePassword.text.toString().trim()

            if (TextUtils.isEmpty(firstName)) {
                FSignUpTilFirstName.error = getString(R.string.first_name_is_required)
                FSignUpTilFirstName.requestFocus()
                return
            } else {
                FSignUpTilFirstName.error = null
            }

            if (TextUtils.isEmpty(lastname)) {
                FSignUpTilLastName.error = getString(R.string.last_name_is_required)
                FSignUpTilLastName.requestFocus()
                return
            } else {
                FSignUpTilLastName.error = null
            }

            if (TextUtils.isEmpty(mobileNumber)) {
                FSignUpTilMobileNumber.error = getString(R.string.mobile_number_is_required)
                FSignUpTilMobileNumber.requestFocus()

                return
            } else if (mobileNumber.length != 11) {
                FSignUpTilMobileNumber.error = getString(R.string.please_enter_a_valid_number)
                FSignUpTilMobileNumber.requestFocus()

                return
            } else {
                FSignUpTilMobileNumber.error = null
            }

            if (TextUtils.isEmpty(email)) {
                FSignUpTilEmail.error = getString(R.string.email_is_required)
                FSignUpTilEmail.requestFocus()
                return
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                FSignUpTilEmail.error = getString(R.string.please_enter_a_valid_email)
                FSignUpTilEmail.requestFocus()
                return
            } else {
                FSignUpTilEmail.error = null
            }

            if (TextUtils.isEmpty(password)) {
                FSignUpTilPassword.error = getString(R.string.enter_a_password)
                FSignUpTilPassword.requestFocus()
                return
            } else if (password.length < 8) {
                FSignUpTilPassword.error = getString(R.string.password_must_contains_eight_chars)
                FSignUpTilPassword.requestFocus()
                return
            } else {
                FSignUpTilPassword.error = null
            }

            if (TextUtils.isEmpty(rePassword)) {
                FSignUpTilRePassword.error = getString(R.string.retype_the_password)
                FSignUpTilRePassword.requestFocus()
                return
            } else if (password != rePassword) {
                FSignUpTilRePassword.error = getString(R.string.password_does_not_match)
                FSignUpTilRePassword.requestFocus()
                return
            } else {
                FSignUpTilRePassword.error = null
            }

            viewModel.register(
                firstName,
                lastname,
                mobileNumber,
                email,
                password,
                rePassword,
                selectedGender,
                selectedCountryId.toString(),
                selectedCityId.toString()
            )
        }
    }

    private fun passwordEditTextsListener() {
        binding.apply {
            FSignUpEtPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    FSignUpTilPassword.error = null
                }

                override fun afterTextChanged(s: Editable?) {

                }

            })

            FSignUpEtRePassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    FSignUpTilRePassword.error = null
                }

                override fun afterTextChanged(s: Editable?) {

                }

            })

            // onTouch method
//            binding.FSignUpEtPassword.setOnTouchListener { view, event ->
//                when (event?.action) {
//                    MotionEvent.ACTION_DOWN -> {
//                        view.performClick()
//                        binding.FSignUpTilPassword.error = null
//                        return@setOnTouchListener false
//                    }
//                    else -> return@setOnTouchListener false
//                }
//            }
        }
    }

}