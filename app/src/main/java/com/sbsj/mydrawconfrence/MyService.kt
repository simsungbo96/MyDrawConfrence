package com.sbsj.mydrawconfrence

import android.app.*
import android.app.PendingIntent.FLAG_MUTABLE
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import java.lang.UnsupportedOperationException
private const val FOREGROUND_SERVICE_ID = 1000
private const val CHANNEL_ID = "MY_ID"

class MyService :Service(){

    companion object{
        var myService  = MyService()
    }
    override fun startService(service: Intent?): ComponentName? {
        return super.startService(service)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
       return START_REDELIVER_INTENT
    }
    override fun onBind(intent: Intent?): IBinder? {
        throw UnsupportedOperationException("NOT YET")
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
    }

    override fun onCreate() {
        super.onCreate()
        startForegroundService()
    }

    open fun startForegroundService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).apply {
                createNotificationChannel(serviceChannel)
            }
        }

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )

        val notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(this, CHANNEL_ID)
        } else {
            Notification.Builder(this)
        }
            .setContentTitle("Foreground Service")
            .setSmallIcon(R.drawable.ic_baseline_arrow_back_24)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(FOREGROUND_SERVICE_ID, notification)
    }
}