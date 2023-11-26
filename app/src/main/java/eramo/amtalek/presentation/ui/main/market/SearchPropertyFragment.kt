package eramo.amtalek.presentation.ui.main.market

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentSearchPropertyBinding
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.presentation.viewmodel.navbottom.extension.NotificationViewModel
import eramo.amtalek.util.StatusBarUtil

@AndroidEntryPoint
class SearchPropertyFragment : BindingFragment<FragmentSearchPropertyBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSearchPropertyBinding::inflate

    private val viewModel by viewModels<NotificationViewModel>()
    private val viewModelShared by activityViewModels<SharedViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        super.registerApiRequest { viewModel.getNotification() }
//        super.registerApiCancellation { viewModel.cancelRequest() }
        StatusBarUtil.whiteWithBackground(requireActivity(), R.color.amtalek_blue_dark)
        setupToolbar()

        binding.apply {
            inSpinnerLocation.tvText.hint = getString(R.string.location)
            inSpinnerType.tvText.hint = getString(R.string.property_type)
            inSpinnerStatus.tvText.hint = getString(R.string.property_status)
            inSpinnerCurrency.tvText.hint = getString(R.string.currency)
            inSpinnerBathroom.tvText.hint = getString(R.string.min_bathrooms)
            inSpinnerBeds.tvText.hint = getString(R.string.min_beds)

            tvSearch.setOnClickListener {
                findNavController().navigate(R.id.searchPropertyResultFragment)
            }
        }
    }

    private fun setupToolbar() {
        binding.inToolbar.apply {
//            ivSearch.isVisible = false
            toolbarIvMenu.setOnClickListener { viewModelShared.openDrawer.value = true }
            inNotification.root.setOnClickListener {
                findNavController().navigate(R.id.notificationFragment)
            }
        }
    }

}