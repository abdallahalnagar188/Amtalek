package eramo.amtalek.presentation.ui.drawer.subscription.packages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentMyInvoicesBinding
import eramo.amtalek.presentation.ui.BindingFragment

@AndroidEntryPoint
class MyInvoicesFragment : BindingFragment<FragmentMyInvoicesBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentMyInvoicesBinding::inflate

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_invoices, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}