package eramo.amtalek.presentation.ui.drawer.messaging

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.databinding.FragmentMessagingOfferBinding
import eramo.amtalek.presentation.ui.BindingFragment

@AndroidEntryPoint
class MessagingOfferFragment : BindingFragment<FragmentMessagingOfferBinding>() {

    override val isRefreshingEnabled: Boolean get() = false
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentMessagingOfferBinding::inflate
}