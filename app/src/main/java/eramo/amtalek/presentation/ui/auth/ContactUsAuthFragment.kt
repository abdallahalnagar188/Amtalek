package eramo.amtalek.presentation.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentContactUsAuthBinding
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.util.StatusBarUtil

@AndroidEntryPoint
class ContactUsAuthFragment : BindingFragment<FragmentContactUsAuthBinding>() {

    override val isRefreshingEnabled: Boolean get() = false
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentContactUsAuthBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)

        setupToolbar()
        listeners()
    }

    private fun setupToolbar() {
        binding.inToolbar.apply {
            tvTitle.text = getString(R.string.contact_us)
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun listeners() {
        binding.apply {
//            btnAddProperty.setOnClickListener {
//                showToast(getString(R.string.property_added))
//                findNavController().popBackStack()
//            }
        }
    }
}