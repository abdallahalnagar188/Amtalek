package eramo.amtalek.presentation.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentPolicyBinding
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.viewmodel.auth.TermsAndConditionsViewModel
import eramo.amtalek.util.StatusBarUtil

@AndroidEntryPoint
class TermsAndConditionsFragment : BindingFragment<FragmentPolicyBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentPolicyBinding::inflate

    private val viewModel by viewModels<TermsAndConditionsViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            ivClose.setOnClickListener {
                findNavController().navigate(R.id.signUpFragment)
            }
        }
    }
}

