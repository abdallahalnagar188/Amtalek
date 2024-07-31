package eramo.amtalek.presentation.ui.main.broker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.data.remote.dto.brokersDetails.Data
import eramo.amtalek.data.remote.dto.brokersProperties.OriginalItem
import eramo.amtalek.databinding.FragmentBrokerDetailsBinding
import eramo.amtalek.presentation.adapters.recyclerview.RvBrokerDetailsPropertiesAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.main.home.details.properties.PropertyDetailsFragmentArgs
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.enum.PropertyType
import eramo.amtalek.util.navOptionsAnimation
import eramo.amtalek.util.showToast
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BrokersDetailsFragment : BindingFragment<FragmentBrokerDetailsBinding>(),
    RvBrokerDetailsPropertiesAdapter.OnItemClickListener{

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentBrokerDetailsBinding::inflate

    private val viewModelShared: SharedViewModel by viewModels()
    private val viewModel: BrokersViewModel by viewModels()

    @Inject
    lateinit var rvBrokerDetailsPropertiesAdapter: RvBrokerDetailsPropertiesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupListeners()

        val id = arguments?.getInt("id") ?: 0
        Log.e("id in fragment", id.toString())

        fetchData(id)
    }

    private fun fetchData(id: Int) {
        viewModel.getBrokersDetails(id)
        viewModel.getBrokersProperties(id)

        lifecycleScope.launch {
            viewModel.brokersDetails.collect { state ->
                Log.e("details in fragment", state?.data.toString())
                state?.data?.firstOrNull()?.let { assignData(it) }
            }
        }

        lifecycleScope.launch {
            viewModel.brokersProperties.collect { state ->
                Log.e("properties in fragment", state?.data.toString())
                initRv(state?.data?.original ?: emptyList())
            }
        }

    }

    private fun handleContactAction(propertyId: Int?, brokerId: Int, transactionType: String) {
        viewModelShared.sendContactRequest(propertyId?:0, brokerId, transactionType)
    }

    private fun setupListeners() {
        binding.apply {

            btnProjects.setOnClickListener {
                findNavController().navigate(R.id.completedProjectsFragment,
                    bundleOf("id" to arguments?.getInt("id")), navOptionsAnimation())
            }
//            btnSatisfiedCustomers.setOnClickListener {
//                findNavController().navigate(R.id.satisfiedCustomersFragment, null, navOptionsAnimation())
//            }
        }
    }

    private fun setupToolbar() {
        StatusBarUtil.transparent()
        binding.apply {
            ivShare.setOnClickListener { showToast("share") }
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun assignData(model: Data) {
        binding.apply {
            tvAllProjectsCount.text = "${model.property_for_sale?.plus(model.property_for_rent ?: 0)} properties"
            tvTitle.text = model.name
            tvDescription.text = model.description
            tvLocation.text = model.phone
            tvProjectsCount.text = "${model.projects_count} projects"
            Glide.with(requireContext()).load(model.logo).into(ivBrokerLogo)
        }
        binding.shareView.btnCall.setOnClickListener {
            if (UserUtil.isUserLogin()){
                model.id?.let { it1 -> handleContactAction(null, it1, "call") }
                val phoneNumber = "+20${model.phone}"
                val callIntent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$phoneNumber")
                }
                startActivity(callIntent)
            }else{
                findNavController().navigate(R.id.loginDialog)
            }

        }

        binding.shareView.btnWhatsApp.setOnClickListener {
            if (UserUtil.isUserLogin()){
                model.id?.let { it1 -> handleContactAction(null, it1, "meeting") }
                val phoneNumber = model.phone
                val url = "https://api.whatsapp.com/send?phone=+20$phoneNumber"
                val sendIntent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(url)
                }
                try {
                    startActivity(sendIntent)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "WhatsApp is not installed on your device", Toast.LENGTH_LONG).show()
                }
            }else{
                findNavController().navigate(R.id.loginDialog)
            }

        }

        binding.shareView.btnMessaging.setOnClickListener {
            if (UserUtil.isUserLogin()){
                model.id?.let { it1 -> handleContactAction(null, it1, "email") }
                val emailAddress = model.email
                val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:$emailAddress")
                    putExtra(Intent.EXTRA_SUBJECT, "Subject Here")
                    putExtra(Intent.EXTRA_TEXT, "Email body here")
                }
                startActivity(emailIntent)
            }else{
                findNavController().navigate(R.id.loginDialog)
            }

        }

    }

    private fun initRv(data: List<OriginalItem>) {
        rvBrokerDetailsPropertiesAdapter.setListener(this@BrokersDetailsFragment)
        binding.rv.adapter = rvBrokerDetailsPropertiesAdapter
        rvBrokerDetailsPropertiesAdapter.submitList(data)
    }

    override fun onPause() {
        super.onPause()
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)
    }

    override fun onPropertyClick(model: OriginalItem) {
        //findNavController().navigate(R.id.propertyDetailsSellFragment, bundleOf("id" to model.id))

        when (model.forWhat) {
            PropertyType.FOR_SELL.key -> {
                findNavController().navigate(
                    R.id.propertyDetailsFragment,
                    model.listingNumber?.let { PropertyDetailsFragmentArgs(it).toBundle() }
                )
            }

            PropertyType.FOR_RENT.key -> {
                findNavController().navigate(
                    R.id.propertyDetailsFragment,
                    model.listingNumber?.let { PropertyDetailsFragmentArgs(it).toBundle() }
                )
            }

            PropertyType.FOR_BOTH.key -> {
                findNavController().navigate(
                    R.id.propertyDetailsFragment,
                    model.listingNumber?.let { PropertyDetailsFragmentArgs(it).toBundle() }
                )
            }
        }
    }

}
