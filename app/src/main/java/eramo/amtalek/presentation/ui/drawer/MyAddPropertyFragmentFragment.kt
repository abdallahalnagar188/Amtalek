package eramo.amtalek.presentation.ui.drawer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentMyAddPropertyFragmentBinding
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.showToast

@AndroidEntryPoint
class MyAddPropertyFragmentFragment : BindingFragment<FragmentMyAddPropertyFragmentBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentMyAddPropertyFragmentBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews(){
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)

        setupToolbar()
        listeners()
    }

    private fun setupToolbar() {
        binding.inToolbar.apply {
            tvTitle.text = getString(R.string.add_property)
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun listeners(){
        binding.apply {
            btnAddProperty.setOnClickListener {
                showToast(getString(R.string.property_added))
                findNavController().popBackStack()
            }
        }
    }
}