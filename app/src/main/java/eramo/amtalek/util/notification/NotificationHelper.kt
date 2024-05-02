package eramo.amtalek.util.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import eramo.amtalek.R
import eramo.amtalek.data.remote.dto.NotificationDto
import java.io.IOException
import java.net.URL
import kotlin.random.Random

class NotificationHelper(private val context: Context) {

    private val CHANNEL_ID = "64325269103265"
    private val CHANNEL_NAME = "Events Channel"

    fun push(notificationDto: NotificationDto) {


        val pendingIntent: PendingIntent = NavDeepLinkBuilder(context).setGraph(R.navigation.nav_app).setArguments(null)
            .setDestination(R.id.homeFragment).createPendingIntent()
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(notificationManager)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setColor(context.resources.getColor(R.color.amtalek_blue))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setContentText(notificationDto.body)
            .setContentTitle(notificationDto.title)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(pendingIntent)
            .setChannelId(CHANNEL_ID)
            .setOnlyAlertOnce(true)
            .setAutoCancel(true)

        var image: Bitmap? = null
        notificationDto.image?.let {
            try {
                val url = URL(it)
                image = BitmapFactory.decodeStream(url.openConnection().getInputStream())
            } catch (e: IOException) {
                println(e)
            }
        }

        image?.let {
            builder.setStyle(NotificationCompat.BigTextStyle().bigText(notificationDto.body))
                .setStyle(NotificationCompat.BigPictureStyle().bigPicture(it))
        }

        notificationManager.notify(Random.nextInt(), builder.build())
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.apply {
                lightColor = Color.GREEN
                enableLights(true)
                setShowBadge(true)
            }
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}