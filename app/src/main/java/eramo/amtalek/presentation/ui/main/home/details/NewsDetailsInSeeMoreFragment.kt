package eramo.amtalek.presentation.ui.main.home.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentNewsDetailsBinding
import eramo.amtalek.databinding.FragmentNewsDetailsInSeeMoreBinding
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.showToast

class NewsDetailsInSeeMoreFragment : BindingFragment<FragmentNewsDetailsInSeeMoreBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentNewsDetailsInSeeMoreBinding::inflate

    val args: NewsDetailsInSeeMoreFragmentArgs by navArgs()
    val news get() = args.news


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        setupToolbar()
        val image = news.image
        Glide.with(requireContext()).load(image).into(binding.ivNewsImage)
        binding.tvTitle.text = news.title
        binding.tvBody.text = news.description
        binding.tvCategory.text = news.newsCategory?.mainTitle
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
}

