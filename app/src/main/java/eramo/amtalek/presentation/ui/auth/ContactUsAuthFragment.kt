package eramo.amtalek.presentation.ui.auth

import android.os.Bundle
import android.text.TextUtils
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
import eramo.amtalek.databinding.FragmentContactUsAuthBinding
import eramo.amtalek.domain.model.auth.ContactUsInfoModel
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
import eramo.amtalek.presentation.viewmodel.auth.ContactUsAuthViewModel
import eramo.amtalek.util.API_SUCCESS_CODE
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.onBackPressed
import eramo.amtalek.util.openLinkInBrowser
import eramo.amtalek.util.showToast
import eramo.amtalek.util.state.UiState

@AndroidEntryPoint
class ContactUsAuthFragment : BindingFragment<FragmentContactUsAuthBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentContactUsAuthBinding::inflate

    private val viewModel by viewModels<ContactUsAuthViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        listeners()
        fetchData()
    }

    private fun listeners() {
        sendBtnAvailability()
        binding.apply {
            btnSend.setOnClickListener { validateAndSendMessage() }
        }

        this@ContactUsAuthFragment.onBackPressed { findNavController().navigate(R.id.loginFragment) }
    }

    private fun fetchData() {
        fetchGetContactUsInfoState()
        fetchSendContactUsMessageState()
    }

    private fun fetchGetContactUsInfoState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getContactUsInfoState.collect { state ->
                    when (state) {

                        is UiState.Success -> {
                            LoadingDialog.dismissDialog()
                            assignContactUsInfoToTheViews(state.data!!)
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

    private fun fetchSendContactUsMessageState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.sendContactUsMessageState.collect { state ->
                    when (state) {

                        is UiState.Success -> {
                            LoadingDialog.dismissDialog()
                            if (state.data?.status == API_SUCCESS_CODE) {
                                showToast(state.data.message)
                                findNavController().navigate(R.id.loginFragment)
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

    private fun validateAndSendMessage() {
        binding.apply {
            val name = etName.text.toString().trim()
            val email = etMail.text.toString().trim()
            val mobileNumber = etMobileNumber.text.toString().trim()
            val message = etMessage.text.toString().trim()

            if (TextUtils.isEmpty(name)) {
                tilName.error = getString(R.string.please_enter_your_name)
                tilName.requestFocus()
                return
            } else {
                tilName.error = null
            }

            if (TextUtils.isEmpty(email)) {
                tilMail.error = getString(R.string.txt_email_is_required)
                return
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                tilMail.error = getString(R.string.txt_please_enter_a_valid_email_address)
                return
            } else {
                tilMail.error = null
            }

            if (TextUtils.isEmpty(mobileNumber)) {
                tilMobileNumber.error = getString(R.string.txt_phone_is_required)
                return
            } else if (mobileNumber.length != 11) {
                tilMobileNumber.error = getString(R.string.txt_please_enter_a_valid_phone_number)
                return
            } else {
                tilMobileNumber.error = null
            }

            if (TextUtils.isEmpty(message)) {
                tilMessage.error = getString(R.string.what_do_you_want_to_tell_us_about)
                return
            } else {
                tilMessage.error = null
            }

            viewModel.sendContactUsMessage(name, mobileNumber, email, message)
        }
    }

    private fun sendBtnAvailability() {
        binding.apply {

            // default
            btnSend.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.button_background_long_faded)
            btnSend.isEnabled = false


            // listener
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    btnSend.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.button_background_long)
                    btnSend.isEnabled = true
                } else {
                    btnSend.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.button_background_long_faded)
                    btnSend.isEnabled = false
                }
            }
        }
    }

    private fun assignContactUsInfoToTheViews(model: ContactUsInfoModel) {
        binding.apply {
            FContactUsTvLocation.text = model.address
            FContactUsTvEmail.text = model.mail
            FContactUsTvPhone.text = model.phone

            FContactUsIvTwitter.setOnClickListener {
                this@ContactUsAuthFragment.openLinkInBrowser(model.twitterUrl)
            }
            FContactUsIvWebsite.setOnClickListener {
                this@ContactUsAuthFragment.openLinkInBrowser(model.linkedinUrl)
            }
            FContactUsIvInsta.setOnClickListener {
                this@ContactUsAuthFragment.openLinkInBrowser(model.instagramUrl)
            }
            FContactUsIvFacebook.setOnClickListener {
                this@ContactUsAuthFragment.openLinkInBrowser(model.facebookUrl)
            }
        }
    }
}