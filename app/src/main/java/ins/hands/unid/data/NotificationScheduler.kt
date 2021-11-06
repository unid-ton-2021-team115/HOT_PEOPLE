package ins.hands.unid.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.O)
class NotificationScheduler(val notificationManager: NotificationManager) {



    init{


    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChanner(notificationManager: NotificationManager)
    {
        val channel = NotificationChannel(12345.toString(),"UNICHANER",NotificationManager.IMPORTANCE_DEFAULT).apply{
            description="Channel of UnithonProject"
            lightColor = Color.GREEN
            enableLights(true)
        }
        notificationManager.createNotificationChannel(channel)
    }
    fun sendNotification(title:String, content : String)
    {


    }
}