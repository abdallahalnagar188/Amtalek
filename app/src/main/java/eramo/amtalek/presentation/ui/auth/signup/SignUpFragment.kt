package eramo.amtalek.presentation.ui.auth.signup

import android.animation.ValueAnimator
import android.app.Activity
import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R

import eramo.amtalek.databinding.FragmentSignupBinding
import eramo.amtalek.domain.model.auth.CityModel
import eramo.amtalek.domain.model.auth.CountryModel
import eramo.amtalek.domain.model.auth.RegionModel
import eramo.amtalek.presentation.adapters.spinner.CitiesSpinnerAdapter
import eramo.amtalek.presentation.adapters.spinner.CountriesSpinnerAdapter
import eramo.amtalek.presentation.adapters.spinner.RegionsSpinnerAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
import eramo.amtalek.presentation.viewmodel.auth.SignUpViewModel
import eramo.amtalek.util.API_SUCCESS_CODE
import eramo.amtalek.util.LocalUtil
import eramo.amtalek.util.SIGN_UP_GENDER_FEMALE
import eramo.amtalek.util.SIGN_UP_GENDER_MALE
import eramo.amtalek.util.SIGN_UP_TYPE_COMPANY
import eramo.amtalek.util.SIGN_UP_TYPE_INDIVIDUAL
import eramo.amtalek.util.StatusBarUtil

import eramo.amtalek.util.navOptionsAnimation
import eramo.amtalek.util.navOptionsFromBottomAnimation
import eramo.amtalek.util.setTextViewDrawableColor
import eramo.amtalek.util.showToast
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar


@AndroidEntryPoint
class SignUpFragment : BindingFragment<FragmentSignupBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSignupBinding::inflate

    private val viewModel by viewModels<SignUpViewModel>()

    private lateinit var rippleAnimatorTermsCheckbox: ValueAnimator

    private var selectedCountryId = -1
    private var selectedCityId = -1
    private var selectedRegionId = -1
    private lateinit var selectedGender: String
    private lateinit var selectType: String
    private var imageUri: Uri? = null
    val calender = Calendar.getInstance()





    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        LocalUtil.loadLocal(requireActivity())
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        return super.onCreateView(inflater, container, savedInstanceState)
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        selectedCountryId = -1
        selectedCityId = -1
        selectedRegionId = -1
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        listeners()
        requestData()
        fetchData()

    }


    override fun onResume() {
        super.onResume()
        selectedCountryId = -1
        selectedCityId = -1
        selectedRegionId = -1
    }
