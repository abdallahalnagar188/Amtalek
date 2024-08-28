package eramo.amtalek.presentation.ui.main.user

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.data.remote.dto.brokersProperties.OriginalItem
import eramo.amtalek.data.remote.dto.userDetials.Data
import eramo.amtalek.data.remote.dto.userDetials.SubmittedPropsForRent
import eramo.amtalek.data.remote.dto.userDetials.SubmittedPropsForSale
import eramo.amtalek.databinding.FragmentBrokerDetailsBinding
import eramo.amtalek.presentation.adapters.recyclerview.RvUserDetailsPropertiesForRentAdapter
import eramo.amtalek.presentation.adapters.recyclerview.RvUserDetailsPropertiesForSallAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.main.broker.BrokersViewModel
import eramo.amtalek.presentation.ui.main.home.HomeMyViewModel
import eramo.amtalek.presentation.ui.main.home.details.properties.PropertyDetailsFragmentArgs
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.enum.PropertyType
import eramo.amtalek.util.showToast
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UserDetailsFragment : BindingFragment<FragmentBrokerDetailsBinding>(),
    RvUserDetailsPropertiesForSallAdapter.OnItemClickListener,
    RvUserDetailsPropertiesForSallAdapter.FavClickListenerOriginalItem, RvUserDetailsPropertiesForRentAdapter.OnItemClickListener {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentBrokerDetailsBinding::inflate

    private val viewModelShared: SharedViewModel by viewModels()
    private val viewModel: BrokersViewModel by viewModels()
    private val homeViewModel: HomeMyViewModel by viewModels()

    @Inject
    lateinit var rvBrokerDetailsPropertiesAdapter: RvUserDetailsPropertiesForSallAdapter


    lateinit var id: String
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupListeners()

        id = arguments.let {
            UserDetailsFragmentArgs.fromBundle(it!!).id
        }
        Log.e("id in fragment", id)

        fetchData(id)
    }

    private fun fetchData(id: String) {

        viewModel.getUserDetails(id.toInt())
        lifecycleScope.launch {
            viewModel.userDetails.collectLatest { state ->
                state?.data?.get(0)?.let { assignData(it) }
                state?.data?.get(0)?.let { initRv(it) }
                Log.e("prop", state?.data?.get(0)?.getAllSubmittedProps().toString())
               // initRvForRent(state?.data?.get(0)?.submitted_props_for_rent ?: emptyList())
            }
        }
        fetchAddRemoveToFavState()
    }

    private fun handleContactAction(propertyId: Int?, brokerId: Int, transactionType: String, brokerType: String) {
        viewModelShared.sendContactRequest(propertyId ?: 0, brokerId, brokerType = brokerType, transactionType)
    }

    private fun setupListeners() {
        binding.apply {

//            btnProjects.setOnClickListener {
//                findNavController().navigate(R.id.completedProjectsFragment,
//                    bundleOf("id" to arguments?.getInt("id")), navOptionsAnimation())
//            }
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

    private fun assignData(model: Data) {
        binding.apply {
            // Set title, description, and other properties
            tvTitle.text = model.name
            tvDescription.text = model.description
            tvLocation.visibility = View.GONE
            tvAllProjectsCount.text = getString(R.string.all_properties)
            btnProjects.visibility = View.GONE
            Glide.with(requireContext()).load(model.logo).into(ivBrokerLogo)
            initRv(model)

            // Set the listeners as before

            binding.shareView.btnCall.setOnClickListener {
                if (UserUtil.isUserLogin()) {
                    model.id?.let { it1 -> model.broker_type?.let { it2 -> handleContactAction(null, it1, "call", it2) } }
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
                    model.id?.let { it1 -> model.broker_type?.let { it2 -> handleContactAction(null, it1, "meeting", it2) } }
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
                    model.id?.let { it1 -> model.broker_type?.let { it2 -> handleContactAction(null, it1, "email", it2) } }
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
    }


    private fun initRv(data: Data) {
        // Set the listeners as before
        rvBrokerDetailsPropertiesAdapter.setListener(
            this@UserDetailsFragment,
            this@UserDetailsFragment
        )

        // Set the adapter to your RecyclerView
        binding.rv.adapter = rvBrokerDetailsPropertiesAdapter

        // Combine the lists and submit the combined list to the adapter
        val combinedList = data.getAllSubmittedProps()
        rvBrokerDetailsPropertiesAdapter.submitList(combinedList)

    }

    override fun onPause() {
        super.onPause()
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)
    }

    override fun onPropertyClick(model: Any) {
        when (model) {
            is SubmittedPropsForSale -> {
                when (model.for_what) {
                    PropertyType.FOR_SELL.key, PropertyType.FOR_BOTH.key -> {
                        findNavController().navigate(
                            R.id.propertyDetailsFragment,
                            model.listing_number?.let {
                                PropertyDetailsFragmentArgs(it).toBundle()
                            }
                        )
                    }
                }
            }

            is SubmittedPropsForRent -> {
                when (model.for_what) {
                    PropertyType.FOR_RENT.key, PropertyType.FOR_BOTH.key -> {
                        findNavController().navigate(
                            R.id.propertyDetailsFragment,
                            model.listing_number?.let {
                                PropertyDetailsFragmentArgs(it).toBundle()
                            }
                        )
                    }
                }
            }
        }
    }
    private fun fetchAddRemoveToFavState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.favState.collect() { state ->
                    when (state) {

                        is UiState.Success -> {
                            showToast(state.data?.message.toString())

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

    override fun onFavClick(model: SubmittedPropsForSale) {
        if (UserUtil.isUserLogin()){
            homeViewModel.addOrRemoveFav(model.id?:0)
        }else{
            findNavController().navigate(R.id.loginDialog)
        }

    }

    override fun onPropertyClick(model: SubmittedPropsForRent) {
        when (model.for_what) {
            PropertyType.FOR_SELL.key -> {
                findNavController().navigate(
                    R.id.propertyDetailsFragment,
                    model.listing_number?.let {
                        PropertyDetailsFragmentArgs(
                            it
                        ).toBundle()
                    }
                )
            }

            PropertyType.FOR_RENT.key -> {
                findNavController().navigate(
                    R.id.propertyDetailsFragment,
                    model.listing_number?.let {
                        PropertyDetailsFragmentArgs(
                            it
                        ).toBundle()
                    }
                )
            }

            PropertyType.FOR_BOTH.key -> {
                findNavController().navigate(
                    R.id.propertyDetailsFragment,
                    model.listing_number?.let {
                        PropertyDetailsFragmentArgs(
                            it
                        ).toBundle()
                    }
                )
            }
        }
    }

}
