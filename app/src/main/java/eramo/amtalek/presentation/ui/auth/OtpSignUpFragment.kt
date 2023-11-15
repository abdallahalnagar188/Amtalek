package eramo.amtalek.presentation.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentOtpBinding
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.viewmodel.auth.OtpSignUpViewModel
import eramo.amtalek.util.StatusBarUtil

@AndroidEntryPoint
class OtpSignUpFragment : BindingFragment<FragmentOtpBinding>() {

    override val isRefreshingEnabled: Boolean get() = false
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentOtpBinding::inflate

    private val viewModel by viewModels<OtpSignUpViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        listeners()
        fetchData()
    }

    private fun listeners() {
        binding.apply {
            FOtpIvBack.setOnClickListener { findNavController().popBackStack() }
            FOtpTvResend.setOnClickListener { viewModel.startTimer() }

        }
    }

    private fun setupViews(){
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)

        setupEditTextsListener()
    }

    private fun fetchData(){
        fetchTimerState()
    }

    // -------------------------------------- setupViews -------------------------------------- //
    private fun setupEditTextsListener() {
        binding.apply {

            FOtpEtOne.requestFocus()
            FOtpBtnConfirm.background = ContextCompat.getDrawable(requireContext(), R.drawable.button_background_long_faded)
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
                if (FOtpEtThree.text?.length == 1) {
                    FOtpEtFive.requestFocus()
                } else {
                    FOtpEtThree.requestFocus()
                }
            }

            FOtpEtFive.addTextChangedListener {
                if (FOtpEtFive.text?.length == 0) {
                    FOtpEtFour.requestFocus()
                    FOtpBtnConfirm.background = ContextCompat.getDrawable(requireContext(), R.drawable.button_background_long_faded)
                    FOtpBtnConfirm.isEnabled = false
                } else {
                    FOtpBtnConfirm.background = ContextCompat.getDrawable(requireContext(), R.drawable.button_background_long)
                    FOtpBtnConfirm.isEnabled = true
                }
            }
        }
    }

    // -------------------------------------- fetchData -------------------------------------- //
    private fun fetchTimerState() {
        lifecycleScope.launchWhenCreated {
            viewModel.timerState.collect { time ->
                binding.FOtpTvTimer.text = time

                if (time == "0:00") {
                    binding.FOtpTvResend.visibility = View.VISIBLE
                } else {
                    binding.FOtpTvResend.visibility = View.GONE
                }
            }
        }
    }

}