package eramo.amtalek.presentation.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentSignupBinding
import eramo.amtalek.presentation.adapters.spinner.CitiesSpinnerAdapter
import eramo.amtalek.presentation.adapters.spinner.CountriesSpinnerAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.viewmodel.auth.SignUpViewModel
import eramo.amtalek.util.Dummy
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.navOptionsAnimation

@AndroidEntryPoint
class SignUpFragment : BindingFragment<FragmentSignupBinding>() {

    override val isRefreshingEnabled: Boolean get() = false
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSignupBinding::inflate

    private val viewModel by viewModels<SignUpViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)

        binding.apply {
//            cvBack.setOnClickListener { findNavController().popBackStack() }
//            signupBtnNext.setOnClickListener {
//                findNavController().navigate(R.id.informationFragment,null, navOptionsAnimation())
//            }
            FSignUpTvLogin.setOnClickListener {
                findNavController().navigate(R.id.loginFragment, null, navOptionsAnimation())
            }
        }

        setupCountriesSpinner()
        setupCitiesSpinner()
        setupGenderSwitch()
        setupTermsAndConditionsCheckBox()
    }

    private fun setupCountriesSpinner() {
        val countriesSpinnerAdapter = CountriesSpinnerAdapter(requireContext(), Dummy.dummyCountriesList())
        binding.FSignUpCountriesSpinner.adapter = countriesSpinnerAdapter

        binding.FSignUpCountriesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                val model = parent?.getItemAtPosition(position) as CountriesSpinnerModel
//
//                Toast.makeText(requireContext(), model.countryName, Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private fun setupCitiesSpinner() {
        val citiesSpinnerAdapter = CitiesSpinnerAdapter(requireContext(), Dummy.dummyCitiesList())
        binding.FSignUpCitiesSpinner.adapter = citiesSpinnerAdapter

        binding.FSignUpCountriesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                val model = parent?.getItemAtPosition(position) as CountriesSpinnerModel
//
//                Toast.makeText(requireContext(), model.countryName, Toast.LENGTH_SHORT).show()
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
            FSignUpBtnRegisterNow.background = ContextCompat.getDrawable(requireContext(), R.drawable.button_background_long_faded)

            // listener
            FSignUpCbAgree.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    FSignUpBtnRegisterNow.background = ContextCompat.getDrawable(requireContext(), R.drawable.button_background_long)
                } else {
                    FSignUpBtnRegisterNow.background = ContextCompat.getDrawable(requireContext(), R.drawable.button_background_long_faded)
                }

            }
        }
    }
}