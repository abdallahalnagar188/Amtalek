package eramo.amtalek.presentation.ui.drawer.subscription

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentRechargePackageBinding
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.navOptionsAnimation

@AndroidEntryPoint
class RechargePackageFragment : BindingFragment<FragmentRechargePackageBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentRechargePackageBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        listeners()
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

    private fun listeners() {
        binding.apply {
            btnBuyNow.setOnClickListener {
                findNavController().navigate(R.id.buyPackageStepOneFragment, null, navOptionsAnimation())
            }

            btnAddCard.setOnClickListener {
                findNavController().navigate(R.id.addCardFragment, null, navOptionsAnimation())
            }
        }
    }
}