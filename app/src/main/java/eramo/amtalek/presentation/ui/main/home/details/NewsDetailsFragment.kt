package eramo.amtalek.presentation.ui.main.home.details

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
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
import eramo.amtalek.data.remote.dto.myHome.news.newsDetails.Data
import eramo.amtalek.databinding.FragmentNewsDetailsBinding
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
import eramo.amtalek.presentation.ui.main.home.HomeMyViewModel
import eramo.amtalek.presentation.ui.main.home.details.newsCategory.NewsCategoryFragmentArgs
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.navOptionsAnimation
import eramo.amtalek.util.showToast
import eramo.amtalek.util.state.Resource

@AndroidEntryPoint
class NewsDetailsFragment : BindingFragment<FragmentNewsDetailsBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentNewsDetailsBinding::inflate

    private val args: NewsDetailsFragmentArgs by navArgs()
    private val viewModel: HomeMyViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getNewsDetails(args.id)
        Log.e("id news" , args.id)
        Log.e("data for news" , viewModel.newsDetails.value.toString())
        // Call the functions to set up UI
        setupToolbar()
        setupListener()

        observeData()
    }

    override fun onPause() {
        super.onPause()
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)
    }

    private fun observeData() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.newsDetails.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        // Show loading indicator if needed
                        LoadingDialog.showDialog()
                    }
                    is Resource.Success -> {
                        Log.e("TAG", "observeData: ${resource.data?.data}")
                        resource.data?.data?.let { assignData(it.first()) }
                        LoadingDialog.dismissDialog()
                    }
                    is Resource.Error -> {
                        // Handle error
                        showToast(resource.message.toString())
                        LoadingDialog.dismissDialog()
                    }
                }
            }
        }

       // Fetch data based on the ID
    }

    private fun assignData(news: Data) {
        binding.apply {
            // Set data to UI elements
            tvTitle.text = news.title
            tvCategory.text = news.categoryDetails?.name

            val htmlContent = news?.description ?: ""
            val spannedText = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(htmlContent, Html.FROM_HTML_MODE_COMPACT)
            } else {
                Html.fromHtml(htmlContent)
            }
                tvBody.text = spannedText

            // Load image using Glide
            Glide.with(this@NewsDetailsFragment)
                .load(news.image)
                .into(ivNewsImage)

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
            // Setup image slider with the news carousel items
        }
    }

    private fun setupListener() {
        binding.apply {
            ivShare.setOnClickListener {
                shareContent()
            }
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun setupToolbar() {
        StatusBarUtil.transparent()
    }

    private fun shareContent() {
        val htmlContent = binding.tvBody.text.toString()
        val spannedText = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(htmlContent, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(htmlContent)
        }
        // The content you want to share
        val shareText = "${binding.tvTitle.text} $spannedText"

        // Create the share intent
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain" // MIME type for sharing text
        }

        // Start the share intent
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

}
