package eramo.amtalek.presentation.ui.auth

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.util.UserUtil
import kotlinx.coroutines.delay

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        StatusBarUtil.transparent()

        lifecycleScope.launchWhenResumed {
            if (!UserUtil.hasDeepLink()) {
                logoAnimation()
                delay(2500L)
            }
//            if(!UserUtil.hasDeepLink()) delay(200L)

            val shouldNavigateToMain = UserUtil.isRememberUser() || !UserUtil.isFirstTime()
            if (shouldNavigateToMain) {
                findNavController().navigate(
                    R.id.nav_main, null,
                    NavOptions.Builder().setPopUpTo(R.id.nav_auth, true).build()
                )

            } else findNavController().navigate(
                R.id.selectCityFragment, null,
                NavOptions.Builder().setPopUpTo(R.id.splashFragment, true).build()
            )
//            findNavController().navigate(
//                R.id.onBoardingFragment, null,
//                NavOptions.Builder().setPopUpTo(R.id.splashFragment, true).build()
//            )
        }
    }

    private fun logoAnimation() {
        val view = activity?.findViewById<View>(R.id.white_view)
        val animator = ValueAnimator.ofFloat(1.0f, 0.0f)
        animator.duration = 2000
        animator.addUpdateListener { animation ->
            view?.alpha = animation.animatedValue as Float
        }

        animator.start()
    }


    override fun onDetach() {
        super.onDetach()
        UserUtil.setHasDeepLink(false)
    }
}