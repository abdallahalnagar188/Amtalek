package eramo.amtalek.presentation.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentOtpBinding
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
import eramo.amtalek.presentation.viewmodel.auth.OtpSignUpViewModel
import eramo.amtalek.util.API_SUCCESS_CODE
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.navOptionsAnimation
import eramo.amtalek.util.onBackPressed
import eramo.amtalek.util.showToast
import eramo.amtalek.util.state.UiState

@AndroidEntryPoint
class OtpSignUpFragment : BindingFragment<FragmentOtpBinding>() {

    override val isRefreshingEnabled: Boolean get() = false
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentOtpBinding::inflate

    private val viewModel by viewModels<OtpSignUpViewModel>()

    private val args by navArgs<OtpSignUpFragmentArgs>()
    private val registeredEmail get() = args.registeredEmail

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        listeners()
        fetchData()
    }

    private fun setupViews() {
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)

        binding.FOtpTvOtpSent.text = registeredEmail

        setupEditTextsListener()
    }

    private fun listeners() {
        binding.apply {
            FOtpIvBack.setOnClickListener { findNavController().popBackStack(R.id.loginFragment, false) }
            FOtpTvResend.setOnClickListener {
                viewModel.startTimer()
                FOtpTvResend.visibility = View.GONE
                FOtpTvHaveNotReceivedMessage.visibility = View.VISIBLE
                FOtpTvTimer.visibility = View.VISIBLE

            }
            FOtpBtnConfirm.setOnClickListener {
                viewModel.checkOtpCode(registeredEmail, enteredOtpCode())
            }
        }
        this@OtpSignUpFragment.onBackPressed { findNavController().popBackStack(R.id.loginFragment, false) }
    }

    private fun fetchData() {
        fetchTimerState()
        fetchEnableResendEvent()

        fetchCheckingOtpCodeState()
    }

    // -------------------------------------- setupViews -------------------------------------- //
    private fun setupEditTextsListener() {
        binding.apply {

            FOtpEtOne.requestFocus()
            FOtpBtnConfirm.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.button_background_long_faded)
            FOtpBtnConfirm.isEnabled = false

            FOtpEtOne.addTextChangedListener {
                if (FOtpEtOne.text?.length == 1) {
                    FOtpEtTwo.requestFocus()
                }
            }

            FOtpEtTwo.addTextChangedListener {
                if (FOtpEtTwo.text?.length == 1) {
                    FOtpEtThree.requestFocus()
                } else {
                    FOtpEtOne.requestFocus()
                }
            }

            FOtpEtThree.addTextChangedListener {
                if (FOtpEtThree.text?.length == 1) {
                    FOtpEtFour.requestFocus()
                } else {
                    FOtpEtTwo.requestFocus()
                }
            }

            FOtpEtFour.addTextChangedListener {
                if (FOtpEtFour.text?.length == 1) {
                    FOtpEtFive.requestFocus()
                } else {
                    FOtpEtThree.requestFocus()
                }
            }

            FOtpEtFive.addTextChangedListener {
                if (FOtpEtFive.text?.length == 0) {
                    FOtpEtFour.requestFocus()
                    FOtpBtnConfirm.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.button_background_long_faded)
                    FOtpBtnConfirm.isEnabled = false
                } else {
                    FOtpBtnConfirm.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.button_background_long)
                    FOtpBtnConfirm.isEnabled = true
                }
            }
        }
    }

    private fun enteredOtpCode(): String {
        binding.apply {
            return FOtpEtOne.text.toString().trim() + FOtpEtTwo.text.toString().trim() + FOtpEtThree.text.toString()
                .trim() + FOtpEtFour.text.toString().trim() + FOtpEtFive.text.toString().trim()
        }

    }

    // -------------------------------------- fetchData -------------------------------------- //
    private fun fetchTimerState() {
        lifecycleScope.launchWhenCreated {
            viewModel.timerState.collect { time ->
                binding.FOtpTvTimer.text = time
            }
        }
    }

    private fun fetchEnableResendEvent() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.enableResendEvent.collect { enableResend ->
                    if (enableResend) {
                        binding.FOtpTvResend.visibility = View.VISIBLE
                        binding.FOtpTvHaveNotReceivedMessage.visibility = View.GONE
                        binding.FOtpTvTimer.visibility = View.GONE

                    } else {
                        binding.FOtpTvResend.visibility = View.GONE
                        binding.FOtpTvHaveNotReceivedMessage.visibility = View.VISIBLE
                        binding.FOtpTvTimer.visibility = View.VISIBLE
                    }

                }
            }
        }
    }

    private fun fetchCheckingOtpCodeState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.checkOtpCodeState.collect { state ->
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

}