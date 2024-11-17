package com.example.yourmusic.Services

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import com.example.yourmusic.Activitys.PlayerActivity
import com.example.yourmusic.PlayOnNotification.ApplicationClass
import com.example.yourmusic.R

class MusicService: Service() {
  var myBinder=MyBinder()
    var mediaPlayer:MediaPlayer?=null
    private lateinit var mediaSession:MediaSessionCompat

    
    override fun onBind(intent: Intent?): IBinder {
        mediaSession=MediaSessionCompat(baseContext,"My Music")
        return myBinder
    }

    inner class MyBinder:Binder(){
        fun currentService():MusicService {
            return this@MusicService
        }
    }

    ////showing the notification



    fun showNotificatin(){
        val notification=NotificationCompat.Builder(baseContext,ApplicationClass.CHANNEL_ID)
            .setContentTitle(PlayerActivity.musicListPA[PlayerActivity.songPosition].title)
            .setContentText(PlayerActivity.musicListPA[PlayerActivity.songPosition].singer)
            .setSmallIcon(R.drawable.musicplayer)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.love))
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediaSession.sessionToken))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .addAction(R.drawable.previous,"Previous",null)
            .addAction(R.drawable.play,"Play",null)
            .addAction(R.drawable.nextbutton,"next",null)
            .addAction(R.drawable.backrrow,"exit",null)
            .build()

        startForeground(413,notification)
    }
}