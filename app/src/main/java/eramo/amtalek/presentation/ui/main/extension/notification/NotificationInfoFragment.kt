package eramo.amtalek.presentation.ui.main.extension.notification

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentNotificationInfoBinding
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.util.StatusBarUtil

@AndroidEntryPoint
class NotificationInfoFragment : BindingFragment<FragmentNotificationInfoBinding>() {

    override val isRefreshingEnabled: Boolean get() = false
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentNotificationInfoBinding::inflate

//    private val args by navArgs<NotificationInfoFragmentArgs>()
//    private val notificationDto get() = args.notificationDto

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        StatusBarUtil.whiteWithBackground(requireActivity(), R.color.amtalek_blue_dark)
        setupScreenHeader()

        binding.apply {
            FNotificationInfoLink.setOnClickListener {
                val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/"))
                startActivity(i)
            }
        }
    }

    private fun setupScreenHeader() {
        binding.inHeaderScreen.apply {
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

}