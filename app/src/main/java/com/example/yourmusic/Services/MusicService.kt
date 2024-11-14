package com.example.yourmusic.Services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import com.example.yourmusic.PlayOnNotification.MusicService


class MusicService: Service() {
  var myBinder=MyBinder()
    val mediaPlayer:MediaPlayer?=null

    
    override fun onBind(intent: Intent?): IBinder {
        return myBinder
    }

    inner class MyBinder:Binder(){
        fun currentService(): com.example.yourmusic.Services.MusicService {
            return this@MusicService
        }
    }
}