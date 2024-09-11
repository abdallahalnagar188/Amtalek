package eramo.amtalek.presentation.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentPolicyBinding
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.viewmodel.auth.TermsAndConditionsViewModel

@AndroidEntryPoint
class TermsAndConditionsFragment : BindingFragment<FragmentPolicyBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentPolicyBinding::inflate

    private val viewModel by viewModels<TermsAndConditionsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                private var backPressCount = 0 // Counter to track back presses

                override fun handleOnBackPressed() {
                    val navController = NavHostFragment.findNavController(this@TermsAndConditionsFragment)

                    if (backPressCount == 0) {
                        // First back press: Navigate to the SignUpFragment
                        navController.navigate(
                            R.id.signUpFragment,
                            null,
                            NavOptions.Builder()
                                .setPopUpTo(R.id.termsAndConditionsFragment, true) // Pop the current fragment from the back stack
                                .build()
                        )
                        backPressCount++ // Increment the counter
                    } else {
                        // Second back press: Exit the app
                        requireActivity().finish() // Finish the app
                    }
                }
            }
        )


        binding.apply {
            ivClose.setOnClickListener {
                val navController = NavHostFragment.findNavController(this@TermsAndConditionsFragment)
                navController.navigate(
                    R.id.signUpFragment,
                    null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.termsAndConditionsFragment, true) // Replace with your current fragment's ID
                        .build()
                )
            }
        }
    }
}


