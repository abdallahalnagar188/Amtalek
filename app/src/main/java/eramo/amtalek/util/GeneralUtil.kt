package eramo.amtalek.util

import android.app.Activity
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.paging.PagingConfig
import eramo.amtalek.R
import eramo.amtalek.util.Constants.TAG
import eramo.amtalek.util.state.ApiState
import eramo.amtalek.util.state.UiText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.text.NumberFormat
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

fun ImageView.setColor(color: Int) {
    this.setColorFilter(
        ContextCompat.getColor(context, color),
        android.graphics.PorterDuff.Mode.SRC_IN
    )
}

fun convertToArabicNumber(englishNumber: Int): String {
    val arabicFormat = NumberFormat.getInstance(Locale("ar"))
    return arabicFormat.format(englishNumber)
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

fun pagingConfig() = PagingConfig(pageSize = Constants.PAGING_PER_PAGE, enablePlaceholders = false)

fun <T> toResultFlow(call: suspend () -> Response<T>): Flow<ApiState<T>> = flow {
    emit(ApiState.Loading())
    try {
        val response = call()
        if (response.isSuccessful) emit(ApiState.Success(response.body()))
        else emit(ApiState.Error(UiText.DynamicString(response.message())))
    } catch (e: HttpException) {
        Log.d(TAG, e.message.toString())
        emit(ApiState.Error(UiText.StringResource(R.string.something_went_wrong)))
    } catch (e: IOException) {
        Log.d(TAG, e.message.toString())
        emit(ApiState.Error(UiText.StringResource(R.string.check_your_internet_connection)))
    } catch (e: Exception) {
        Log.d(TAG, e.message.toString())
        emit(ApiState.Error(UiText.StringResource(R.string.something_went_wrong)))
    }
}