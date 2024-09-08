package eramo.amtalek.presentation.ui.main.home.details

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.data.remote.dto.myHome.news.NewsDetailsResponse
import eramo.amtalek.data.remote.dto.myHome.news.newsDetails.Data
import eramo.amtalek.databinding.FragmentNewsDetailsBinding
import eramo.amtalek.databinding.FragmentNewsDetailsInSeeMoreBinding
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
import eramo.amtalek.presentation.ui.main.home.HomeMyViewModel
import eramo.amtalek.presentation.ui.main.home.details.newsCategory.NewsCategoryFragmentArgs
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.navOptionsAnimation
import eramo.amtalek.util.showToast
import eramo.amtalek.util.state.Resource

@AndroidEntryPoint
class NewsDetailsInSeeMoreFragment : BindingFragment<FragmentNewsDetailsInSeeMoreBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentNewsDetailsInSeeMoreBinding::inflate

    val args: NewsDetailsInSeeMoreFragmentArgs by navArgs()

    val viewModel: HomeMyViewModel by viewModels()

    var news: Data? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getNewsDetails(args.id)
        fetchData()
        setupListener()
    }

    override fun onPause() {
        super.onPause()
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)
    }

    fun fetchData() {
        viewModel.getNewsDetails(args.id)
        lifecycleScope.launchWhenStarted {
            viewModel.newsDetails.collect {
                when (it) {

                    is Resource.Success -> {

                        it.data?.data?.first()?.let { it1 -> setupViews(it1) }
                        news = it.data?.data?.first()
                        LoadingDialog.dismissDialog()
                    }


                    is Resource.Error -> {
                        showToast(it.message.toString())
                        LoadingDialog.dismissDialog()
                    }

                    is Resource.Loading -> {
                        LoadingDialog.showDialog()
                    }

                    else -> {}
                }
            }
        }
    }

    private fun setupViews(news: Data) {
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
        binding.tvCategory.text = news.categoryDetails?.name
        binding.apply {
            tvCategory.setOnClickListener {
                findNavController().navigate(
                    R.id.newsCategoryFragment,
                    NewsCategoryFragmentArgs(
                        categoryId = news.categoryDetails?.id.toString(),
                        titleName = news.categoryDetails?.name.toString()
                    ).toBundle(), navOptionsAnimation()
                )
            }
        }
//        setupImageSliderTop()
//        initRvComments(Dummy.dummyRatingCommentsList())
    }

    private fun setupListener() {

    }

    private fun shareContent() {
        // The content you want to share
        val htmlContent = news?.description ?: ""
        val spannedText = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(htmlContent, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(htmlContent)
        }
        val shareText = news?.title + " " + spannedText

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

