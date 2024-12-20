package com.example.yourmusic.PlayOnNotification

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class ApplicationClass: Application() {
    companion object{
        const val CHANNEL_ID="channel1"
        const val PLAY="play"
        const val NEXT="next"
        const val PREVIOUS="previous"
        const val EXIT="exit"
    }
    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.Q){
            val notificationChannel=NotificationChannel(CHANNEL_ID,"Now Playing Song",NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.description="This is important channel for song"
            val notificarionManager=getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificarionManager.createNotificationChannel(notificationChannel)
        }

    }
}