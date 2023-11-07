package eramo.amtalek.presentation.ui.main.extension

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentMyProjectDetailsBinding
import eramo.amtalek.presentation.ui.BindingFragment

@AndroidEntryPoint
class MyProjectDetailsFragment : BindingFragment<FragmentMyProjectDetailsBinding>() {

    override val isRefreshingEnabled: Boolean get() = false
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentMyProjectDetailsBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    private fun setupViews(){

    }
}