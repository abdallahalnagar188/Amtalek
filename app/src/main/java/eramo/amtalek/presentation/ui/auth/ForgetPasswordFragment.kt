package eramo.amtalek.presentation.ui.auth

import android.os.Bundle
import android.text.Editable
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
import eramo.amtalek.util.showToast
import eramo.amtalek.util.state.UiState

@AndroidEntryPoint
class ForgetPasswordFragment : BindingFragment<FragmentForgetPasswordBinding>() {

    override val isRefreshingEnabled: Boolean get() = false
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
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)
    }

    private fun listeners() {
        setupBtnSendCodeAvailability()

        binding.apply {
            FForgetBtnSendCode.setOnClickListener {
                viewModel.sendForgotPasswordMail(FForgetEtEmail.text.toString().trim())
            }

            FForgetTvSignUp.setOnClickListener {
                findNavController().navigate(R.id.signUpFragment)
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
                    if (FForgetEtEmail.text.toString().trim().isNotEmpty() &&
                        Patterns.EMAIL_ADDRESS.matcher(FForgetEtEmail.text.toString().trim()).matches()
                    ) {
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

}