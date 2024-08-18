package eramo.amtalek.util

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import java.util.Locale

object LocalUtil {
    private lateinit var sharedPreferences: SharedPreferences
    private const val LANGUAGE = "language"

    private const val PREFS_NAME = "app_prefs"
    private const val LAST_ACTION = "last_action"
    private const val LAST_PHONE_NUMBER = "last_phone_number"
    private const val LAST_EMAIL = "last_email"

    fun saveLastAction(context: Context, action: String, phoneNumber: String? = null, email: String? = null) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString(LAST_ACTION, action)
            putString(LAST_PHONE_NUMBER, phoneNumber)
            putString(LAST_EMAIL, email)
            apply()
        }
    }

    fun getLastAction(context: Context): Pair<String?, Pair<String?, String?>> {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val action = sharedPreferences.getString(LAST_ACTION, null)
        val phoneNumber = sharedPreferences.getString(LAST_PHONE_NUMBER, null)
        val email = sharedPreferences.getString(LAST_EMAIL, null)
        return Pair(action, Pair(phoneNumber, email))
    }

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences("local", Context.MODE_PRIVATE)
    }

    fun setLocal(activity: Activity, language: String) {

        sharedPreferences.edit().putString(LANGUAGE, language).apply()
    }


    fun loadLocal(activity: Activity) =
        setLocal(activity, sharedPreferences.getString(LANGUAGE, "ar")?:"ar")

    fun getLang() = sharedPreferences.getString(LANGUAGE, "ar")

    fun isEnglish() = sharedPreferences.getString(LANGUAGE, "en").equals("en")
}