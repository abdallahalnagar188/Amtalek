package eramo.amtalek.presentation.ui.drawer.subscription

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentBuyPackageStepTwoBinding
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.util.StatusBarUtil

@AndroidEntryPoint
class BuyPackageStepTwoFragment : BindingFragment<FragmentBuyPackageStepTwoBinding>() {

    override val isRefreshingEnabled: Boolean get() = false
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentBuyPackageStepTwoBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)
        setupToolbar()

    }

    private fun setupToolbar() {
        binding.inToolbar.apply {
            tvTitle.text = getString(R.string.recharge_the_package_2)
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

}