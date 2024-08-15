package eramo.amtalek.presentation.ui.auth

import android.os.Bundle
import android.util.Log
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
import eramo.amtalek.data.remote.dto.splash.splashV2.OnBordingResponse
import eramo.amtalek.databinding.FragmentOnBoardingBinding
import eramo.amtalek.presentation.adapters.viewpager.OnBoardingAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
import eramo.amtalek.presentation.viewmodel.auth.OnBoardingViewModel
import eramo.amtalek.util.Dummy
import eramo.amtalek.util.LocalUtil
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.showToast
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.flow.collectLatest
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

        // Fetch onboarding data
        viewModel.getOnBoarding()
        observeOnBoardingState()

        binding.apply {
            // Set up button listeners
            onBoardingBtnNext.setOnClickListener {
                if (binding.slider.currentItem < (slideAdapter.count - 1)) {
                    binding.slider.currentItem += 1
                }
            }

            onBoardingBtnStartNow.setOnClickListener { navigateToMain() }

            // Set up indicator view
            indicatorView.apply {
                setSliderWidth((resources.displayMetrics.widthPixels.toFloat() / slideAdapter.count) - 40f)
                setSliderHeight(resources.getDimension(com.intuit.ssp.R.dimen._2ssp))
                setSlideMode(IndicatorSlideMode.WORM)
                setIndicatorStyle(IndicatorStyle.ROUND_RECT)
                setupWithViewPager(binding.slider)
            }
        }

        // Handle RTL or LTR layout direction
        if (!LocalUtil.isEnglish()) {
            binding.slider.rotationY = 180f
            binding.indicatorView.rotationY = 180f
        } else {
            binding.slider.rotationY = 0f
            binding.indicatorView.rotationY = 0f
        }
    }

    // Observe the onboarding data state from ViewModel
    private fun observeOnBoardingState() {
        lifecycleScope.launchWhenStarted {
            viewModel.onBoardingStateStateDto.collectLatest { state ->
                when (state) {
                    is Resource.Success -> {
                        val data = state.data?.data ?: emptyList()
                        if (data.isNotEmpty()) {
                            setupSlider(data)
                        }
                    }
                    is Resource.Error -> {
                        // Handle error
                       // requireContext().showToast("Failed to load data")
                    }
                    is Resource.Loading -> {
                        // Show loading if necessary
                    }
                }
            }
        }
    }

    private fun setupSlider(list: List<OnBordingResponse.Data?>) {
        binding.slider.apply {
            slideAdapter.setScreens(list)
            adapter = slideAdapter
            addOnPageChangeListener(object : OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

                override fun onPageSelected(position: Int) {
                    val lastPageIndex = (viewModel.onBoardingStateStateDto.value.data?.data?.size ?: 1) - 1
                    if (position == lastPageIndex) {
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

    private fun navigateToMain() {
        UserUtil.saveFirstTime()
        findNavController().navigate(
            R.id.nav_main, null,
            NavOptions.Builder().setPopUpTo(R.id.nav_auth, true).build()
        )
    }
}

