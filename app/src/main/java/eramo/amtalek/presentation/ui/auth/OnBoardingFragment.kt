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
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.showToast
import eramo.amtalek.util.state.UiState
import javax.inject.Inject

@AndroidEntryPoint
class OnBoardingFragment : BindingFragment<FragmentOnBoardingBinding>() {

    override val isRefreshingEnabled: Boolean get() = false
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentOnBoardingBinding::inflate

    @Inject
    lateinit var slideAdapter: OnBoardingAdapter
    private val viewModel by viewModels<OnBoardingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        super.registerApiCancellation { viewModel.cancelRequest() }
//        StatusBarUtil.transparent()
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)
        setupSlider()

        binding.apply {
            onBoardingBtnNext.setOnClickListener {
                if (binding.slider.currentItem < 2)
                    binding.slider.currentItem = binding.slider.currentItem + 1
                else navigateToMain()
            }

//            onBoardingTvSkip.setOnClickListener { navigateToMain() }

            indicatorView.apply {
//                setSliderWidth(resources.getDimension(com.intuit.ssp.R.dimen._95ssp))
//                setSliderHeight(resources.getDimension(com.intuit.ssp.R.dimen._2ssp))
                setSliderWidth((resources.displayMetrics.widthPixels.toFloat() / 3f) - 40f)
                setSliderHeight(resources.getDimension(com.intuit.ssp.R.dimen._2ssp))
                setSlideMode(IndicatorSlideMode.WORM)
                setIndicatorStyle(IndicatorStyle.ROUND_RECT)
                setupWithViewPager(binding.slider)
            }
        }
//        fetchLatestDealsState()
    }

    override fun onStart() {
        super.onStart()
        viewModel.getOnBoarding()
    }

    private fun fetchLatestDealsState() {
        lifecycleScope.launchWhenStarted {
            viewModel.onBoardingStateStateDto.collect { state ->
                when (state) {
                    is UiState.Success -> {
                        LoadingDialog.dismissDialog()
                        slideAdapter.setScreens(state.data ?: emptyList())
                    }

                    is UiState.Error -> {
                        LoadingDialog.dismissDialog()
                        showToast(state.message!!.asString(requireContext()))
                    }

                    is UiState.Loading -> {
                        LoadingDialog.showDialog()
                    }

                    else -> Unit
                }
            }
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
            slideAdapter.setScreens(Dummy.dummyBoard())
            adapter = slideAdapter
            addOnPageChangeListener(object : OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }

                override fun onPageSelected(position: Int) {
                    if (position == 2) {
                        binding.onBoardingBtnNext.visibility = View.VISIBLE
                    } else {
//                        binding.onBoardingBtnNext.visibility = View.INVISIBLE
                    }
                }

                override fun onPageScrollStateChanged(state: Int) {}
            })
        }
    }


}
