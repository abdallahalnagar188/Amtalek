package eramo.amtalek.presentation.ui.main.extension.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentNotificationBinding
import eramo.amtalek.presentation.adapters.recyclerview.DummyNotificationAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.util.Dummy
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.navOptionsAnimation
import javax.inject.Inject

@AndroidEntryPoint
class NotificationFragment : BindingFragment<FragmentNotificationBinding>(),
    DummyNotificationAdapter.OnItemClickListener {

    override val isRefreshingEnabled: Boolean get() = false
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentNotificationBinding::inflate

    @Inject
    lateinit var dummyNotificationAdapter: DummyNotificationAdapter

    private val viewModelShared by activityViewModels<SharedViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        StatusBarUtil.whiteWithBackground(requireActivity(), R.color.amtalek_blue_dark)
        setupScreenHeader()

        dummyNotificationAdapter.setListener(this)
        dummyNotificationAdapter.submitList(Dummy.list())
        binding.FFavouriteRvNotification.adapter = dummyNotificationAdapter
    }

    private fun setupScreenHeader() {
        binding.inHeaderScreen.apply {
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    override fun onNotificationClick(model: String) {
        findNavController().navigate(R.id.notificationInfoFragment, null, navOptionsAnimation())
    }

}