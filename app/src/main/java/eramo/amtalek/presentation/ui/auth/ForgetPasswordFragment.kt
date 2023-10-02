package eramo.amtalek.presentation.ui.auth

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentForgetPasswordBinding
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.util.showToast
import eramo.amtalek.presentation.viewmodel.auth.ForgotPassViewModel
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.state.UiState

@AndroidEntryPoint
class ForgetPasswordFragment : BindingFragment<FragmentForgetPasswordBinding>() {

    override val isRefreshingEnabled: Boolean get() = false
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentForgetPasswordBinding::inflate

    private val viewModel by viewModels<ForgotPassViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.registerApiCancellation { viewModel.cancelRequest() }
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.gray_low)

        binding.FForgetBtnForget.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}