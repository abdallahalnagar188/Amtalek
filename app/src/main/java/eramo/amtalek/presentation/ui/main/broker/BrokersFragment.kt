package eramo.amtalek.presentation.ui.main.broker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentBrokersBinding
import eramo.amtalek.presentation.adapters.dummy.DummyBrokerAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.presentation.viewmodel.navbottom.CartViewModel
import eramo.amtalek.util.Dummy
import eramo.amtalek.util.StatusBarUtil
import javax.inject.Inject

@AndroidEntryPoint
class BrokersFragment : BindingFragment<FragmentBrokersBinding>(),
    DummyBrokerAdapter.OnItemClickListener {

    override val isRefreshingEnabled: Boolean get() = false
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentBrokersBinding::inflate

    private val viewModelShared: SharedViewModel by activityViewModels()
    private val viewModel: CartViewModel by viewModels()

    @Inject
    lateinit var dummyBrokerAdapter: DummyBrokerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.registerApiRequest { viewModel.cartData() }
        super.registerApiCancellation { viewModel.cancelRequest() }

        StatusBarUtil.whiteWithBackground(requireActivity(), R.color.amtalek_blue_dark)
        setupToolbar()

        dummyBrokerAdapter.setListener(this)
        binding.apply {
            dummyBrokerAdapter.submitList(Dummy.list())
            rvBrokers.adapter = dummyBrokerAdapter
        }
    }

    private fun setupToolbar() {
        binding.inToolbar.apply {
            toolbarIvMenu.setOnClickListener { viewModelShared.openDrawer.value = true }
//            ivSearch.setOnClickListener { findNavController().navigate(R.id.searchPropertyFragment) }
            inNotification.root.setOnClickListener {
                findNavController().navigate(R.id.notificationFragment)
            }
        }
    }

    override fun onProductClick(model: String) {
        findNavController().navigate(R.id.brokersDetailsFragment)
    }

}