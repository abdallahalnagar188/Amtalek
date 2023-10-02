package eramo.amtalek.util.deeplink

import android.content.Intent
import androidx.navigation.NavController
import eramo.amtalek.R

interface DeeplinkHandler {
    fun handleDeeplink(intent: Intent?, navController: NavController)
}