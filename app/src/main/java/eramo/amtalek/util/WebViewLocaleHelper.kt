package eramo.amtalek.util

import android.app.Activity
import android.webkit.WebView
import androidx.annotation.MainThread

/**
 * WebViewLocaleHelper implements a workaround that fixes the unwanted side effect while
 * using a [WebView] introduced in Android N.
 *
 * For unknown reasons, the very first creation of a [WebView] (either programmatically
 * or via inflation) resets an application locale to the system default.
 * More on that: https://issuetracker.google.com/issues/37113860
 */
class WebViewLocaleHelper(private val activity: Activity) {

    private var requireWorkaround = true

    @MainThread
    fun implementWorkaround() {
        if (requireWorkaround) {
            requireWorkaround = false
            WebView(activity).destroy()
            LocalUtil.loadLocal(activity)
        }
    }
}