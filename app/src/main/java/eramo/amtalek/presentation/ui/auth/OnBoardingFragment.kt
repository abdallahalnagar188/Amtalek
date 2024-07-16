package eramo.amtalek.presentation.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.zhpan.indicator.enums.IndicatorSlideMode
import com.zhpan.indicator.enums.IndicatorStyle
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentOnBoardingBinding
import eramo.amtalek.presentation.adapters.viewpager.OnBoardingAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
import eramo.amtalek.presentation.viewmodel.auth.OnBoardingViewModel
import eramo.amtalek.util.Dummy
import eramo.amtalek.util.LocalUtil
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.showToast
import eramo.amtalek.util.state.UiState
import javax.inject.Inject

@AndroidEntryPoint
class OnBoardingFragment : BindingFragment<FragmentOnBoardingBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentOnBoardingBinding::inflate

    @Inject
    lateinit var slideAdapter: OnBoardingAdapter
    private val viewModel by viewModels<OnBoardingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        super.registerApiCancellation { viewModel.cancelRequest() }
//        StatusBarUtil.transparent()
        setupSlider()

        binding.apply {
            onBoardingBtnNext.setOnClickListener {
//                if (binding.slider.currentItem < 2)
                binding.slider.currentItem = binding.slider.currentItem + 1
//                else navigateToMain()
            }

            onBoardingBtnStartNow.setOnClickListener { navigateToMain() }
//            onBoardingBtnStartNow.setOnClickListener { findNavController().navigate(R.id.loginFragment,null, navOptionsAnimation()) }

            indicatorView.apply {
//                setSliderWidth(resources.getDimension(com.intuit.ssp.R.dimen._95ssp))
//                setSliderHeight(resources.getDimension(com.intuit.ssp.R.dimen._2ssp))
                setSliderWidth((resources.displayMetrics.widthPixels.toFloat() / (Dummy.dummyBoard(requireContext()).size.toFloat())) - 40f)
                setSliderHeight(resources.getDimension(com.intuit.ssp.R.dimen._2ssp))
                setSlideMode(IndicatorSlideMode.WORM)
                setIndicatorStyle(IndicatorStyle.ROUND_RECT)
                setupWithViewPager(binding.slider)
            }
        }
//        fetchLatestDealsState()

        if (!LocalUtil.isEnglish()){
            binding.slider.rotationY = 180f
            binding.indicatorView.rotationY = 180f
        }else{
            binding.slider.rotationY = 0f
            binding.indicatorView.rotationY = 0f
        }
    }


    private fun navigateToMain() {
        UserUtil.saveFirstTime()
        findNavController().navigate(
            R.id.nav_main, null,
            NavOptions.Builder().setPopUpTo(R.id.nav_auth, true).build()
        )
    }

    private fun setupSlider() {
        binding.slider.apply {
            slideAdapter.setScreens(Dummy.dummyBoard(requireContext()))
            adapter = slideAdapter
            addOnPageChangeListener(object : OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }

                override fun onPageSelected(position: Int) {
                    if (position == (Dummy.dummyBoard(requireContext()).size -1)) {
                        binding.onBoardingBtnNext.visibility = View.INVISIBLE
                        binding.onBoardingBtnStartNow.visibility = View.VISIBLE
                    } else {
                        binding.onBoardingBtnNext.visibility = View.VISIBLE
                        binding.onBoardingBtnStartNow.visibility = View.INVISIBLE
                    }
                }

                override fun onPageScrollStateChanged(state: Int) {}
            })
        }
    }


}
