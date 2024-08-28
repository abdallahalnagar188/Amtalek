package eramo.amtalek.presentation.ui.drawer.messaging.addons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentLottieAnimationBinding
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.util.navOptionsAnimation
import eramo.amtalek.util.onBackPressed
import kotlinx.coroutines.delay

@AndroidEntryPoint
class LottieAnimationFragment: BindingFragment<FragmentLottieAnimationBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentLottieAnimationBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenStarted {
            delay(2000L)
           //findNavController().popBackStack(findNavController().graph.startDestinationId, false)
            findNavController().navigate(R.id.homeFragment, null, navOptionsAnimation())
        }
        this@LottieAnimationFragment.onBackPressed { }

    }

}