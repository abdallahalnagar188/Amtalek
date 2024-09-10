package eramo.amtalek.presentation.ui.main.broker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.data.remote.dto.brokersProperties.OriginalItem
import eramo.amtalek.data.remote.dto.brokersProperties.newBrokerProps.DataX
import eramo.amtalek.databinding.FragmentSeeMorePropertiesBinding
import eramo.amtalek.presentation.adapters.recyclerview.RvAllBrokerPropertiesAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.main.home.HomeMyViewModel
import eramo.amtalek.presentation.ui.main.home.details.properties.PropertyDetailsFragmentArgs
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.enum.PropertyType
import eramo.amtalek.util.showToast
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AllBrokerPropertiesFragment : BindingFragment<FragmentSeeMorePropertiesBinding>(),
    RvAllBrokerPropertiesAdapter.OnItemClickListener, RvAllBrokerPropertiesAdapter.OnFavClickListener {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSeeMorePropertiesBinding::inflate


    val viewModel: HomeMyViewModel by viewModels()
    private val brokerViewModel: BrokersViewModel by viewModels()
    private val args: AllBrokerPropertiesFragmentArgs by navArgs()

    @Inject
    lateinit var rvPropertiesAdapter: RvAllBrokerPropertiesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        brokerViewModel.getBrokersProperties(args.id)
        Log.e("broker id", args.id.toString())
        setupRecyclerView()
        setupToolbar()
        fetchProperties()

    }


    private fun setupToolbar() {
        binding.inToolbar.apply {
            tvTitle.text = getString(R.string.all_properties)
            //           tvTitle.text = "Featured Properties in Egypt"
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun setupRecyclerView() {
        rvPropertiesAdapter.setListener(this, this)
        binding.rvProperties.adapter = rvPropertiesAdapter

    }

    private fun fetchProperties() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                brokerViewModel.getAllBrokerPropertyPagingFlow(args.id).collectLatest { pagingData ->
                    rvPropertiesAdapter.submitData(pagingData)
                    Log.e("broker properties", pagingData.toString())
                }
            }

        }
    }

    private fun fetchAddRemoveToFavState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.favState.collect() { state ->
                    when (state) {

                        is UiState.Success -> {
                            showToast(state.data?.message.toString())

                            // viewModel.getAllProperty()
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

    override fun onPropertyClick(model: DataX) {
        when (model.forWhat) {
            PropertyType.FOR_SELL.key,
            PropertyType.FOR_RENT.key,
            PropertyType.FOR_BOTH.key -> {
                findNavController().navigate(
                    R.id.propertyDetailsFragment,
                    model.listingNumber?.let { PropertyDetailsFragmentArgs(it).toBundle() }
                )
            }
        }
    }


    override fun onFavClick(model: DataX) {
        if (UserUtil.isUserLogin()){
            viewModel.addOrRemoveFav(model.id?:0)
            fetchAddRemoveToFavState()

        }else{
            findNavController().navigate(R.id.loginDialog)
        }
    }
}