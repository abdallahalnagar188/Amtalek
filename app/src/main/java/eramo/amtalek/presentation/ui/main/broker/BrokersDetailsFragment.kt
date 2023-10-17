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
import eramo.amtalek.databinding.FragmentBrokerDetailsBinding
import eramo.amtalek.presentation.adapters.recyclerview.DummyPropertyAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.presentation.viewmodel.navbottom.CartViewModel
import eramo.amtalek.util.Dummy
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.showToast
import javax.inject.Inject

@AndroidEntryPoint
class BrokersDetailsFragment : BindingFragment<FragmentBrokerDetailsBinding>() {

    override val isRefreshingEnabled: Boolean get() = false
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentBrokerDetailsBinding::inflate

    private val viewModelShared: SharedViewModel by activityViewModels()
    private val viewModel: CartViewModel by viewModels()

    @Inject
    lateinit var dummyPropertyAdapter: DummyPropertyAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.registerApiRequest { viewModel.cartData() }
        super.registerApiCancellation { viewModel.cancelRequest() }

        StatusBarUtil.whiteWithBackground(requireActivity(), R.color.amtalek_blue_dark)
        setupToolbar()

        binding.apply {
            dummyPropertyAdapter.submitList(Dummy.list())
            rvProperties.adapter = dummyPropertyAdapter

            tvSendMessage.setOnClickListener { showToast(getString(R.string.success)) }
        }
    }

    private fun setupToolbar() {
        binding.inToolbar.apply {
            toolbarIvMenu.setOnClickListener {
                viewModelShared.openDrawer.value = true
            }
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

}