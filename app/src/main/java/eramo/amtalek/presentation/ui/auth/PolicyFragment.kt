package eramo.amtalek.presentation.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentPolicyBinding
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.viewmodel.auth.PolicyViewModel
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
import eramo.amtalek.util.Constants
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.state.UiState
import eramo.amtalek.util.showToast

@AndroidEntryPoint
class PolicyFragment : BindingFragment<FragmentPolicyBinding>() {

    override val isRefreshingEnabled: Boolean get() = false
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentPolicyBinding::inflate

    private val viewModel by viewModels<PolicyViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.registerApiCancellation { viewModel.cancelRequest() }
        StatusBarUtil.transparent()

        binding.apply {
            ivClose.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
}