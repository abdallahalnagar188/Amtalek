package eramo.amtalek.presentation.adapters.viewpager

import android.content.Context
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import dagger.hilt.android.qualifiers.ApplicationContext
import eramo.amtalek.R
import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.splash.SplashResponse
import eramo.amtalek.data.remote.dto.splash.Trace
import eramo.amtalek.data.remote.dto.splash.splashV2.OnBordingResponse
import eramo.amtalek.domain.model.auth.OnBoardingModel
import eramo.amtalek.util.LocalUtil
import javax.inject.Inject

class OnBoardingAdapter @Inject constructor(
    @ApplicationContext private val context: Context
) : PagerAdapter() {

    private var screensList = emptyList<OnBordingResponse.Data?>()

    fun setScreens(list: List<OnBordingResponse.Data?>) {
        this.screensList = list
        notifyDataSetChanged()
    }

    override fun getCount(): Int = screensList.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = layoutInflater.inflate(R.layout.layout_onboarding_sliding, container, false)

        // Reference views
        val imageView = view.findViewById<ImageView>(R.id.slider_img)
        val tvHeading = view.findViewById<TextView>(R.id.heading)
        val tvBody = view.findViewById<TextView>(R.id.tv_body)

        // Get current screen data
        val currentScreen = screensList[position]

        // Load image with Glide
        Glide.with(context)
            .load(currentScreen?.logo)
            .into(imageView)

        // Set text content for heading
      //  tvHeading.text = currentScreen?.heading ?: ""

        // Convert HTML content to a string and set it in tvBody
        val htmlContent = currentScreen?.text ?: ""
        val spannedText = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(htmlContent, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(htmlContent)
        }
        tvBody.text = spannedText

        // Handle RTL layout if needed
        if (!LocalUtil.isEnglish()) {
            tvBody.rotationY = 180f
        } else {
            tvBody.rotationY = 0f
        }

        // Add the view to the container
        container.addView(view)
        return view
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
