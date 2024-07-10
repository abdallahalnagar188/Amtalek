package eramo.amtalek.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.net.Uri
import android.os.Build
import android.text.Html
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.paging.PagingConfig
import com.google.gson.Gson
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentHomeBinding
import eramo.amtalek.domain.model.ResultModel
import eramo.amtalek.domain.search.LocationModel
import eramo.amtalek.util.state.ApiState
import eramo.amtalek.util.state.UiText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.text.NumberFormat
import java.text.ParseException
import java.util.Locale

fun Activity.hideSoftKeyboard() {
    currentFocus?.let {
        val inputMethodManager =
            ContextCompat.getSystemService(this, InputMethodManager::class.java)!!
        inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
    }
}

fun Fragment.showToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun Fragment.onBackPressed(code: () -> Unit) {
    requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
        code()
    }
}

fun Fragment.openLinkInBrowser(link: String) {
    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
}

fun ImageView.setColor(color: Int) {
    this.setColorFilter(
        ContextCompat.getColor(context, color),
        android.graphics.PorterDuff.Mode.SRC_IN
    )
}

fun NavController.isFragmentExist(destinationId: Int) =
    try {
        getBackStackEntry(destinationId)
        true
    } catch (e: Exception) {
        false
    }

fun navOptionsAnimation(): NavOptions {
    return if (LocalUtil.isEnglish())
        NavOptions.Builder()
            .setEnterAnim(R.anim.from_right)
            .setExitAnim(R.anim.to_left)
            .setPopEnterAnim(R.anim.from_left)
            .setPopExitAnim(R.anim.to_right)
            .build()
    else
        NavOptions.Builder()
            .setEnterAnim(R.anim.from_left)
            .setExitAnim(R.anim.to_right)
            .setPopEnterAnim(R.anim.from_right)
            .setPopExitAnim(R.anim.to_left)
            .build()
}

fun navOptionsFromBottomAnimation(): NavOptions {
    return NavOptions.Builder()
            .setEnterAnim(R.anim.from_bottom)
            .setExitAnim(R.anim.to_top)
            .setPopEnterAnim(R.anim.from_top)
            .setPopExitAnim(R.anim.to_bottom)
            .build()

}
fun navOptionsFromTopAnimation(): NavOptions {
    return NavOptions.Builder()
            .setEnterAnim(R.anim.from_top)
            .setExitAnim(R.anim.to_bottom)
            .setPopEnterAnim(R.anim.from_bottom)
            .setPopExitAnim(R.anim.to_top)
            .build()

}

fun pagingConfig() = PagingConfig(pageSize = PAGING_PER_PAGE, enablePlaceholders = false)

fun <T> toResultFlow(call: suspend () -> Response<T>): Flow<ApiState<T>> = flow {
    emit(ApiState.Loading())
    try {
        val response = call()
        if (response.isSuccessful) {
            emit(ApiState.Success(response.body()))
            Log.e("networkResponse", "Success\n" + response.body().toString())

        } else {
            val errorBody = response.errorBody()?.string()
            val errorObject = Gson().fromJson(errorBody, ResultModel::class.java)
            emit(ApiState.Error(UiText.DynamicString(errorObject.message)))
            Log.e("networkResponse", "Failure\n $errorBody")
        }

    } catch (e: HttpException) {
        Log.e("networkResponse", "HttpException\n ${e.message.toString()}")
        emit(ApiState.Error(UiText.StringResource(R.string.something_went_wrong)))

    } catch (e: IOException) {
        Log.e("networkResponse", "IOException\n ${e.message.toString()}")
        emit(ApiState.Error(UiText.StringResource(R.string.check_your_internet_connection)))

    } catch (e: Exception) {
            Log.e("networkResponse", "Exception\n ${e.message.toString()}")
        emit(ApiState.Error(UiText.StringResource(R.string.something_went_wrong)))
    }
}

// -------------------------------------------------------------- //

fun parseErrorResponse(jsonString: String): String {
    val jsonObject = Gson().fromJson(jsonString, ResultModel::class.java)
    return jsonObject.message
}

fun htmlFormatToString(htmlTxt: String): CharSequence {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        Html.fromHtml(htmlTxt, Html.FROM_HTML_MODE_COMPACT)
    else Html.fromHtml(htmlTxt)
}

@Throws(ParseException::class)
fun removePriceComma(value: String): String {
    return value.replace(",", "")
}

fun formatPrice(price: Double): String {
    return "%,.2f".format(Locale.ENGLISH, removePriceComma(price.toString()).toDouble())
}

fun formatNumber(input: Int): String {
    return "%,d".format(Locale.ENGLISH, input)
}

fun convertToArabicNumber(englishNumber: Int): String {
    val arabicFormat = NumberFormat.getInstance(Locale("ar"))
    return arabicFormat.format(englishNumber)
}

fun getYoutubeUrlId(url: String): String? {
    return Uri.parse(url).getQueryParameter("v")
}

fun TextView.setTextViewDrawableColor(color: Int) {
    this.compoundDrawablesRelative.filterNotNull().forEach { drawable ->
        drawable.mutate()
        drawable.colorFilter =
            PorterDuffColorFilter(
                ContextCompat.getColor(this.context, color),
                PorterDuff.Mode.SRC_IN
            )
    }
}
val selectedLocation = MutableLiveData<LocationModel>()



// -------------------------------------------------------------- //