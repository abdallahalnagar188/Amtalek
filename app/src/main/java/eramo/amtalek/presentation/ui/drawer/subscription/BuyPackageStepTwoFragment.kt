package eramo.amtalek.presentation.ui.drawer.subscription

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentBuyPackageStepTwoBinding
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.util.navOptionsAnimation

@AndroidEntryPoint
class BuyPackageStepTwoFragment : BindingFragment<FragmentBuyPackageStepTwoBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentBuyPackageStepTwoBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        setupToolbar()

        listeners()
    }

    private fun listeners() {
        binding.apply {
            tvBackToHome.setOnClickListener {
                findNavController().navigate(R.id.homeFragment, null, navOptionsAnimation())
            }

            btnSendNotes.setOnClickListener {
                initBottomSheetRatingDialog()
            }
        }
    }

    private fun setupToolbar() {
        binding.inToolbar.apply {
            tvTitle.text = getString(R.string.recharge_the_package_2)
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun initBottomSheetRatingDialog() {
        RateBottomDialogFragment().show(this@BuyPackageStepTwoFragment.childFragmentManager, "")
    }

}