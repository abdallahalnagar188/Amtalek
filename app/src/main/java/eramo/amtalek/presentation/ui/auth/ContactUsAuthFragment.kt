package eramo.amtalek.presentation.ui.auth

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
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
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
import eramo.amtalek.presentation.viewmodel.auth.ContactUsAuthViewModel
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.openLinkInBrowser
import eramo.amtalek.util.showToast
import eramo.amtalek.util.state.UiState

@AndroidEntryPoint
class ContactUsAuthFragment : BindingFragment<FragmentContactUsAuthBinding>() {

    override val isRefreshingEnabled: Boolean get() = false
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentContactUsAuthBinding::inflate

    private val viewModel by viewModels<ContactUsAuthViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)

        setupToolbar()
        listeners()

        fetchData()
    }

    private fun setupToolbar() {
        binding.inToolbar.apply {
            tvTitle.text = getString(R.string.contact_us)
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun listeners() {
        sendBtnAvailability()
        binding.apply {
            btnSend.setOnClickListener { validateAndSendMessage() }
        }
    }

    private fun fetchData() {
        fetchGetContactUsInfoState()
    }

    private fun fetchGetContactUsInfoState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getContactUsInfoState.collect { state ->
                    when (state) {

                        is UiState.Success -> {
                            LoadingDialog.dismissDialog()
                            binding.apply {
                                FContactUsTvLocation.text = state.data?.address
                                FContactUsTvEmail.text = state.data?.mail
                                FContactUsTvPhone.text = state.data?.phone

                                FContactUsIvTwitter.setOnClickListener {
                                    this@ContactUsAuthFragment.openLinkInBrowser(state.data?.twitterUrl!!)
                                }
                                FContactUsIvWebsite.setOnClickListener {
                                    this@ContactUsAuthFragment.openLinkInBrowser(state.data?.linkedinUrl!!)
                                }
                                FContactUsIvInsta.setOnClickListener {
                                    this@ContactUsAuthFragment.openLinkInBrowser(state.data?.instagramUrl!!)
                                }
                                FContactUsIvFacebook.setOnClickListener {
                                    this@ContactUsAuthFragment.openLinkInBrowser(state.data?.facebookUrl!!)
                                }
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
}