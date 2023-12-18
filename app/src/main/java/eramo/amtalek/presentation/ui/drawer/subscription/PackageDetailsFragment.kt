package eramo.amtalek.presentation.ui.drawer.subscription

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentPackageDetailsBinding
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.util.navOptionsAnimation

@AndroidEntryPoint
class PackageDetailsFragment : BindingFragment<FragmentPackageDetailsBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentPackageDetailsBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        listeners()

    }

    private fun setupViews() {
        setupToolbar()

        setupFakeData()
    }

    private fun setupToolbar() {
        binding.inToolbar.apply {
            tvTitle.text = getString(R.string.packages_details)
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun listeners() {
        binding.apply {
            btnRechargePackage.setOnClickListener {
                findNavController().navigate(R.id.rechargePackageFragment, null, navOptionsAnimation())
            }

            btnChangePackage.setOnClickListener {
                findNavController().navigate(R.id.packagesFragment, null, navOptionsAnimation())
            }
        }
    }

    private fun setupFakeData() {
        binding.apply {
            tvCardTitle.text = "Free"
            tvDescription.text = "is free and will always free"
            tvPrice.text = "0"

            tvNormalListing.text = getString(R.string.s_normal_listing, "5")
            tvFeaturedListing.text = getString(R.string.s_featured_listings, "1")
            tvSocialMediaPromotion.text = getString(R.string.s_social_media_promotion, "1")
            tvLeadsManagement.text = getString(R.string.s_leads_management, "10")

            tvExtraLeads.text = getString(R.string.s_extra_lead, "25")

            cover.setBackgroundColor(Color.parseColor("#1E617A"))
            cvPrice.setCardBackgroundColor(Color.parseColor("#1E617A"))
        }
    }
}