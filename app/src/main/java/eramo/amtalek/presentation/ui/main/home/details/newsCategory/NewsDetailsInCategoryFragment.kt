package eramo.amtalek.presentation.ui.main.home.details.newsCategory

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
import eramo.amtalek.data.remote.dto.myHome.news.newscateg.NewsData
import eramo.amtalek.databinding.FragmentNewsCategoryBinding
import eramo.amtalek.databinding.FragmentNewsDetailsInCategoryBinding
import eramo.amtalek.domain.model.home.news.NewsModel
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.main.home.details.NewsDetailsFragmentArgs
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.navOptionsAnimation

@AndroidEntryPoint
class NewsDetailsInCategoryFragment : BindingFragment<FragmentNewsDetailsInCategoryBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentNewsDetailsInCategoryBinding::inflate

    val args: NewsDetailsInCategoryFragmentArgs by navArgs()
    val news get() = args.newsCat


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupListener(news)

    }


    override fun onPause() {
        super.onPause()
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)
    }

    fun setupViews() {
        setupToolbar()
        val image = news.image
        Glide.with(requireContext()).load(image).into(binding.ivNewsImage)
        binding.tvTitle.text = news.title
        binding.tvBody.text = news.description
        binding.tvCategory.text = news.newsCategory?.mainTitle
        val htmlContent = news?.description ?: ""
        val spannedText = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(htmlContent, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(htmlContent)
        }
        binding.tvBody.text = spannedText

//        setupImageSliderTop()
//        initRvComments(Dummy.dummyRatingCommentsList())
    }

    fun setupListener(model: NewsData) {
        binding.apply {
            tvCategory.setOnClickListener {
                findNavController().navigate(
                    R.id.newsCategoryFragment,
                    NewsCategoryFragmentArgs(
                        categoryId = model.newsCategory?.id.toString() ?: "",
                        titleName = model.newsCategory?.mainTitle ?: ""
                    ).toBundle(), navOptionsAnimation()
                )

            }
        }
    }

    private fun setupToolbar() {
        StatusBarUtil.transparent()
        binding.apply {
            ivShare.setOnClickListener {
                shareContent()
            }

            //  ivShare.setOnClickListener { showToast("share") }
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun shareContent() {
        val htmlContent = news?.description ?: ""
        val spannedText = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(htmlContent, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(htmlContent)
        }
        // The content you want to share
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

}