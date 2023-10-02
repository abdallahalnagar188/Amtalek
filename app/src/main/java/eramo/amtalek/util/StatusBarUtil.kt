package eramo.amtalek.util

import android.annotation.TargetApi
import android.app.Activity
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import eramo.amtalek.R

object StatusBarUtil {

    private lateinit var window: Window

    fun init(window: Window) {
        this.window = window
    }

    fun hide() {
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    fun transparent() {
        // clear fullScreen
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        // set transparent
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        // make status icons white
        var flags: Int = window.decorView.systemUiVisibility
        flags = flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        window.decorView.systemUiVisibility = flags
    }

    fun whiteWithBackground(activity: Activity, color: Int) {
        // clear fullScreen
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        // clear transparent
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        // make status icons white
        var flags: Int = window.decorView.systemUiVisibility
        flags = flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        window.decorView.systemUiVisibility = flags
        // status background
        window.statusBarColor = ContextCompat.getColor(activity, color)
    }

    fun blackWithBackground(activity: Activity, color: Int) {
        // clear fullScreen
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        // clear transparent
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        // make status icons black
        val flag: Int = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.decorView.systemUiVisibility = flag
        // status background
        window.statusBarColor = ContextCompat.getColor(activity, color)
    }

}