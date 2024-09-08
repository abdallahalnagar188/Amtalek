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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.data.remote.dto.brokersDetails.Data
import eramo.amtalek.data.remote.dto.brokersDetails.Project
import eramo.amtalek.data.remote.dto.brokersProperties.OriginalItem
import eramo.amtalek.databinding.FragmentBrokerDetailsBinding
import eramo.amtalek.presentation.adapters.recyclerview.RvBrokerDetailsPropertiesAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
import eramo.amtalek.presentation.ui.interfaces.FavClickListenerOriginalItem
import eramo.amtalek.presentation.ui.main.home.HomeMyViewModel
import eramo.amtalek.presentation.ui.main.home.details.properties.PropertyDetailsFragmentArgs
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.enum.PropertyType
import eramo.amtalek.util.navOptionsAnimation
import eramo.amtalek.util.showToast
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BrokersDetailsFragment : BindingFragment<FragmentBrokerDetailsBinding>(),
    RvBrokerDetailsPropertiesAdapter.OnItemClickListener,
    FavClickListenerOriginalItem {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentBrokerDetailsBinding::inflate

    private val viewModelShared: SharedViewModel by viewModels()
    private val viewModel: BrokersViewModel by viewModels()
    private val homeViewModel: HomeMyViewModel by viewModels()

    @Inject
    lateinit var rvBrokerDetailsPropertiesAdapter: RvBrokerDetailsPropertiesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()


        val id = arguments?.getInt("id") ?: 0
        Log.e("id in fragment", id.toString())

        binding.btnProjects.setOnClickListener {

            findNavController().navigate(
                R.id.action_brokersDetailsFragment_to_completedProjectsFragment,
                bundleOf("id" to id),
                navOptionsAnimation()
            )

        }
        fetchData(id)
    }

    private fun fetchData(id: Int) {
        viewModel.getBrokersDetails(id)
        viewModel.getBrokersProperties(id)


//        lifecycleScope.launch {
//            viewModel.brokersDetails.collect { state ->
//                Log.e("details in fragment", state?.data.toString())
//                state?.data?.firstOrNull()?.let { assignData(it) }
//                Log.e("projects fragment", state?.data?.get(0)?.projects.toString())
//            }
//        }

        lifecycleScope.launch {
            viewModel.brokersDetails.collect {
                when (it) {
                    is Resource.Success -> {
                        it.data?.data?.first()?.let { it1 -> assignData(it1) }
                        LoadingDialog.dismissDialog()
                    }

                    is Resource.Error -> {
                        showToast(it.message.toString())
                        LoadingDialog.dismissDialog()
                    }

                    is Resource.Loading -> {
                        LoadingDialog.showDialog()
                    }
                }

            }
        }

        lifecycleScope.launch {
            viewModel.brokersProperties.collect { state ->
                when (state) {
                    is Resource.Success -> {
                        state.data?.data?.original?.let { initRv(it) }
                        LoadingDialog.dismissDialog()
                    }
                    is Resource.Error -> {
                        showToast(state.message.toString())
                        LoadingDialog.dismissDialog()
                    }
                    is Resource.Loading -> {
                        LoadingDialog.showDialog()
                    }
                }
            }
        }

        fetchAddRemoveToFavState(id)
    }

    private fun handleContactAction(propertyId: Int?, brokerId: Int, transactionType: String, brokerType: String) {
        viewModelShared.sendContactRequest(propertyId = propertyId ?: 0, brokerId = brokerId, brokerType = brokerType, transactionType = transactionType)
    }


//    private fun setupListeners(id: Int) {
//        binding.apply {
//
//
//        }
//    }

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

            rvForRent.visibility = View.GONE
            tvAllProjectsCount.text =
                "${model.property_for_sale?.plus(model.property_for_rent ?: 0)}  " + getString(R.string.property)
            tvTitle.text = model.name
            tvDescription.text = model.description
            tvLocation.visibility = View.GONE
            tvProjectsCount.text = "${model.projects_count}  " + getString(R.string.project)
            Glide.with(requireContext()).load(model.logo).into(ivBrokerLogo)
//            if (model.broker_type == "broker" && model.has_package == "yes") {
//                shareView.root.visibility = View.VISIBLE
//            } else {
//                shareView.root.visibility = View.GONE
//            }
        }
        binding.shareView.btnCall.setOnClickListener {
            if (UserUtil.isUserLogin()) {
                model.id?.let { it1 -> model.broker_type?.let { it2 -> handleContactAction(null, it1, "call", brokerType = it2) } }
                val phoneNumber = "+20${model.phone}"
                val callIntent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$phoneNumber")
                }
                startActivity(callIntent)
            } else {
                findNavController().navigate(R.id.loginDialog)
            }

        }

        binding.shareView.btnWhatsApp.setOnClickListener {
            if (UserUtil.isUserLogin()) {
                model.id?.let { it1 -> model.broker_type?.let { it2 -> handleContactAction(null, it1, "meeting", brokerType = it2) } }
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
            } else {
                findNavController().navigate(R.id.loginDialog)
            }

        }

        binding.shareView.btnMessaging.setOnClickListener {
            if (UserUtil.isUserLogin()) {
                model.id?.let { it1 -> handleContactAction(null, it1, "email", brokerType = model.broker_type.toString()) }
                val emailAddress = model.email
                val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:$emailAddress")
                    putExtra(Intent.EXTRA_SUBJECT, "Subject Here")
                    putExtra(Intent.EXTRA_TEXT, "Email body here")
                }
                startActivity(emailIntent)
            } else {
                findNavController().navigate(R.id.loginDialog)
            }

        }

    }

    private fun initRv(data: List<OriginalItem>) {
        rvBrokerDetailsPropertiesAdapter.setListener(
            this@BrokersDetailsFragment,
            this@BrokersDetailsFragment
        )
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

    private fun fetchAddRemoveToFavState(id: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.favState.collect() { state ->
                    when (state) {

                        is UiState.Success -> {
                            showToast(state.data?.message.toString())
                            viewModel.getBrokersDetails(id)

                           // homeViewModel.getHomeApis("1","1"

                        }

                        is UiState.Error -> {
                            val errorMessage = state.message!!.asString(requireContext())
                            showToast(errorMessage)
                        }

                        is UiState.Loading -> {
                        }

                        else -> {}
                    }
                }
            }
        }
    }
    override fun onFavClick(model: OriginalItem) {
        if (UserUtil.isUserLogin()){
            homeViewModel.addOrRemoveFav(model.id?:0)
        }else{
            findNavController().navigate(R.id.loginDialog)
        }

    }

}
