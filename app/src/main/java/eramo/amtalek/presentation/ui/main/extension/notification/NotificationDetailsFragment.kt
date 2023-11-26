package eramo.amtalek.presentation.ui.main.extension.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentNotificationDetailsBinding
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.openLinkInBrowser

@AndroidEntryPoint
class NotificationDetailsFragment : BindingFragment<FragmentNotificationDetailsBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentNotificationDetailsBinding::inflate

//    private val args by navArgs<NotificationInfoFragmentArgs>()
//    private val notificationDto get() = args.notificationDto

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        listeners()
    }

    private fun setupViews() {
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)

        initViewsData()
    }

    private fun listeners() {
        binding.apply {
            ivImage.setOnClickListener { this@NotificationDetailsFragment.openLinkInBrowser("https://www.google.com/") }
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun initViewsData() {
        binding.apply {
            tvTitle.text = "Real Estate Development"
            tvBody.text =
                "Lörem ipsum cirkulär handel visual search, till öppen innovation, respektive Anna Ekström Viktoria Nyberg. Endoitet Anette Eliasson och Göran Jonsson Annika Nyström av tiliga. Soft landing Mats Ekström Adam Henriksson Lisa Forsberg Kjell Åkesson, metataggar fasamma Roger Gustafsson. Ebba Holmberg kropona. Growth hacking facilitera Henrik Carlsson, i Elias Nordin Annika Mårtensson egosade."

            Glide.with(requireContext())
                .load("https://www.elmostaqbal.com/wp-content/uploads/2022/02/www.elmostaqbal.com_2022-02-24_11-44-53_037410.jpg")
                .into(ivImage)
        }
    }
}