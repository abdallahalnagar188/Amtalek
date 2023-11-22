package eramo.amtalek.presentation.ui.drawer.myaccount

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.theartofdev.edmodo.cropper.CropImage
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentEditPersonalDetailsBinding
import eramo.amtalek.domain.model.auth.CityModel
import eramo.amtalek.domain.model.auth.CountryModel
import eramo.amtalek.domain.model.auth.UserModel
import eramo.amtalek.presentation.adapters.spinner.CitiesSpinnerAdapter
import eramo.amtalek.presentation.adapters.spinner.CountriesSpinnerAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.presentation.viewmodel.drawer.myaccount.EditPersonalDetailsViewModel
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.showToast
import eramo.amtalek.util.state.UiState

@AndroidEntryPoint
class EditPersonalDetailsFragment : BindingFragment<FragmentEditPersonalDetailsBinding>() {

    override val isRefreshingEnabled: Boolean get() = false
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentEditPersonalDetailsBinding::inflate

    private val viewModel by viewModels<EditPersonalDetailsViewModel>()
    private val viewModelShared: SharedViewModel by activityViewModels()

    private lateinit var userModel: UserModel
    private var selectedCountryId = -1
    private var selectedCityId = -1

    private var imageUri: Uri? = null
    private val activityResultContract = object : ActivityResultContract<Any, Uri?>() {
        override fun createIntent(context: Context, input: Any?): Intent {
            return CropImage.activity()
                .setAspectRatio(1, 1)
                .getIntent(requireActivity())
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            if (resultCode == AppCompatActivity.RESULT_OK)
                return CropImage.getActivityResult(intent).uri
            return Uri.parse("")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val activityResultLauncher = registerForActivityResult(activityResultContract) {
            it?.let { uri ->
                imageUri = uri
                if (it.toString().isNotEmpty())
                    binding.ivProfile.setImageURI(it)
            }
        }

        setupViews()
        listeners()

        requestData()
        fetchData()
    }

    private fun listeners() {
        binding.apply {
            inToolbar.ivBack.setOnClickListener { findNavController().popBackStack() }
            btnConfirm.setOnClickListener { validateAndUpdateProfile() }

//            cvCamera.setOnClickListener { activityResultLauncher.launch(null) }
        }
    }

    private fun setupViews() {
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)
        binding.inToolbar.tvTitle.text = getString(R.string.edit_profile)

    }

    private fun requestData() {
        viewModel.getProfile()
    }

    private fun fetchData() {
        fetchCities()
        fetchCountries()
        fetchGetProfileState()
        fetchUpdateProfileState()
    }

    // -------------------------------------- setupViews -------------------------------------- //
    private fun setupCountriesSpinner(data: List<CountryModel>) {
        binding.apply {
            val countriesSpinnerAdapter = CountriesSpinnerAdapter(requireContext(), data)
            countriesSpinner.adapter = countriesSpinnerAdapter

            for (i in data) {
                if (i.id == userModel.countryId) {
                    countriesSpinner.setSelection(data.indexOf(i))
                }
            }

            countriesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val model = parent?.getItemAtPosition(position) as CountryModel
                    selectedCountryId = model.id

                    viewModel.getCities(model.id.toString())
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

            // refresh onClick if getting list fail
            countriesSpinner.setOnTouchListener { view, motionEvent ->
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
            citiesSpinner.adapter = citiesSpinnerAdapter

            for (i in data) {
                if (i.id == userModel.cityId) {
                    citiesSpinner.setSelection(data.indexOf(i))
                }
            }

            citiesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val model = parent?.getItemAtPosition(position) as CityModel
                    selectedCityId = model.id
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

            // refresh onClick if getting list fail
            citiesSpinner.setOnTouchListener { view, motionEvent ->
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

    private fun validateAndUpdateProfile() {
        binding.apply {
            val firstName = etFirstName.text.toString().trim()
            val lastname = etLastName.text.toString().trim()
            val mobileNumber = etMobileNumber.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val bio = etBio.text.toString().trim()

            if (TextUtils.isEmpty(firstName)) {
                tilFirstName.error = getString(R.string.first_name_is_required)
                tilFirstName.requestFocus()
                return
            } else {
                tilFirstName.error = null
            }

            if (TextUtils.isEmpty(lastname)) {
                tilLastName.error = getString(R.string.last_name_is_required)
                tilLastName.requestFocus()
                return
            } else {
                tilLastName.error = null
            }

            if (TextUtils.isEmpty(mobileNumber)) {
                tilMobileNumber.error = getString(R.string.mobile_number_is_required)
                tilMobileNumber.requestFocus()

                return
            } else if (mobileNumber.length != 11) {
                tilMobileNumber.error = getString(R.string.please_enter_a_valid_number)
                tilMobileNumber.requestFocus()

                return
            } else {
                tilMobileNumber.error = null
            }

            if (TextUtils.isEmpty(email)) {
                tilEmail.error = getString(R.string.email_is_required)
                tilEmail.requestFocus()
                return
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                tilEmail.error = getString(R.string.please_enter_a_valid_email)
                tilEmail.requestFocus()
                return
            } else {
                tilEmail.error = null
            }

            if (selectedCountryId == -1) {
                showToast(getString(R.string.select_country))
                return
            }
            if (selectedCityId == -1) {
                showToast(getString(R.string.select_city))
                return
            }

            viewModel.updateProfile(
                firstName,
                lastname,
                mobileNumber,
                email,
                selectedCountryId.toString(),
                selectedCityId.toString(),
                bio,
                null,
                null
            )

        }
    }

    private fun fetchGetProfileState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getProfileState.collect { state ->
                    when (state) {

                        is UiState.Success -> {
                            LoadingDialog.dismissDialog()
                            userModel = state.data!!
                            assignDataToTheViews(userModel)

                            viewModel.getCountries()
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

    private fun fetchUpdateProfileState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.updateProfileState.collect { state ->
                    when (state) {

                        is UiState.Success -> {
                            LoadingDialog.dismissDialog()
                            showToast(state.data?.message!!)
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
//                            setupCitiesSpinner(emptyList())
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

                            setupCitiesSpinner(state.data!!)
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

    private fun assignDataToTheViews(user: UserModel) {
        binding.apply {
            etFirstName.setText(user.firstName)
            etLastName.setText(user.lastName)
            etMobileNumber.setText(user.phone)
            etEmail.setText(user.email)
            etBio.setText(user.bio)

            Glide.with(requireContext()).load(user.coverImageUrl).into(ivCover)

            if (user.profileImageUrl != "") {
                Glide.with(requireContext()).load(user.profileImageUrl).into(ivProfile)
            } else {
                Glide.with(requireContext()).load(R.drawable.avatar).into(ivProfile)
            }
        }
    }
}