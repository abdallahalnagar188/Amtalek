package eramo.amtalek.di

import android.content.Context
import com.akexorcist.localizationactivity.ui.LocalizationApplication
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import dagger.hilt.android.HiltAndroidApp
import java.util.Locale

@HiltAndroidApp
class EventsApplication : LocalizationApplication(){
    override fun onCreate() {
        super.onCreate()
        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)
    }


    //    init {
//
//    LocalUtil.setLocal(r, "ar")
//    }
    override fun getDefaultLanguage(context: Context): Locale {
        return Locale("ar")
    }

}