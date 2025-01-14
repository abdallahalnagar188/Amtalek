package eramo.amtalek.presentation.ui.auth

import android.animation.ValueAnimator
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentLoginBinding
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.auth.signup.OtpSignUpFragmentArgs
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.presentation.viewmodel.auth.LoginViewModel
import eramo.amtalek.util.API_SUCCESS_CODE
import eramo.amtalek.util.LocalUtil
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.navOptionsAnimation
import eramo.amtalek.util.navOptionsFromBottomAnimation
import eramo.amtalek.util.onBackPressed
import eramo.amtalek.util.showToast
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class LoginFragment : BindingFragment<FragmentLoginBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentLoginBinding::inflate

    private val viewModelShared: SharedViewModel by activityViewModels()
    private val viewModel by viewModels<LoginViewModel>()
    private val args by navArgs<LoginFragmentArgs>()
    private val proceedRequire get() = args.proceedRequire

    private var backPressedTime: Long = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        super.registerApiCancellation { viewModel.cancelRequest() }

        setupViews()
        listeners()

        fetchData()
        loginUser {
            if (it) {
                onLoginSuccess()
            } else {
                // Handle login failure
            }
        }
    }



    private fun loginUser(callback: (Boolean) -> Unit) {
        // Your login logic here
        // Call callback(true) on success, callback(false) on failure
    }

    fun onLoginSuccess() {
        viewModelShared.previousScreen?.let { destinationId ->
            findNavController().popBackStack(destinationId, false)
            // Optionally clear the previousScreen after navigating
            viewModelShared.previousScreen = null
        }
    }

    private fun setupViews() {
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        setupAnimations()
        if (LocalUtil.isEnglish()) {
            binding.FLoginIvLogo.setImageDrawable(context?.getDrawable(R.drawable.top_logo_en))

        } else {
            binding.FLoginIvLogo.setImageDrawable(context?.getDrawable(R.drawable.top_logo_ar))
        }
    }

    private fun listeners() {
        setupBtnLoginAvailability()

        binding.apply {
            FLoginTvSignUp.setOnClickListener {
                findNavController().navigate(R.id.signUpFragment, null, navOptionsFromBottomAnimation())
            }

            FLoginTvForgotPassword.setOnClickListener {
                findNavController().navigate(R.id.forgetPasswordFragment)
            }

            FLoginBtnLogin.setOnClickListener {
                validateAndLogin()
            }

            FLoginIvBack.setOnClickListener {
                findNavController().navigate(
                    R.id.nav_main, null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.nav_auth, true)
                        .setPopUpTo(R.id.nav_main, true)
                        .build()
                )
            }
        }

        this@LoginFragment.onBackPressed { pressBackAgainToExist() }
    }

    private fun fetchData() {
        fetchLoginState()
        fetchSendingVerificationCodeEmailState()
    }

    private fun setupAnimations() {
        applyLogoAnimation()
    }

    private fun fetchLoginState() {
        lifecycleScope.launch {
            withContext(coroutineContext) {
                viewModel.loginState.collect { state ->
                    when (state) {
                        is UiState.Success -> {
                            viewModelShared.LoginData.value = UiState.Success(state.data!!)

                            findNavController().navigate(
                                R.id.nav_main, null,
                                NavOptions.Builder()
                                    .setPopUpTo(R.id.nav_auth, true)
                                    .setPopUpTo(R.id.nav_main, true)
                                    .build()
                            )
                            LoadingDialog.dismissDialog()

                        }

                        is UiState.Error -> {
                            LoadingDialog.dismissDialog()
                            val errorMessage = state.message!!.asString(requireContext())

                            when (errorMessage) {
                                getString(R.string.please_verify_account) -> {
                                    showToast(getString(R.string.please_verify_account_an_email_sent))
                                    viewModel.sendVerificationCodeEmail(binding.FLoginEtMail.text.toString().trim())
                                    findNavController().navigate(
                                        R.id.otpSignUpFragment,
                                        OtpSignUpFragmentArgs(binding.FLoginEtMail.text.toString().trim()).toBundle(),
                                        navOptionsAnimation()
                                    )

                                }

                                getString(R.string.suspended_account_string) -> {
                                    findNavController().navigate(R.id.deletedAccountDialogFragment)
                                }

                                else -> {
                                    showToast(errorMessage)
                                }
                            }
                        }

                        is UiState.Loading -> {
                            LoadingDialog.showDialog()
                        }

                        else -> Unit
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

                                findNavController().navigate(
                                    R.id.otpSignUpFragment,
                                    OtpSignUpFragmentArgs(binding.FLoginEtMail.text.toString().trim()).toBundle(),
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

    private fun setupBtnLoginAvailability() {
        binding.apply {

            // default
            FLoginBtnLogin.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.button_background_long_faded)
            FLoginBtnLogin.isEnabled = false

            // listener editText email
            FLoginEtMail.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (FLoginEtMail.text.toString().trim().isNotEmpty() &&
                        FLoginEtPassword.text.toString().trim().isNotEmpty()
                    ) {
                        FLoginBtnLogin.background =
                            ContextCompat.getDrawable(requireContext(), R.drawable.button_background_long)
                        FLoginBtnLogin.isEnabled = true

                    } else {
                        FLoginBtnLogin.background =
                            ContextCompat.getDrawable(requireContext(), R.drawable.button_background_long_faded)
                        FLoginBtnLogin.isEnabled = false
                    }
                }
            })

            // listener editText Password
            FLoginEtPassword.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (FLoginEtMail.text.toString().trim().isNotEmpty() &&
                        FLoginEtPassword.text.toString().trim().isNotEmpty()
                    ) {
                        FLoginBtnLogin.background =
                            ContextCompat.getDrawable(requireContext(), R.drawable.button_background_long)
                        FLoginBtnLogin.isEnabled = true

                    } else {
                        FLoginBtnLogin.background =
                            ContextCompat.getDrawable(requireContext(), R.drawable.button_background_long_faded)
                        FLoginBtnLogin.isEnabled = false
                    }
                }
            })
        }
    }

    private fun validateAndLogin() {
        binding.apply {
            val email = FLoginEtMail.text.toString().trim()

            if (TextUtils.isEmpty(email)) {
                FLoginTilMail.error = getString(R.string.please_enter_your_email)
                FLoginTilMail.requestFocus()
                return
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                FLoginTilMail.error = getString(R.string.please_enter_a_valid_email)
                FLoginTilMail.requestFocus()
                return
            } else {
                FLoginTilMail.error = null
            }

            viewModel.login(
                email,
                FLoginEtPassword.text.toString().trim(),
                "test",
                true
            )
        }
    }


    private fun applyLogoAnimation() {
        val view = binding.FLoginIvLogoWhiteView
        val animator = ValueAnimator.ofFloat(1.0f, 0.0f)
        animator.duration = 1000
        animator.addUpdateListener { animation ->
            view?.alpha = animation.animatedValue as Float
        }

        animator.start()
    }

    private fun pressBackAgainToExist() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            requireActivity().finish()
        } else {
            showToast(getString(R.string.press_back_again_to_exist))
        }
        backPressedTime = System.currentTimeMillis()
    }
}