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
    private const val USER_TYPE = "user_type"
    private const val FIRST_NAME = "user_name"
    private const val LAST_NAME = "last_name"
    private const val PHONE = "user_phone"
    private const val EMAIL = "user_email"
    private const val HAS_PACKAGE = "has_package"

    private const val BROKER_NAME = "broker_name"
    private const val BROKER_DASHBOARD_LINK = "broker_dashboard_link"


    private const val COUNTRY_ID = "country_id"
    private const val COUNTRY_NAME = "country_name"

    private const val CITY_ID = "city_id"
    private const val CITY_NAME = "city_name"
    private const val FIREBASE_TOKEN= "fireBaseToken"



    private const val USER_BIO = "user_bio"
    private const val PROFILE_IMAGE_URL = "user_profile_image_url"
    private const val COVER_IMAGE_URL = "user_cover_image_url"


    private const val CITY_FILTRATION_ID = "city_filtration"
    private const val CITY_FILTRATION_TITLE_EN = "city_filtration_en"
    private const val CITY_FILTRATION_TITLE_AR = "city_filtration_ar"
    private const val COUNTRY_FILTRATION_ID = "country_filtration_id"

    private const val HAS_DEEP_LINK = "HAS_DEEP_LINK"


    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences("user_util", Context.MODE_PRIVATE)

//        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
//
//        encryptedSharedPreferences =   EncryptedSharedPreferences.create(
//            "encrypted_preferences",
//            masterKeyAlias,
//            context,
//            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
//            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
//        )
    }

    fun saveUserInfo(
        isRemember: Boolean,
        userToken: String,
        userID: String,
        userType:String,
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
        hasPackage:String,
        brokerName:String,
        dashboardLink:String
    ) {
        sharedPreferences.edit().putBoolean(REMEMBER, isRemember).apply()

//        sharedPreferences.edit().putString(USER_TOKEN, userToken).apply()
        sharedPreferences.edit().putString(USER_TOKEN, userToken).apply()

        sharedPreferences.edit().putString(USER_ID, userID).apply()
        sharedPreferences.edit().putString(FIRST_NAME, firstName).apply()
        sharedPreferences.edit().putString(LAST_NAME, lastName).apply()
        sharedPreferences.edit().putString(PHONE, phone).apply()
        sharedPreferences.edit().putString(EMAIL, email).apply()
        sharedPreferences.edit().putString(USER_TYPE,userType).apply()
        sharedPreferences.edit().putString(COUNTRY_ID, countryId).apply()
        sharedPreferences.edit().putString(COUNTRY_NAME, countryName).apply()

        sharedPreferences.edit().putString(CITY_ID, cityId).apply()
        sharedPreferences.edit().putString(CITY_NAME, cityName).apply()

        sharedPreferences.edit().putString(USER_BIO, userBio).apply()
        sharedPreferences.edit().putString(PROFILE_IMAGE_URL, profileImageUrl).apply()
        sharedPreferences.edit().putString(HAS_PACKAGE,hasPackage).apply()
        sharedPreferences.edit().putString(BROKER_NAME,brokerName).apply()
        sharedPreferences.edit().putString(BROKER_DASHBOARD_LINK,dashboardLink).apply()
    }

    fun clearUserInfo() {
        sharedPreferences.edit().putBoolean(REMEMBER, false).apply()

//        sharedPreferences.edit().putString(USER_TOKEN, "").apply()
        sharedPreferences.edit().putString(USER_TOKEN, "").apply()

        sharedPreferences.edit().putString(USER_ID, "").apply()
        sharedPreferences.edit().putString(USER_TYPE,"").apply()
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
        sharedPreferences.edit().putString(HAS_PACKAGE,"").apply()
        sharedPreferences.edit().putString(FIREBASE_TOKEN,"").apply()
        sharedPreferences.edit().putString(CITY_FILTRATION_ID, "").apply()
        sharedPreferences.edit().putString(CITY_FILTRATION_TITLE_EN, "").apply()
        sharedPreferences.edit().putString(CITY_FILTRATION_TITLE_AR, "").apply()
        sharedPreferences.edit().putString(COUNTRY_FILTRATION_ID, "").apply()
        sharedPreferences.edit().putString(BROKER_NAME,"").apply()
        sharedPreferences.edit().putString(BROKER_DASHBOARD_LINK,"").apply()

    }

    fun saveFirstTime() = sharedPreferences.edit().putBoolean(IS_FIRST_TIME, false).apply()
    fun isFirstTime() = sharedPreferences.getBoolean(IS_FIRST_TIME, true)

    fun isUserLogin() = getUserToken().isNotEmpty()
    fun isRememberUser() = sharedPreferences.getBoolean(REMEMBER, false)

//    fun getUserToken() = sharedPreferences.getString(USER_TOKEN, "") ?: ""
    fun getUserToken() = sharedPreferences.getString(USER_TOKEN, "") ?: ""

    fun getUserId() = sharedPreferences.getString(USER_ID, "") ?: ""
    fun getUserType() = sharedPreferences.getString(USER_TYPE, "") ?: ""

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


    fun getHasPackage() = sharedPreferences.getString(HAS_PACKAGE, "") ?: ""

    fun setHasDeepLink(hasDeepLink: Boolean) = sharedPreferences.edit().putBoolean(HAS_DEEP_LINK, hasDeepLink).apply()
    fun hasDeepLink() = sharedPreferences.getBoolean(HAS_DEEP_LINK, false)
    fun saveFireBaseToken(token:String)= sharedPreferences.edit().putString(FIREBASE_TOKEN,token).apply()
    fun getFireBaseToken()= sharedPreferences.getString(FIREBASE_TOKEN,"")

    fun getBrokerName()= sharedPreferences.getString(BROKER_NAME,"")

    fun saveDashboardLink(link:String)= sharedPreferences.edit().putString(BROKER_DASHBOARD_LINK,link).apply()
    fun getDashboardLink()= sharedPreferences.getString(BROKER_DASHBOARD_LINK,"")

    fun saveUserCityFiltrationId(cityId: String) {
        sharedPreferences.edit().putString(CITY_FILTRATION_ID, cityId).apply()
    }

    fun saveUserCityFiltrationTitleEn(cityTitleEn: String) {
        sharedPreferences.edit().putString(CITY_FILTRATION_TITLE_EN, cityTitleEn).apply()
    }

    fun saveUserCityFiltrationTitleAr(cityTitleAr: String) {
        sharedPreferences.edit().putString(CITY_FILTRATION_TITLE_AR, cityTitleAr).apply()}

    fun saveUserCountryFiltrationTitleId(countryId: String) {
        sharedPreferences.edit().putString(COUNTRY_FILTRATION_ID, countryId).apply()}


    fun getCityFiltrationId():String{
        return sharedPreferences.getString(CITY_FILTRATION_ID, "0")!!
    }

    fun getCityFiltrationTitleEn():String{
        return sharedPreferences.getString(CITY_FILTRATION_TITLE_EN, "")!!
    }

    fun getCityFiltrationTitleAr():String{
        return sharedPreferences.getString(CITY_FILTRATION_TITLE_AR, "")!!
    }

    fun getUserCountryFiltrationTitleId():String{
        return sharedPreferences.getString(COUNTRY_FILTRATION_ID, "")!!
    }
}