package eramo.amtalek.util.deeplink

import android.content.Intent
import androidx.navigation.NavController

class DeeplinkHandlerImpl : DeeplinkHandler {
    override fun handleDeeplink(intent: Intent?, navController: NavController) {
        val deeplink = intent?.getStringExtra(DeeplinkUtil.Key)
        deeplink?.let { value ->
//            when (value) {
//                "15" -> navController.navigate(R.id.aboutUsFragment)
//                "18" -> navController.navigate(R.id.contactUsFragment)
//            }
        }
    }
}