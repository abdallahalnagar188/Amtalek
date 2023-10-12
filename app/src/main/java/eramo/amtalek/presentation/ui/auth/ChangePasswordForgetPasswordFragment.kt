package eramo.amtalek.presentation.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentChangePasswordForgetPasswordBinding
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.viewmodel.auth.ChangePasswordForgetPasswordViewModel
import eramo.amtalek.util.StatusBarUtil


class ChangePasswordForgetPasswordFragment : BindingFragment<FragmentChangePasswordForgetPasswordBinding>() {

    override val isRefreshingEnabled: Boolean get() = false
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentChangePasswordForgetPasswordBinding::inflate

    private val viewModel by viewModels<ChangePasswordForgetPasswordViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        listeners()
    }

    private fun listeners() {
        binding.apply {
            FChangePasswordBtnConfirm.setOnClickListener {
                findNavController().popBackStack(R.id.loginFragment,false)
            }

        }
    }

    private fun setupViews() {
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)

    }

}