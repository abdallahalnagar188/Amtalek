package eramo.amtalek.presentation.ui.drawer.messaging

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.databinding.FragmentUsersChatBinding
import eramo.amtalek.presentation.ui.BindingFragment

@AndroidEntryPoint
class UsersChatFragment : BindingFragment<FragmentUsersChatBinding>() {

    override val isRefreshingEnabled: Boolean get() = false
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentUsersChatBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}