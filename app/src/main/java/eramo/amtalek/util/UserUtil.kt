package eramo.amtalek.util

import android.content.Context
import android.content.SharedPreferences
import java.util.regex.Pattern

object UserUtil {

    private lateinit var sharedPreferences: SharedPreferences
    private const val IS_FIRST_TIME = "isFirsTime"
    private const val REMEMBER = "remember"
    private const val USER_ID = "user_id"
    private const val USER_TOKEN = "token"
    private const val USERNAME = "username"
    private const val USER_PASS = "userPass"
    private const val ADDRESS = "address"
    private const val COUNTRY_ID = "countryId"
    private const val CITY_ID = "cityId"
    private const val REGION_ID = "regionId"
    private const val USER_PHONE = "user_phone"
    private const val USER_EMAIL = "user_email"
    private const val USER_PROFILE = "user_profile"
    private const val HAS_DEEP_LINK = "HAS_DEEP_LINK"
    val PASSWORD_PATTERN: Pattern = Pattern.compile(
        "^" + "(?=.*[0-9])" +         //at least 1 digit
                "(?=.*[a-z])" +         //at least 1 lower case letter
                "(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +  //any letter
                "(?=.*[@#$%^&+=])" +  //at least 1 special character
                "(?=\\S+$)" +  //no white spaces
                ".{6,}" +  //at least 4 characters
                "$"
    )

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences("user_util", Context.MODE_PRIVATE)
    }

    fun saveUserInfo(
        isRemember: Boolean,
        userID: String,
        userToken: String,
        username: String,
        userPass: String,
        address: String,
        countryId: String,
        cityId: String,
        regionId: String,
        userPhone: String,
        userEmail: String,
        m_image: String
    ) {
        sharedPreferences.edit().putBoolean(REMEMBER, isRemember).apply()
        sharedPreferences.edit().putString(USER_ID, userID).apply()
        sharedPreferences.edit().putString(USER_TOKEN, userToken).apply()
        sharedPreferences.edit().putString(USERNAME, username).apply()
        sharedPreferences.edit().putString(USER_PASS, userPass).apply()
        sharedPreferences.edit().putString(ADDRESS, address).apply()
        sharedPreferences.edit().putString(COUNTRY_ID, countryId).apply()
        sharedPreferences.edit().putString(CITY_ID, cityId).apply()
        sharedPreferences.edit().putString(REGION_ID, regionId).apply()
        sharedPreferences.edit().putString(USER_PHONE, userPhone).apply()
        sharedPreferences.edit().putString(USER_EMAIL, userEmail).apply()
        sharedPreferences.edit().putString(USER_PROFILE, m_image).apply()
    }

    fun saveUserProfile(m_image: String) {
        sharedPreferences.edit().putString(USER_PROFILE, m_image).apply()
    }

    fun clearUserInfo() {
        sharedPreferences.edit().putBoolean(REMEMBER, false).apply()
        sharedPreferences.edit().putString(USER_ID, "").apply()
        sharedPreferences.edit().putString(USER_TOKEN, "").apply()
        sharedPreferences.edit().putString(USERNAME, "").apply()
        sharedPreferences.edit().putString(USER_PASS, "").apply()
        sharedPreferences.edit().putString(ADDRESS, "").apply()
        sharedPreferences.edit().putString(USER_PHONE, "").apply()
        sharedPreferences.edit().putString(USER_EMAIL, "").apply()
        sharedPreferences.edit().putString(USER_PROFILE, "").apply()
    }

    fun saveFirstTime() = sharedPreferences.edit().putBoolean(IS_FIRST_TIME, false).apply()

    fun isFirstTime() = sharedPreferences.getBoolean(IS_FIRST_TIME, true)

    fun isUserLogin() = getUserId().isNotEmpty()

    fun isRememberUser() = sharedPreferences.getBoolean(REMEMBER, false)

    fun getUserId() = sharedPreferences.getString(USER_ID, "") ?: ""

    fun getUserToken() = sharedPreferences.getString(USER_TOKEN, "") ?: ""

    fun getUserName() = sharedPreferences.getString(USERNAME, "") ?: ""

    fun getUserPass() = sharedPreferences.getString(USER_PASS, "") ?: ""

    fun getUserAddress() = sharedPreferences.getString(ADDRESS, "") ?: ""

    fun getCountryId() = sharedPreferences.getString(COUNTRY_ID, "") ?: ""

    fun getCityId() = sharedPreferences.getString(CITY_ID, "") ?: ""

    fun getRegionId() = sharedPreferences.getString(REGION_ID, "") ?: ""

    fun getUserPhone() = sharedPreferences.getString(USER_PHONE, "") ?: ""

    fun getUserEmail() = sharedPreferences.getString(USER_EMAIL, "") ?: ""

    fun getUserProfile() = sharedPreferences.getString(USER_PROFILE, "") ?: ""

    fun setHasDeepLink(hasDeepLink: Boolean) = sharedPreferences.edit().putBoolean(HAS_DEEP_LINK, hasDeepLink).apply()

    fun hasDeepLink() = sharedPreferences.getBoolean(HAS_DEEP_LINK, false)

}