package eramo.amtalek.presentation.ui.main.extension

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.databinding.FragmentPropertyDetailsSellBinding
import eramo.amtalek.presentation.ui.BindingFragment

@AndroidEntryPoint
class PropertyDetailsSellFragment : BindingFragment<FragmentPropertyDetailsSellBinding>() {

    override val isRefreshingEnabled: Boolean get() = false
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentPropertyDetailsSellBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun setupViews(){

    }
}