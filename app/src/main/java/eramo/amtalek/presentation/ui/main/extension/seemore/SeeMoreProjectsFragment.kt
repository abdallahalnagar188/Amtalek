package eramo.amtalek.presentation.ui.main.extension.seemore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.databinding.FragmentSeeMoreProjectsBinding
import eramo.amtalek.presentation.ui.BindingFragment

@AndroidEntryPoint
class SeeMoreProjectsFragment : BindingFragment<FragmentSeeMoreProjectsBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSeeMoreProjectsBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}