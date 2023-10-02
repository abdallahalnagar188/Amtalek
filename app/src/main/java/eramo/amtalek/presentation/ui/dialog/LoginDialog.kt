package eramo.amtalek.presentation.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import eramo.amtalek.R
import eramo.amtalek.databinding.DialogLoginBinding
import eramo.amtalek.util.deeplink.DeeplinkUtil

class LoginDialog : DialogFragment(R.layout.dialog_login) {
    private lateinit var binding: DialogLoginBinding
    private val args by navArgs<LoginDialogArgs>()
    private val proceedRequire get() = args.proceedRequire

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
        binding = DialogLoginBinding.bind(view)

        binding.apply {
            DFLogInBtnCancel.setOnClickListener { findNavController().popBackStack() }

            DFLogInBtnLogin.setOnClickListener {
                findNavController().popBackStack()
                findNavController().navigate(
                    NavDeepLinkRequest.Builder.fromUri(DeeplinkUtil.toLogin(proceedRequire)).build()
                )
            }
        }
    }
}