package eramo.amtalek.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import eramo.amtalek.R

//object Constants {

    const val API_HEADER_LANG_EN = "en"
    const val API_HEADER_LANG_AR = "ar"

    const val API_SUCCESS_CODE = 1
    const val API_FAILURE_CODE = 0

    const val API_SUSPEND_CODE = "1"

    const val API_OPERATION_TYPE_VERIFY_CODE = "verify_code"
    const val API_OPERATION_TYPE_FORGET_PASSWORD = "forget_password"

    const val SIGN_UP_GENDER_MALE = "Male"
    const val SIGN_UP_GENDER_FEMALE = "Female"

    const val SIGN_UP_TYPE_INDIVIDUAL = "individual"
    const val SIGN_UP_TYPE_COMPANY = "company"

    const val SIGN_UP_GENDER_ACCEPT_CONDITION = "yes"
    const val SIGN_UP_GENDER_ACCEPT_NOT_ROBOT = "yes"

    const val UPLOAD_PROFILE_IMAGE_KEY = "image"
    const val UPLOAD_COVER_IMAGE_KEY = "cover"

    const val FROM_ANDROID = "Android"
    const val NOT_ROBOT = "yes"

    const val TOPIC = "/topics/MyTopic"

    const val TEXT_YES = "yes"
    const val TEXT_NO = "no"

    const val ANIMATION_DELAY = 450L
    const val TAG = "amtalek"

    const val ROLLING_TEXT_ANIMATION_DURATION = 800L

    const val PAGING_START_INDEX = 1
    const val PAGING_PER_PAGE = 10

    const val ERAMO_WEBSITE = "https://www.e-ramo.net"
    const val ERAMO_PHONE = "tel:+201011559674"

    const val TRUE = 1
    const val FALSE = 0

    const val NONE_IMAGE_URL="https://static.thenounproject.com/png/3349099-200.png"

//    const val ADVERTISEMENT = 1001
//    const val TEXT = 1002
//    const val PHOTOS = 1003
//    const val TEXT_AND_PHOTOS = 1004

    fun createSpinnerAdapter(context: Context, list: List<StringWithTag>) =
        ArrayAdapter(context, R.layout.layout_spinner_item, list)

    fun spinnerAdapter(context: Context, list: List<String>) =
        ArrayAdapter(context, R.layout.layout_spinner_item, list)

    fun setupLangChooser(
        activity: Activity,
        flagIcon: ImageView,
        header: CardView,
        body: CardView,
        iconArrow: ImageView,
        iconCheckEn: ImageView,
        iconCheckAr: ImageView,
        linChoiceEn: LinearLayout,
        linChoiceAr: LinearLayout,
    ) {
        var isLangOpened = false
        if (LocalUtil.isEnglish()) {
            iconCheckEn.visibility = View.VISIBLE
            iconCheckAr.visibility = View.INVISIBLE
            flagIcon.setImageResource(R.drawable.pic_en)
        } else {
            iconCheckEn.visibility = View.INVISIBLE
            iconCheckAr.visibility = View.VISIBLE
            flagIcon.setImageResource(R.drawable.pic_egypt)
        }

        header.setOnClickListener {
            if (isLangOpened) closeChooser(body, iconArrow) else openChooser(body, iconArrow)
            isLangOpened = !isLangOpened
        }

        linChoiceEn.setOnClickListener {
            closeChooser(body, iconArrow)
            LocalUtil.setLocal(activity, "en")
            ActivityCompat.recreate(activity)
        }

        linChoiceAr.setOnClickListener {
            closeChooser(body, iconArrow)
            LocalUtil.setLocal(activity, "ar")
            ActivityCompat.recreate(activity)
        }
    }

    private fun closeChooser(body: CardView, iconArrow: ImageView) {
        body.visibility = View.GONE
        iconArrow.setImageResource(R.drawable.ic_arrow_down)
    }

    private fun openChooser(body: CardView, iconArrow: ImageView) {
        body.visibility = View.VISIBLE
        iconArrow.setImageResource(R.drawable.ic_arrow_up)
    }
//}