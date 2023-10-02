package eramo.amtalek.presentation.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.DialogSuspendBinding
import eramo.amtalek.presentation.ui.auth.LoginFragmentArgs
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.deeplink.DeeplinkUtil
import eramo.amtalek.util.showToast
import eramo.amtalek.util.state.UiState

@AndroidEntryPoint
class SuspendDialog : DialogFragment(R.layout.dialog_suspend) {
    private lateinit var binding: DialogSuspendBinding
//    private val viewModel by viewModels<SuspendDialogViewModel>()
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

        binding.apply {
//            if (isRequestSuspend) tvSuspendMessage.text =
//                getString(R.string.are_you_sure_to_suspend)
//            else tvSuspendMessage.text = getString(R.string.this_account_is_suspended)

            DFCancelBtnCancel.setOnClickListener { findNavController().popBackStack() }

            DFCancelBtnConfirm.setOnClickListener {
                UserUtil.clearUserInfo()
                Navigation.findNavController(requireActivity(), R.id.main_navHost)
                    .navigate(
                        NavDeepLinkRequest.Builder.fromUri(DeeplinkUtil.toLogin()).build(),
                        NavOptions.Builder().setPopUpTo(R.id.nav_main, true).build()
                    )
            }
        }
    }

}