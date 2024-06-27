package eramo.amtalek.presentation.ui.auth

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentSplashBinding
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.util.LocalUtil
import eramo.amtalek.util.UserUtil
import kotlinx.coroutines.delay

@AndroidEntryPoint
class SplashFragment :BindingFragment<FragmentSplashBinding>() {
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSplashBinding::inflate
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        StatusBarUtil.transparent()
        if(LocalUtil.isEnglish()){
            binding.splashImage.setImageResource(R.drawable.splash_en)
        }else{
            binding.splashImage.setImageResource(R.drawable.splash_ar)
        }

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