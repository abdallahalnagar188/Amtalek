package eramo.amtalek.presentation.ui.main.home.details

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentNewsDetailsBinding
import eramo.amtalek.databinding.FragmentNewsDetailsInSeeMoreBinding
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.main.home.details.newsCategory.NewsCategoryFragmentArgs
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.navOptionsAnimation
import eramo.amtalek.util.showToast

@AndroidEntryPoint
class NewsDetailsInSeeMoreFragment : BindingFragment<FragmentNewsDetailsInSeeMoreBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentNewsDetailsInSeeMoreBinding::inflate

    val args: NewsDetailsInSeeMoreFragmentArgs by navArgs()
    val news get() = args.news


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupListener()
    }
    override fun onPause() {
        super.onPause()
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)
    }

    private fun setupViews() {
        setupToolbar()
        val htmlContent = news?.description ?: ""
        val spannedText = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(htmlContent, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(htmlContent)
        }
        val image = news.image
        Glide.with(requireContext()).load(image).into(binding.ivNewsImage)
        binding.tvTitle.text = news.title
        binding.tvBody.text = spannedText
        binding.tvCategory.text = news.newsCategory?.mainTitle

//        setupImageSliderTop()
//        initRvComments(Dummy.dummyRatingCommentsList())
    }

    private fun setupListener() {
        binding.apply {
            tvCategory.setOnClickListener {
                findNavController().navigate(
                    R.id.newsCategoryFragment,
                    NewsCategoryFragmentArgs(
                        categoryId = news.newsCategory?.id.toString(),
                        titleName = news.newsCategory?.mainTitle.toString()
                    ).toBundle(), navOptionsAnimation()
                )
            }
        }
    }

    private fun shareContent() {
        // The content you want to share
        val htmlContent = news?.description ?: ""
        val spannedText = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(htmlContent, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(htmlContent)
        }
        val shareText = news.title + " " + spannedText

        // Create the share intent
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"// MIME type for sharing text
        }

        // Start the share intent
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

    private fun setupToolbar() {
        StatusBarUtil.transparent()
        binding.apply {
            ivShare.setOnClickListener { shareContent() }
            // ivShare.setOnClickListener { showToast("share") }
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }
}

