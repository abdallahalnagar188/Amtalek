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
import eramo.amtalek.domain.model.extentions.NotificationsModel
import eramo.amtalek.presentation.adapters.recyclerview.RvNotificationsAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.util.Dummy
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.navOptionsAnimation
import javax.inject.Inject

@AndroidEntryPoint
class NotificationFragment : BindingFragment<FragmentNotificationBinding>(),
    RvNotificationsAdapter.OnItemClickListener {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentNotificationBinding::inflate

    @Inject
    lateinit var rvNotificationsAdapter: RvNotificationsAdapter

    private val viewModelShared by activityViewModels<SharedViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()

    }

    private fun setupViews() {
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)

        setupScreenHeader()

        initNotificationsRv(Dummy.dummyNotificationsList())
    }

    private fun setupScreenHeader() {
        binding.inToolbar.apply {
            tvTitle.text = getString(R.string.s_Notifications, Dummy.dummyNotificationsList().size.toString())
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun initNotificationsRv(data: List<NotificationsModel>) {
        rvNotificationsAdapter.setListener(this@NotificationFragment)
        binding.rvNotification.adapter = rvNotificationsAdapter
        rvNotificationsAdapter.submitList(data)

    }

    override fun onNotificationClick(model: NotificationsModel) {
        findNavController().navigate(R.id.notificationDetailsFragment, null, navOptionsAnimation())
    }

}