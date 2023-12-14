package eramo.amtalek.presentation.ui.auth.forgetpassword

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentForgetPasswordBinding
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
import eramo.amtalek.presentation.viewmodel.auth.ForgotPassViewModel
import eramo.amtalek.util.API_SUCCESS_CODE
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.navOptionsAnimation
import eramo.amtalek.util.navOptionsFromBottomAnimation
import eramo.amtalek.util.showToast
import eramo.amtalek.util.state.UiState

@AndroidEntryPoint
class ForgetPasswordFragment : BindingFragment<FragmentForgetPasswordBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentForgetPasswordBinding::inflate

    private val viewModel by viewModels<ForgotPassViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        listeners()

        fetchData()
    }

    private fun setupViews() {
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    private fun listeners() {
        setupBtnSendCodeAvailability()

        binding.apply {
            FForgetBtnSendCode.setOnClickListener {
                validateAndSendForgotPasswordMail()
            }

            FForgetTvSignUp.setOnClickListener {
                findNavController().navigate(R.id.signUpFragment,null, navOptionsFromBottomAnimation())
            }

        }
    }

    private fun fetchData() {
        fetchSendingForgetPasswordCodeEmailState()
    }

    private fun fetchSendingForgetPasswordCodeEmailState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.sendForgotPasswordMailState.collect { state ->
                    when (state) {

                        is UiState.Success -> {
                            LoadingDialog.dismissDialog()

                            if (state.data?.status == API_SUCCESS_CODE) {
                                viewModel.registeredEmail?.let { registeredEmail ->

                                    findNavController().navigate(
                                        R.id.otpForgetPasswordFragment,
                                        OtpForgetPasswordFragmentArgs(registeredEmail).toBundle(),
                                        navOptionsAnimation()
                                    )
                                }

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

    private fun setupBtnSendCodeAvailability() {
        binding.apply {

            // default
            FForgetBtnSendCode.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.button_background_long_faded)
            FForgetBtnSendCode.isEnabled = false

            // listener editText email
            FForgetEtEmail.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (FForgetEtEmail.text.toString().trim().isNotEmpty()) {
                        FForgetBtnSendCode.background =
                            ContextCompat.getDrawable(requireContext(), R.drawable.button_background_long)
                        FForgetBtnSendCode.isEnabled = true

                    } else {
                        FForgetBtnSendCode.background =
                            ContextCompat.getDrawable(requireContext(), R.drawable.button_background_long_faded)
                        FForgetBtnSendCode.isEnabled = false
                    }
                }
            })
        }
    }

    private fun validateAndSendForgotPasswordMail(){
        val email = binding.FForgetEtEmail.text.toString().trim()

        if (TextUtils.isEmpty(email)) {
            binding.FForgetTilEmail.error = getString(R.string.email_is_required)
            binding.FForgetTilEmail.requestFocus()
            return
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.FForgetTilEmail.error = getString(R.string.please_enter_a_valid_email)
            binding.FForgetTilEmail.requestFocus()
            return
        } else {
            binding.FForgetTilEmail.error = null
        }

        viewModel.sendForgotPasswordMail(email)
    }
}