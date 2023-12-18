package eramo.amtalek.presentation.ui.auth.forgetpassword

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentChangePasswordForgetPasswordBinding
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
import eramo.amtalek.presentation.viewmodel.auth.ChangePasswordForgetPasswordViewModel
import eramo.amtalek.util.API_SUCCESS_CODE
import eramo.amtalek.util.showToast
import eramo.amtalek.util.state.UiState

@AndroidEntryPoint
class ChangePasswordForgetPasswordFragment : BindingFragment<FragmentChangePasswordForgetPasswordBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentChangePasswordForgetPasswordBinding::inflate

    private val viewModel by viewModels<ChangePasswordForgetPasswordViewModel>()

    private val args by navArgs<ChangePasswordForgetPasswordFragmentArgs>()
    private val registeredMail get() = args.registeredMail
    private val enteredCode get() = args.enteredCode

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        listeners()

        fetchData()
    }

    private fun listeners() {
        setupBtnConfirmAvailability()

        binding.apply {
            FChangePasswordBtnConfirm.setOnClickListener {
                validateAndChangePassword()
            }
        }
    }

    private fun setupViews() {
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

    }

    private fun fetchData(){
        fetchChangingPasswordState()
    }

    private fun fetchChangingPasswordState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.changePasswordState.collect { state ->
                    when (state) {

                        is UiState.Success -> {
                            LoadingDialog.dismissDialog()

                            if (state.data?.status == API_SUCCESS_CODE) {
                                showToast(state.data.message)

                                findNavController().popBackStack(R.id.loginFragment, false)

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

    private fun validateAndChangePassword() {
        binding.apply {
            val newPassword = FChangePasswordEtPassword.text.toString().trim()
            val rePassword = FChangePasswordEtRePassword.text.toString().trim()

            if (TextUtils.isEmpty(newPassword)) {
                FChangePasswordTilPassword.error = getString(R.string.enter_a_password)
                FChangePasswordTilPassword.requestFocus()
                return
            } else if (newPassword.length < 8) {
                FChangePasswordTilPassword.error = getString(R.string.password_must_contains_eight_chars)
                FChangePasswordTilPassword.requestFocus()
                return
            } else {
                FChangePasswordTilPassword.error = null
            }

            if (TextUtils.isEmpty(rePassword)) {
                FChangePasswordTilConfirmPassword.error = getString(R.string.retype_the_password)
                FChangePasswordTilConfirmPassword.requestFocus()
                return
            } else if (newPassword != rePassword) {
                FChangePasswordTilConfirmPassword.error = getString(R.string.password_does_not_match)
                FChangePasswordTilConfirmPassword.requestFocus()
                return
            } else {
                FChangePasswordTilConfirmPassword.error = null
            }

            viewModel.changePassword(
                registeredMail, enteredCode, newPassword, rePassword
            )

        }
    }

    private fun setupBtnConfirmAvailability(){
        binding.apply {

            // default
            FChangePasswordBtnConfirm.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.button_background_long_faded)
            FChangePasswordBtnConfirm.isEnabled = false

            // listener editText password
           FChangePasswordEtPassword .addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (FChangePasswordEtPassword.text.toString().trim().isNotEmpty() &&
                    FChangePasswordEtRePassword.text.toString().trim().isNotEmpty()
                ) {
                    FChangePasswordBtnConfirm.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.button_background_long)
                    FChangePasswordBtnConfirm.isEnabled = true

                } else {
                    FChangePasswordBtnConfirm.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.button_background_long_faded)
                    FChangePasswordBtnConfirm.isEnabled = false
                }
            }
        })

            // listener editText rePassword
           FChangePasswordEtRePassword .addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (FChangePasswordEtPassword.text.toString().trim().isNotEmpty() &&
                    FChangePasswordEtRePassword.text.toString().trim().isNotEmpty()
                ) {
                    FChangePasswordBtnConfirm.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.button_background_long)
                    FChangePasswordBtnConfirm.isEnabled = true

                } else {
                    FChangePasswordBtnConfirm.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.button_background_long_faded)
                    FChangePasswordBtnConfirm.isEnabled = false
                }
            }
        })
        }
    }

}