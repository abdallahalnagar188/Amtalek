package eramo.amtalek.presentation.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.DialogSuspendBinding
import eramo.amtalek.presentation.viewmodel.dialog.SuspendDialogViewModel
import eramo.amtalek.util.API_SUCCESS_CODE
import eramo.amtalek.util.deeplink.DeeplinkUtil
import eramo.amtalek.util.showToast
import eramo.amtalek.util.state.UiState

@AndroidEntryPoint
class SuspendDialog : DialogFragment(R.layout.dialog_suspend) {
    private lateinit var binding: DialogSuspendBinding

    private val viewModel by viewModels<SuspendDialogViewModel>()
//    private val args by navArgs<SuspendDialogArgs>()
//    private val isRequestSuspend get() = args.isRequestSuspend

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setStyle(STYLE_NORMAL, R.style.DialogStyle)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DialogSuspendBinding.bind(view)

        setupViews()
        listeners()
        fetchData()

    }

    private fun setupViews() {
        isCancelable = true
    }

    private fun listeners() {
        binding.apply {
            DFCancelBtnConfirm.setOnClickListener {
                viewModel.suspendAccount()
            }

            DFCancelBtnCancel.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun fetchData() {
        fetchSuspendAccountState()
    }

    private fun fetchSuspendAccountState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.suspendAccountState.collect { state ->
                    when (state) {
                        is UiState.Success -> {
                            LoadingDialog.dismissDialog()

                            if (state.data?.status == API_SUCCESS_CODE) {
                                showToast(state.data.message)

                                Navigation.findNavController(requireActivity(), R.id.main_navHost)
                                    .navigate(
                                        NavDeepLinkRequest.Builder.fromUri(DeeplinkUtil.toLogin()).build(),
                                        NavOptions.Builder().setPopUpTo(R.id.nav_main, true).build()
                                    )
                            } else {
                                showToast(getString(R.string.something_went_wrong))
                            }

                        }

                        is UiState.Error -> {
                            LoadingDialog.dismissDialog()
                            val errorMessage = state.message!!.asString(requireContext())
                            showToast(errorMessage)

                            Navigation.findNavController(requireActivity(), R.id.main_navHost)
                                .navigate(
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

}