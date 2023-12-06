package eramo.amtalek.presentation.ui.drawer.myaccount

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
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentChangePasswordBinding
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.presentation.viewmodel.drawer.myaccount.ChangePasswordViewModel
import eramo.amtalek.util.API_SUCCESS_CODE
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.deeplink.DeeplinkUtil
import eramo.amtalek.util.showToast
import eramo.amtalek.util.state.UiState

@AndroidEntryPoint
class ChangePasswordFragment : BindingFragment<FragmentChangePasswordBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentChangePasswordBinding::inflate

    private val viewModel by viewModels<ChangePasswordViewModel>()
    private val viewModelShared: SharedViewModel by viewModels()
//    private val args by navArgs<ChangePasswordFragmentArgs>()
//    private val memberModel get() = args.memberModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        super.registerApiCancellation { viewModel.cancelRequest() }

        setupViews()
        listeners()

        fetchData()
    }

    private fun setupViews() {
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)

        setupToolbar()
        setupBtnChangeAvailability()
    }

    private fun listeners() {
        binding.apply {
            btnConfirm.setOnClickListener {
                validateAndChangePassword()
            }
        }
    }

    private fun fetchData() {
        fetchUpdatePasswordState()
        fetchLogoutState()
    }

    private fun setupToolbar() {
        binding.inToolbar.apply {
            tvTitle.text = getString(R.string.change_password)
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun validateAndChangePassword() {
        binding.apply {
            val currentPassword = etCurrentPassword.text.toString().trim()
            val newPassword = etNewPassword.text.toString().trim()
            val rePassword = etConfirmPassword.text.toString().trim()

            if (TextUtils.isEmpty(currentPassword)) {
                tilCurrentPassword.error = getString(R.string.enter_current_password)
                tilCurrentPassword.requestFocus()
                return
            } else {
                tilCurrentPassword.error = null
            }

            if (TextUtils.isEmpty(newPassword)) {
                tilNewPassword.error = getString(R.string.enter_a_password)
                tilNewPassword.requestFocus()
                return
            } else if (newPassword.length < 8) {
                tilNewPassword.error = getString(R.string.password_must_contains_eight_chars)
                tilNewPassword.requestFocus()
                return
            } else {
                tilNewPassword.error = null
            }

            if (TextUtils.isEmpty(rePassword)) {
                tilConfirmPassword.error = getString(R.string.retype_the_password)
                tilConfirmPassword.requestFocus()
                return
            } else if (newPassword != rePassword) {
                tilConfirmPassword.error = getString(R.string.password_does_not_match)
                tilConfirmPassword.requestFocus()
                return
            } else {
                tilConfirmPassword.error = null
            }

            if (currentPassword == newPassword && newPassword == rePassword) {
                showToast(getString(R.string.password_is_the_same_current))
                return
            }

            viewModel.updatePassword(currentPassword, newPassword, rePassword)
        }
    }

    private fun fetchUpdatePasswordState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.updatePasswordState.collect { state ->
                    when (state) {
                        is UiState.Success -> {
                            LoadingDialog.dismissDialog()

                            if (state.data?.status == API_SUCCESS_CODE) {
                                showToast(state.data?.message?: getString(R.string.success))
                                viewModelShared.logout()
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

                        else -> Unit
                    }
                }
            }
        }
    }

    private fun fetchLogoutState() {
        lifecycleScope.launchWhenStarted {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelShared.logoutState.collect { state ->
                    when (state) {
                        is UiState.Success -> {
                            LoadingDialog.dismissDialog()
                            findNavController().navigate(
                                NavDeepLinkRequest.Builder.fromUri(DeeplinkUtil.toLogin()).build(),
                                NavOptions.Builder().setPopUpTo(R.id.nav_main, true).build()
                            )
                        }

                        is UiState.Error -> {
                            LoadingDialog.dismissDialog()
                            val errorMessage = state.message!!.asString(requireContext())
                            showToast(errorMessage)

                            findNavController().navigate(
                                NavDeepLinkRequest.Builder.fromUri(DeeplinkUtil.toLogin()).build(),
                                NavOptions.Builder().setPopUpTo(R.id.nav_main, true).build()
                            )
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

    private fun setupBtnChangeAvailability() {
        binding.apply {

            // default
            btnConfirm.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.button_background_long_faded)
            btnConfirm.isEnabled = false

            // listener editText currentPassword
            etCurrentPassword.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (etCurrentPassword.text.toString().trim().isNotEmpty() &&
                        etNewPassword.text.toString().trim().isNotEmpty() &&
                        etConfirmPassword.text.toString().trim().isNotEmpty()
                    ) {
                        btnConfirm.background =
                            ContextCompat.getDrawable(requireContext(), R.drawable.button_background_long)
                        btnConfirm.isEnabled = true

                    } else {
                        btnConfirm.background =
                            ContextCompat.getDrawable(requireContext(), R.drawable.button_background_long_faded)
                        btnConfirm.isEnabled = false
                    }
                }
            })

            // listener editText newPassword
            etNewPassword.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (etCurrentPassword.text.toString().trim().isNotEmpty() &&
                        etNewPassword.text.toString().trim().isNotEmpty() &&
                        etConfirmPassword.text.toString().trim().isNotEmpty()
                    ) {
                        btnConfirm.background =
                            ContextCompat.getDrawable(requireContext(), R.drawable.button_background_long)
                        btnConfirm.isEnabled = true

                    } else {
                        btnConfirm.background =
                            ContextCompat.getDrawable(requireContext(), R.drawable.button_background_long_faded)
                        btnConfirm.isEnabled = false
                    }
                }
            })

            // listener editText rePassword
            etConfirmPassword.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (etCurrentPassword.text.toString().trim().isNotEmpty() &&
                        etNewPassword.text.toString().trim().isNotEmpty() &&
                        etConfirmPassword.text.toString().trim().isNotEmpty()
                    ) {
                        btnConfirm.background =
                            ContextCompat.getDrawable(requireContext(), R.drawable.button_background_long)
                        btnConfirm.isEnabled = true

                    } else {
                        btnConfirm.background =
                            ContextCompat.getDrawable(requireContext(), R.drawable.button_background_long_faded)
                        btnConfirm.isEnabled = false
                    }
                }
            })
        }
    }
}