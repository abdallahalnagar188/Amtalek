package eramo.amtalek.util

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

object UserUtil {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var encryptedSharedPreferences: SharedPreferences

    private const val IS_FIRST_TIME = "isFirsTime"
    private const val REMEMBER = "remember"

    private const val USER_TOKEN = "user_token"

    private const val USER_ID = "user_id"
    private const val FIRST_NAME = "user_name"
    private const val LAST_NAME = "last_name"
    private const val PHONE = "user_phone"
    private const val EMAIL = "user_email"

    private const val COUNTRY_ID = "country_id"
    private const val COUNTRY_NAME = "country_name"

    private const val CITY_ID = "city_id"
    private const val CITY_NAME = "city_name"

    private const val USER_BIO = "user_bio"
    private const val PROFILE_IMAGE_URL = "user_profile_image_url"
    private const val COVER_IMAGE_URL = "user_cover_image_url"

    private const val HAS_DEEP_LINK = "HAS_DEEP_LINK"


    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences("user_util", Context.MODE_PRIVATE)

        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

        encryptedSharedPreferences =   EncryptedSharedPreferences.create(
            "encrypted_preferences",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun saveUserInfo(
        isRemember: Boolean,

        userToken: String,
        userID: String,

        firstName: String,
        lastName: String,
        phone: String,
        email: String,

        countryId: String,
        countryName: String,

        cityId: String,
        cityName: String,

        userBio: String,
        profileImageUrl: String,
    ) {
        sharedPreferences.edit().putBoolean(REMEMBER, isRemember).apply()

//        sharedPreferences.edit().putString(USER_TOKEN, userToken).apply()
        encryptedSharedPreferences.edit().putString(USER_TOKEN, userToken).apply()

        sharedPreferences.edit().putString(USER_ID, userID).apply()
        sharedPreferences.edit().putString(FIRST_NAME, firstName).apply()
        sharedPreferences.edit().putString(LAST_NAME, lastName).apply()
        sharedPreferences.edit().putString(PHONE, phone).apply()
        sharedPreferences.edit().putString(EMAIL, email).apply()

        sharedPreferences.edit().putString(COUNTRY_ID, countryId).apply()
        sharedPreferences.edit().putString(COUNTRY_NAME, countryName).apply()

        sharedPreferences.edit().putString(CITY_ID, cityId).apply()
        sharedPreferences.edit().putString(CITY_NAME, cityName).apply()

        sharedPreferences.edit().putString(USER_BIO, userBio).apply()
        sharedPreferences.edit().putString(PROFILE_IMAGE_URL, profileImageUrl).apply()
    }

    fun clearUserInfo() {
        sharedPreferences.edit().putBoolean(REMEMBER, false).apply()

//        sharedPreferences.edit().putString(USER_TOKEN, "").apply()
        encryptedSharedPreferences.edit().putString(USER_TOKEN, "").apply()

        sharedPreferences.edit().putString(USER_ID, "").apply()
        sharedPreferences.edit().putString(FIRST_NAME, "").apply()
        sharedPreferences.edit().putString(LAST_NAME, "").apply()
        sharedPreferences.edit().putString(PHONE, "").apply()
        sharedPreferences.edit().putString(EMAIL, "").apply()

        sharedPreferences.edit().putString(COUNTRY_ID, "").apply()
        sharedPreferences.edit().putString(COUNTRY_NAME, "").apply()

        sharedPreferences.edit().putString(CITY_ID, "").apply()
        sharedPreferences.edit().putString(CITY_NAME, "").apply()

        sharedPreferences.edit().putString(USER_BIO, "").apply()
        sharedPreferences.edit().putString(PROFILE_IMAGE_URL, "").apply()
        sharedPreferences.edit().putString(COVER_IMAGE_URL, "").apply()
    }

    fun saveFirstTime() = sharedPreferences.edit().putBoolean(IS_FIRST_TIME, false).apply()
    fun isFirstTime() = sharedPreferences.getBoolean(IS_FIRST_TIME, true)

    fun isUserLogin() = getUserToken().isNotEmpty()
    fun isRememberUser() = sharedPreferences.getBoolean(REMEMBER, false)

//    fun getUserToken() = sharedPreferences.getString(USER_TOKEN, "") ?: ""
    fun getUserToken() = encryptedSharedPreferences.getString(USER_TOKEN, "") ?: ""

    fun getUserId() = sharedPreferences.getString(USER_ID, "") ?: ""

    fun getUserFirstName() = sharedPreferences.getString(FIRST_NAME, "") ?: ""
    fun getUserLastName() = sharedPreferences.getString(LAST_NAME, "") ?: ""
    fun getUserPhone() = sharedPreferences.getString(PHONE, "") ?: ""
    fun getUserEmail() = sharedPreferences.getString(EMAIL, "") ?: ""

    fun getCountryId() = sharedPreferences.getString(COUNTRY_ID, "") ?: ""
    fun getCountryName() = sharedPreferences.getString(COUNTRY_NAME, "") ?: ""

    fun getCityId() = sharedPreferences.getString(CITY_ID, "") ?: ""
    fun getCityName() = sharedPreferences.getString(CITY_NAME, "") ?: ""

    fun getUserBio() = sharedPreferences.getString(USER_BIO, "") ?: ""
    fun getUserProfileImageUrl() = sharedPreferences.getString(PROFILE_IMAGE_URL, "") ?: ""
    fun getUserCoverImageUrl() = sharedPreferences.getString(COVER_IMAGE_URL, "") ?: ""

    fun setHasDeepLink(hasDeepLink: Boolean) = sharedPreferences.edit().putBoolean(HAS_DEEP_LINK, hasDeepLink).apply()
    fun hasDeepLink() = sharedPreferences.getBoolean(HAS_DEEP_LINK, false)

}