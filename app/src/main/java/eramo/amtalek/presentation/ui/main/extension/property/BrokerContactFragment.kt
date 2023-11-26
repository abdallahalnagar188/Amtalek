package eramo.amtalek.presentation.ui.main.extension.property

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentBrokerContactBinding
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.util.showToast

@AndroidEntryPoint
class BrokerContactFragment : BindingFragment<FragmentBrokerContactBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentBrokerContactBinding::inflate

    private val viewModelShared: SharedViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            tvSendMessage.setOnClickListener { showToast(getString(R.string.success)) }
            tvPresentOffer.setOnClickListener { showToast(getString(R.string.success)) }
        }
    }

}