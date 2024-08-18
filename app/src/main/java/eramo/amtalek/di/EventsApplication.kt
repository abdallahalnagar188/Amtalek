package eramo.amtalek.di

import android.content.Context
import com.akexorcist.localizationactivity.ui.LocalizationApplication
import dagger.hilt.android.HiltAndroidApp
import java.util.Locale

@HiltAndroidApp
class EventsApplication : LocalizationApplication(){


    //    init {
//
//    LocalUtil.setLocal(r, "ar")
//    }
    override fun getDefaultLanguage(context: Context): Locale {
        return Locale("ar")
    }

}