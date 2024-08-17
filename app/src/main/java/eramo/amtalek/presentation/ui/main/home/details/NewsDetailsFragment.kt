package eramo.amtalek.presentation.ui.main.home.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentNewsDetailsBinding
import eramo.amtalek.domain.model.home.news.NewsModel
import eramo.amtalek.domain.model.social.RatingCommentsModel
import eramo.amtalek.presentation.adapters.recyclerview.RvNewsDetailsCommentsAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.main.home.HomeMyViewModel
import eramo.amtalek.util.Dummy
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.showToast
import kotlinx.coroutines.launch
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import javax.inject.Inject

@AndroidEntryPoint
class NewsDetailsFragment : BindingFragment<FragmentNewsDetailsBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentNewsDetailsBinding::inflate
    val args: NewsDetailsFragmentArgs by navArgs()
    val news get() = args.news
    @Inject
    lateinit var rvNewsDetailsCommentsAdapter: RvNewsDetailsCommentsAdapter

    val viewModel: HomeMyViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()

    }

    override fun onPause() {
        super.onPause()
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)
    }

    private fun setupViews() {
        setupToolbar()
        val image = news.image
        Glide.with(requireContext()).load(image).into(binding.ivNewsImage)
        binding.tvTitle.text = news.title
        binding.tvBody.text = news.description
        binding.tvCategory.text = news.newsCategory.mainTitle
//        setupImageSliderTop()
//        initRvComments(Dummy.dummyRatingCommentsList())
    }

    private fun setupToolbar() {
        StatusBarUtil.transparent()
        binding.apply {
            ivShare.visibility = View.GONE
            ivShare.setOnClickListener { showToast("share") }
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun setupImageSliderTop(data: List<CarouselItem>) {
        binding.apply {
//            imageSlider.registerLifecycle(viewLifecycleOwner.lifecycle)
//            imageSlider.setIndicator(imageSliderDots)
//            imageSlider.setData(data)
        }
    }

//    private fun initRvComments(data: List<RatingCommentsModel>) {
//        binding.rv.adapter = rvNewsDetailsCommentsAdapter
//        rvNewsDetailsCommentsAdapter.submitList(data)
//    }
}