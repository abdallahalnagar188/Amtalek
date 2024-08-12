package eramo.amtalek.presentation.ui.drawer.messaging.addons

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentAddAdomsBinding
import eramo.amtalek.databinding.FragmentBuyAddonsBinding
import eramo.amtalek.presentation.ui.BindingFragment

@AndroidEntryPoint
class BuyAddonsFragment : BindingFragment<FragmentBuyAddonsBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentBuyAddonsBinding::inflate


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolBar()
        setupViews()
    }

    private fun setupToolBar() {
        binding.inToolbar.apply {
            tvTitle.text = getString(R.string.payment)
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
    private fun setupViews(){
    }
}