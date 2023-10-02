package eramo.amtalek.presentation.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
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
        StatusBarUtil.transparent()
        addDots(0)
        setupSlider()

        binding.apply {
            onBoardingBtnNext.setOnClickListener {
                if (binding.slider.currentItem < 2)
                    binding.slider.currentItem = binding.slider.currentItem + 1
                else navigateToMain()
            }

            onBoardingTvSkip.setOnClickListener { navigateToMain() }
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
                    addDots(position)
                    if (position == 2) {
                        binding.onBoardingBtnNext.text = getString(R.string.txt_get_start)
                        binding.onBoardingTvSkip.visibility = View.INVISIBLE
                    } else {
                        binding.onBoardingBtnNext.text = getString(R.string.next)
                        binding.onBoardingTvSkip.visibility = View.VISIBLE
                    }
                }

                override fun onPageScrollStateChanged(state: Int) {}
            })
        }
    }

    private fun addDots(currentPosition: Int) {
        var lp: ViewGroup.LayoutParams
        binding.apply {
            dotZero.apply {
                setImageResource(R.drawable.shape_red_low)
                lp = this.layoutParams
                lp.height = 14
                lp.width = 14
                this.layoutParams = lp
            }
            dotOne.apply {
                setImageResource(R.drawable.shape_red_low)
                lp = this.layoutParams
                lp.height = 14
                lp.width = 14
                this.layoutParams = lp
            }
            dotTwo.apply {
                setImageResource(R.drawable.shape_red_low)
                lp = this.layoutParams
                lp.height = 14
                lp.width = 14
                this.layoutParams = lp
            }

            when (currentPosition) {
                0 -> dotZero.apply {
                    setImageResource(R.drawable.shape_orange)
                    lp = this.layoutParams
                    lp.height = 14
                    lp.width = 28
                    this.layoutParams = lp
                }
                1 -> dotOne.apply {
                    setImageResource(R.drawable.shape_orange)
                    lp = this.layoutParams
                    lp.height = 14
                    lp.width = 28
                    this.layoutParams = lp
                }
                2 -> dotTwo.apply {
                    setImageResource(R.drawable.shape_orange)
                    lp = this.layoutParams
                    lp.height = 14
                    lp.width = 28
                    this.layoutParams = lp
                }
            }
        }
    }
}
