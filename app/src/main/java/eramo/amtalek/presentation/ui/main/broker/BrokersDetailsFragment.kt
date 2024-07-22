package eramo.amtalek.presentation.ui.main.broker

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
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
import eramo.amtalek.presentation.ui.main.home.details.properties.PropertyDetailsSellAndRentFragmentArgs
import eramo.amtalek.presentation.ui.main.home.details.properties.PropertyDetailsSellFragmentArgs
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.enum.PropertyType
import eramo.amtalek.util.navOptionsAnimation
import eramo.amtalek.util.showToast
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BrokersDetailsFragment : BindingFragment<FragmentBrokerDetailsBinding>(),
    RvBrokerDetailsPropertiesAdapter.OnItemClickListener {

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

    private fun setupListeners() {
        binding.apply {

            btnProjects.setOnClickListener {

                findNavController().navigate(R.id.completedProjectsFragment, bundleOf("id" to 1))
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
    private fun assignData(data: Data) {
        binding.apply {
            tvAllProjectsCount.text = "${data.property_for_sale?.plus(data.property_for_rent ?: 0)} properties"
            tvTitle.text = data.name
            tvDescription.text = data.description
            tvLocation.text = data.phone
            tvProjectsCount.text = "${data.projects_count} projects"
            Glide.with(requireContext()).load(data.logo).into(ivBrokerLogo)
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
        //findNavController().navigate(R.id.propertyDetailsFragment, bundleOf("id" to model.id))

        when (model.forWhat) {
            PropertyType.FOR_SELL.key -> {
                findNavController().navigate(
                    R.id.propertyDetailsSellFragment,
                    PropertyDetailsSellFragmentArgs(model.id.toString()).toBundle(),
                    navOptionsAnimation()
                )
            }

            PropertyType.FOR_RENT.key -> {
//                findNavController().navigate(
//                    R.id.propertyDetailsRentFragment,
//                    PropertyDetailsRentFragmentArgs(model.id.toString()).toBundle(),
//                    navOptionsAnimation()
//                )
            }

            PropertyType.FOR_BOTH.key -> {
                findNavController().navigate(
                    R.id.propertyDetailsSellAndRentFragment,
                    PropertyDetailsSellAndRentFragmentArgs(model.id.toString()).toBundle(),
                    navOptionsAnimation()
                )
            }
        }

    }
}