//
//    override fun onPause() {
//        super.onPause()
//        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)
//        selectedCountryId = -1
//        selectedCityId = -1
//        selectedRegionId = -1
//        fetchData()
//    }


    private fun setupViews() {
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        setupAnimations()
        binding.FSignUpTvTerms.setOnClickListener {
            findNavController().navigate(R.id.termsAndConditionsFragment, null, navOptionsAnimation())
        }
        if (LocalUtil.isEnglish()) {
            binding.FSignUpIvLogo.setImageDrawable(context?.getDrawable(R.drawable.top_logo_en))

        } else {
            binding.FSignUpIvLogo.setImageDrawable(context?.getDrawable(R.drawable.top_logo_ar))
        }
    }

    private fun listeners() {
        setupGenderSwitch()
        setupTypeSwitch()
        // remove error on text changed
        passwordEditTextsListener()
        setupTermsAndConditionsCheckBox()

        binding.apply {

            FSignUpBtnRegisterNow.setOnClickListener {
                validateAndSignUp()
            }
            FSignUpTvLogin.setOnClickListener {
                findNavController().navigate(R.id.loginFragment, null, navOptionsFromBottomAnimation())
            }
            identityCard.setOnClickListener() {
                ImagePicker.with(requireActivity())
                    .compress(1024)
                    .cropSquare()
                    .createIntent { intent ->
                        startForProfileImageResult.launch(intent)
                    }
            }
            FSignUpTilBirthdate.setOnClickListener() {
                setupBirthDate()
            }
            birthDateView.setOnClickListener() {
                setupBirthDate()
            }
        }

    }

    private fun setupBirthDate() {
        val dialog = DatePickerDialog(requireContext())
        dialog.setOnDateSetListener { datePicker, year, month, day ->

            binding.FSignUpEtBirthdate.text = Editable.Factory.getInstance().newEditable("$year-${month + 1}-$day")
            calender.set(year, month, day)
            calender.set(Calendar.HOUR_OF_DAY, 0)
            calender.set(Calendar.MINUTE, 0)
            calender.set(Calendar.SECOND, 0)
            calender.set(Calendar.MILLISECOND, 0)
        }
        dialog.show()
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!
                imageUri = fileUri
                binding.ivImagePicked.setImageURI(fileUri)
                binding.ivImagePicked.isVisible = true
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "No Image chosen", Toast.LENGTH_SHORT).show()
            }
        }

    private fun requestData() {
        viewLifecycleOwner.lifecycleScope.launch {

            // for enter animation
            delay(400)
            viewModel.getCountries()
        }
    }

    private fun fetchData() {
        fetchCountries()
        fetchCities()
        fetchRegions()
        fetchRegisterState()
        fetchSendingVerificationCodeEmailState()
    }

    private fun setupAnimations() {
        applyLogoAnimation()
        applyCheckboxAnimation()
    }


    private fun applyCheckboxAnimation() {
        val view = binding.FSignUpCbAgreeAnimator
        view.setBackgroundResource(R.drawable.checkbox_ripple_effect_background)

        rippleAnimatorTermsCheckbox = ValueAnimator.ofFloat(0f, 1f)
        rippleAnimatorTermsCheckbox.addUpdateListener { animator ->
            val value = animator.animatedValue as Float
            view.scaleX = 1 + value
            view.scaleY = 1 + value
            view.alpha = 1 - value
        }
        rippleAnimatorTermsCheckbox.duration = 1000
        rippleAnimatorTermsCheckbox.repeatMode = ValueAnimator.RESTART
        rippleAnimatorTermsCheckbox.repeatCount = ValueAnimator.INFINITE
        rippleAnimatorTermsCheckbox.start()
    }

    private fun applyLogoAnimation() {
        val view = binding.FSignUpIvLogoWhiteView
        val animator = ValueAnimator.ofFloat(1.0f, 0.0f)
        animator.duration = 1500
        animator.addUpdateListener { animation ->
            view?.alpha = animation.animatedValue as Float
        }

        animator.start()
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
                    viewModel.getRegions(model.id.toString())

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

    private fun setupRegionsSpinner(data: List<RegionModel>) {
        binding.apply {
            val regionsSpinnerAdapter = RegionsSpinnerAdapter(requireContext(), data)
            FSignUpRegionsSpinner.adapter = regionsSpinnerAdapter
            FSignUpRegionsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val model = parent?.getItemAtPosition(position) as RegionModel
                    selectedRegionId = model.id
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle case when nothing is selected if needed
                }
            }

            // Refresh onClick if getting list fails
            FSignUpRegionsSpinner.setOnTouchListener { view, motionEvent ->
                when (motionEvent?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        view.performClick()
                        if (data.isEmpty()) {
                            viewModel.getCountries()  // Fetch data if the list is empty
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

    private fun setupTypeSwitch() {
        binding.typeSelectionLayout.apply {
            // Default
            selectType = SIGN_UP_TYPE_INDIVIDUAL

            // individual color
            btnIndividual.setTextColor(ContextCompat.getColor(requireContext(), R.color.amtalek_blue))
            btnIndividual.setTextViewDrawableColor(R.color.amtalek_blue)

            // company color
            btnCompany.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_gray))
            btnCompany.setTextViewDrawableColor(R.color.text_gray)

            // individual onClickListener
            btnIndividual.setOnClickListener {
                selectType = SIGN_UP_TYPE_INDIVIDUAL
                // individual color
                btnIndividual.setTextColor(ContextCompat.getColor(requireContext(), R.color.amtalek_blue))
                btnIndividual.setTextViewDrawableColor(R.color.amtalek_blue)
                binding.FSignUpTilCompanyName.isVisible = false
                binding.identityCard.isVisible = false
                binding.FSignUpEtCompanyName.isVisible = false

                // company color
                btnCompany.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_gray))
                btnCompany.setTextViewDrawableColor(R.color.text_gray)

            }

            // company onClickListener
            btnCompany.setOnClickListener {
                selectType = SIGN_UP_TYPE_COMPANY
                binding.FSignUpTilCompanyName.isVisible = true
                binding.identityCard.isVisible = true
                binding.FSignUpEtCompanyName.isVisible = true
                // individual color
                btnIndividual.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_gray))
                btnIndividual.setTextViewDrawableColor(R.color.text_gray)

                // company color
                btnCompany.setTextColor(ContextCompat.getColor(requireContext(), R.color.amtalek_blue))
                btnCompany.setTextViewDrawableColor(R.color.amtalek_blue)

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
                    rippleAnimatorTermsCheckbox.end()
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
                            showShimmerEffect()
                        }

                        is UiState.Success -> {
                            dismissShimmerEffect()
                            setupCountriesSpinner(state.data ?: emptyList())
                        }

                        is UiState.Error -> {
                            dismissShimmerEffect()
                            val errorMessage = state.message!!.asString(requireContext())
                            showToast(errorMessage)

                            /**
                            initialize spinners onConnection fail
                            with empty lists to enable refresh onClick
                             **/
                            setupCountriesSpinner(emptyList())
                            setupCitiesSpinner(emptyList())
                            setupRegionsSpinner(emptyList())
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
//                            showShimmerEffect()
                        }

                        is UiState.Success -> {
                            dismissShimmerEffect()

                            setupCitiesSpinner(state.data ?: emptyList())
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

    private fun fetchRegions() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.regionsState.collect { state ->
                    when (state) {
                        is UiState.Loading -> {
//                            showShimmerEffect()
                        }

                        is UiState.Success -> {
                            dismissShimmerEffect()
                            setupRegionsSpinner(state.data ?: emptyList())
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

    private fun fetchRegisterState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.registerState.collect { state ->
                    when (state) {

                        is UiState.Success -> {
                            LoadingDialog.dismissDialog()

                            if (state.data?.status == API_SUCCESS_CODE) {
                                if (selectType == SIGN_UP_TYPE_INDIVIDUAL) {
                                    viewModel.sendVerificationCodeEmail()
                                } else {
                                    findNavController().navigate(R.id.loginFragment, null, navOptionsAnimation())
                                }
                            } else {
                                showToast(getString(R.string.something_went_wrong))
                            }
                        }

                        is UiState.Error -> {
                            Log.e("Mego", state.message!!.toString())
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
                firstName = firstName,
                lastName = lastname,
                phone = mobileNumber,
                email = email,
                password = password,
                confirmPassword = rePassword,
                gender = selectedGender,
                countryId = selectedCountryId.toString(),
                cityId = selectedCityId.toString(),
                regionId = selectedRegionId.toString(),
                companyName = binding.FSignUpEtCompanyName.text.toString(),
                companyLogo = imageUri,
                iam = selectType,
                birthday = binding.FSignUpEtBirthdate.text.toString()
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

    private fun showShimmerEffect() {
        binding.apply {
            shimmerLayout.startShimmer()

            viewLayout.visibility = View.GONE
            shimmerLayout.visibility = View.VISIBLE
        }
    }

    private fun dismissShimmerEffect() {
        binding.apply {
            shimmerLayout.stopShimmer()

            viewLayout.visibility = View.VISIBLE
            shimmerLayout.visibility = View.GONE
        }
    }

}