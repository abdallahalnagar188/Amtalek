package eramo.amtalek.presentation.ui.drawer.subscription

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentPackageDetailsBinding
import eramo.amtalek.domain.model.drawer.PackageModel
import eramo.amtalek.domain.model.profile.ProfileModel
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.drawer.myaccount.MyAccountFragment
import eramo.amtalek.presentation.ui.main.home.seemore.SeeMoreProjectsFragmentArgs
import eramo.amtalek.presentation.ui.social.MyProfileFragment
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
        //setupData()

    }

    private fun setupToolbar() {
        binding.inToolbar.apply {
            tvTitle.text = getString(R.string.packages_details)
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun listeners() {
        binding.apply {
//            btnRechargePackage.setOnClickListener {
//                findNavController().navigate(R.id.rechargePackageFragment, null, navOptionsAnimation())
//            }

//            btnChangePackage.visibility = View.GONE
//            btnRechargePackage.visibility = View.GONE
//            btnChangePackage.setOnClickListener {
//                findNavController().navigate(R.id.packagesFragment, null, navOptionsAnimation())
//            }
        }
    }

    private fun setupData(model: ProfileModel) {
        binding.apply {
            tvCardTitle.visibility = View.GONE
            tvDescription.visibility = View.GONE
//            tvCardTitle.text = model.currentPackage?.packageInfo?.get(0)?.title
//            tvDescription.text = model.currentPackage?.packageInfo?.get(0)?.title
            tvPrice.text = model.currentPackage?.actualPayment
            tvExpirationDate.text = model.currentPackage?.expirationDate?.expirationDate
//            tvPayment.text = model.currentPackage?.payment?.payment

            tvNormalListing.text = getString(R.string.s_normal_listing, model.currentPackage?.packageInfo?.get(0)?.base.toString())
            tvFeaturedListing.text = getString(R.string.s_featured_listings, model.currentPackage?.packageInfo?.get(1)?.base.toString())
            tvSocialMediaPromotion.text =
                getString(R.string.s_social_media_promotion, model.currentPackage?.packageInfo?.get(2)?.base.toString())
            tvLeadsManagement.text = getString(R.string.s_leads_management, model.currentPackage?.packageInfo?.get(3)?.base.toString())

//            tvExtraLeads.text = getString(R.string.s_extra_lead, "25")

            cover.setBackgroundColor(Color.parseColor("#1E617A"))
            cvPrice.setCardBackgroundColor(Color.parseColor("#1E617A"))
        }
    }
}