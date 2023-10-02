package eramo.amtalek.util.deeplink

import androidx.core.net.toUri

object DeeplinkUtil {

    const val Key = "DeeplinkKey"

    fun toLogin(argument: Boolean = false) =
        "android-app://eramo.amtalek/loginFragment?proceedRequire={$argument}".toUri()

    fun toPolicy() =
        "android-app://eramo.amtalek/policyFragment".toUri()

    fun toDatePicker() =
        "android-app://eramo.amtalek/datePickerDialog".toUri()
}