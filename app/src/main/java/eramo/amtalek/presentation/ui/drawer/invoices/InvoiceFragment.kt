package eramo.amtalek.presentation.ui.drawer.invoices

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentInvoiceBinding
import eramo.amtalek.databinding.FragmentMessagingChatBinding
import eramo.amtalek.databinding.FragmentMyInvoicesBinding
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.viewmodel.social.MyProfileViewModel

@AndroidEntryPoint
class InvoiceFragment : BindingFragment<FragmentInvoiceBinding>(){

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentInvoiceBinding::inflate

    private val viewModel: MyProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_invoice, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}