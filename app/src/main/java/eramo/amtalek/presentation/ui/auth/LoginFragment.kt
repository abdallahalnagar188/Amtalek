package eramo.amtalek.presentation.ui.auth

import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentLoginBinding
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
import eramo.amtalek.presentation.viewmodel.auth.LoginViewModel
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.showToast
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiState

@AndroidEntryPoint
class LoginFragment : BindingFragment<FragmentLoginBinding>(), View.OnClickListener {

    override val isRefreshingEnabled: Boolean get() = false
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentLoginBinding::inflate

    private val viewModel by viewModels<LoginViewModel>()
    private val args by navArgs<LoginFragmentArgs>()
    private val proceedRequire get() = args.proceedRequire

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        super.registerApiCancellation { viewModel.cancelRequest() }
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)

        binding.apply {
            FLoginTvSignUp.setOnClickListener(this@LoginFragment)
            FLoginBtnLogin.setOnClickListener(this@LoginFragment)
            FLoginTvForgotPassword.setOnClickListener(this@LoginFragment)
        }
        fetchLoginState()
    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.FLogin_tv_signUp -> findNavController().navigate(R.id.signUpFragment)

            R.id.FLogin_tv_forgot_password -> findNavController().navigate(R.id.forgetPasswordFragment)

            R.id.FLogin_btn_login -> {
//                setupLogin()
                findNavController().navigate(
                    R.id.nav_main, null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.nav_auth, true)
                        .setPopUpTo(R.id.nav_main, true)
                        .build()
                )
            }
        }
    }

    private fun setupLogin() {
//        binding.apply {
//            val phone = loginEtPhone.text.toString().trim()
//            val password = loginEtPassword.text.toString().trim()
//
//            if (TextUtils.isEmpty(phone)) {
//                itlPhone.error = getString(R.string.txt_phone_is_required)
//                return
//            } else if (!PhoneNumberUtils.isGlobalPhoneNumber(phone)) {
//                itlPhone.error = getString(R.string.txt_please_enter_a_valid_phone_number)
//                return
//            } else itlPhone.error = null
//
//            if (TextUtils.isEmpty(password)) {
//                itlPassword.error = getString(R.string.txt_password_is_required)
//                return
//            } else itlPassword.error = null
//
//            viewModel.loginApp(phone, password, true)
//        }
    }

    private fun fetchLoginState() {
        lifecycleScope.launchWhenCreated {
            viewModel.loginState.collect { state ->
                when (state) {
                    is UiState.Success -> {
                        LoadingDialog.dismissDialog()
                        if (proceedRequire) switchLocalCartToRemote()
                        else findNavController().navigate(
                            R.id.nav_main, null,
                            NavOptions.Builder()
                                .setPopUpTo(R.id.nav_auth, true)
                                .setPopUpTo(R.id.nav_main, true)
                                .build()
                        )
                    }
                    is UiState.Error -> {
                        LoadingDialog.dismissDialog()
                        showToast(state.message!!.asString(requireContext()))
                    }
                    is UiState.Loading -> {
                        LoadingDialog.showDialog()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun switchLocalCartToRemote() {
        lifecycleScope.launchWhenStarted {
            viewModel.switchLocalCartToRemote().collect {
                when (it) {
                    is Resource.Success -> {
                        LoadingDialog.dismissDialog()
                        findNavController().popBackStack()
                    }
                    is Resource.Error -> {
                        LoadingDialog.dismissDialog()
                        showToast(it.message!!.asString(requireContext()))
                    }
                    is Resource.Loading -> {
                        LoadingDialog.showDialog()
                    }
                }
            }
        }
    }
}