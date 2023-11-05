package eramo.amtalek.presentation.ui.main.extension

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.databinding.FragmentNewsDetailsBinding
import eramo.amtalek.domain.model.social.RatingCommentsModel
import eramo.amtalek.presentation.adapters.recyclerview.RvNewsDetailsCommentsAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.util.Dummy
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.showToast
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import javax.inject.Inject

@AndroidEntryPoint
class NewsDetailsFragment : BindingFragment<FragmentNewsDetailsBinding>() {

    override val isRefreshingEnabled: Boolean get() = false
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentNewsDetailsBinding::inflate

    @Inject
    lateinit var rvNewsDetailsCommentsAdapter: RvNewsDetailsCommentsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        setupToolbar()

        setupImageSliderTop(Dummy.dummyCarouselImagesList())
        initRvComments(Dummy.dummyRatingCommentsList())
    }

    private fun setupToolbar() {
        StatusBarUtil.transparent()
        binding.apply {
            ivShare.setOnClickListener { showToast("share") }
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun setupImageSliderTop(data: List<CarouselItem>) {
        binding.apply {
            imageSlider.registerLifecycle(viewLifecycleOwner.lifecycle)
            imageSlider.setIndicator(imageSliderDots)
            imageSlider.setData(data)
        }
    }

    private fun initRvComments(data: List<RatingCommentsModel>) {
        binding.rv.adapter = rvNewsDetailsCommentsAdapter
        rvNewsDetailsCommentsAdapter.submitList(data)
    }
}