package eramo.amtalek.presentation.ui.drawer.subscription

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentPackageDetailsBinding
import eramo.amtalek.domain.model.drawer.PackageModel
import eramo.amtalek.domain.model.profile.ProfileModel
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
import eramo.amtalek.presentation.ui.drawer.myaccount.MyAccountFragment
import eramo.amtalek.presentation.ui.main.home.details.NewsDetailsFragmentArgs
import eramo.amtalek.presentation.ui.main.home.seemore.SeeMoreProjectsFragmentArgs
import eramo.amtalek.presentation.ui.social.MyProfileFragment
import eramo.amtalek.presentation.viewmodel.drawer.myaccount.MyAccountViewModel
import eramo.amtalek.presentation.viewmodel.social.MyProfileViewModel
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.navOptionsAnimation
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PackageDetailsFragment : BindingFragment<FragmentPackageDetailsBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentPackageDetailsBinding::inflate


    private val viewModel: MyProfileViewModel by viewModels()

//    val args: PackageDetailsFragmentArgs by navArgs()
//    private val packageModel get() = args.profileModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getProfile(UserUtil.getUserType(), UserUtil.getUserId())
        setupViews()
        //  listeners()

    }

    private fun setupViews() {
        setupToolbar()
        //   setupData(packageModel)


    }

    override fun onResume() {
        super.onResume()
        getCurrentPackage()
    }

    private fun getCurrentPackage() {
        lifecycleScope.launch {
            viewModel.getProfileState.collectLatest {
                when (it) {
                    is Resource.Success -> {
                        it.data?.let { it1 -> setupData(it1) }
                        LoadingDialog.dismissDialog()
                    }

                    is Resource.Error -> {
                        LoadingDialog.dismissDialog()

                    }

                    is Resource.Loading -> {
                        LoadingDialog.showDialog()
                    }
                }

            }
        }
    }

    private fun setupToolbar() {
        binding.inToolbar.apply {
            tvTitle.text = getString(R.string.packages_details)
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

//    private fun listeners() {
//        binding.apply {
////            btnRechargePackage.setOnClickListener {
////                findNavController().navigate(R.id.rechargePackageFragment, null, navOptionsAnimation())
////            }
//
////            btnChangePackage.visibility = View.GONE
////            btnRechargePackage.visibility = View.GONE
////            btnChangePackage.setOnClickListener {
////                findNavController().navigate(R.id.packagesFragment, null, navOptionsAnimation())
////            }
//        }
//    }

    private fun setupData(model: ProfileModel) {

        binding.apply {
            tvCardTitle.visibility = View.GONE
            tvDescription.visibility = View.GONE
//            tvCardTitle.text = model.currentPackage?.packageInfo?.get(0)?.title
//            tvDescription.text = model.currentPackage?.packageInfo?.get(0)?.title
            tvPrice.text = model.currentPackage?.actualPayment
            tvExpirationDate.text = model.currentPackage?.expirationDate?.expirationDate

            when (model.currentPackage?.expirationDate?.paymentMethod) {
                "visa" -> {
                    tvPayment.text = getString(R.string.visa)
                }

                else -> {
                    tvPayment.text = getString(R.string.payment_cash)
                }


            }

            // tvPayment.text = model.currentPackage?.expirationDate?.paymentMethod

            tvNormalListing.text = getString(R.string.normal_listings)
            basedNumberOfNormal.text = model.currentPackage?.packageInfo?.get(1)?.used.toString()
            usedNumberOfNormal.text = model.currentPackage?.packageInfo?.get(1)?.base.toString()
            tvFeaturedListing.text = getString(R.string.featured_listings)
            basedNumberOfFeaturedListing.text = model.currentPackage?.packageInfo?.get(0)?.used.toString()
            usedNumberOfFeaturedListing.text = model.currentPackage?.packageInfo?.get(0)?.base.toString()
            tvProjects.text = getString(R.string.projects)
            basedNumberOfProjects.text = model.currentPackage?.packageInfo?.get(2)?.used.toString()
            usedNumberOfProjects.text = model.currentPackage?.packageInfo?.get(2)?.base.toString()
            tvLeadsManagement.text = getString(R.string.messages)
            basedNumberOfMessages.text = model.currentPackage?.packageInfo?.get(3)?.used.toString()
            usedNumberOfMessages.text = model.currentPackage?.packageInfo?.get(3)?.base.toString()
//            tvExtraLeads.text = getString(R.string.s_extra_lead, "25")

            cover.setBackgroundColor(Color.parseColor("#1E617A"))
            cvPrice.setCardBackgroundColor(Color.parseColor("#1E617A"))
        }
    }
}