package com.example.yourmusic.Services

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.Service

import android.content.Intent
import android.graphics.Bitmap


import android.media.MediaPlayer
import android.os.Binder
import android.os.Build

import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat



import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.SeekBar

import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition


import com.example.yourmusic.Activitys.PlayerActivity
import com.example.yourmusic.Activitys.PlayerActivity.Companion.binding
import com.example.yourmusic.Activitys.PlayerActivity.Companion.isPlay
import com.example.yourmusic.Activitys.PlayerActivity.Companion.musicListPA
import com.example.yourmusic.Activitys.PlayerActivity.Companion.musicService
import com.example.yourmusic.Activitys.PlayerActivity.Companion.songPosition
import com.example.yourmusic.MainActivity
import com.example.yourmusic.PlayOnNotification.ApplicationClass
import com.example.yourmusic.R



class MusicService : Service() {
    var myBinder = MyBinder()
    var mediaPlayer: MediaPlayer? = null
    private lateinit var mediaSession: MediaSessionCompat


    override fun onBind(intent: Intent?): IBinder {
        mediaSession = MediaSessionCompat(baseContext, "My Music")
        return myBinder
    }

    inner class MyBinder : Binder() {
        fun currentService(): MusicService {
            return this@MusicService
        }
    }

    ////showing the notification


    @SuppressLint("UnspecifiedImmutableFlag")
     fun showNotificatin(playPause: Int) {


        val intent = Intent(baseContext, MainActivity::class.java)

        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        /// for  notification previous
        val prevtent = Intent(
            baseContext,
            NotificationReciver::class.java
        ).setAction(ApplicationClass.PREVIOUS)
        val prevPendingIntent = PendingIntent.getBroadcast(baseContext, 0, prevtent, flag)

        /// for  notification previous
        val playIntent =
            Intent(baseContext, NotificationReciver::class.java).setAction(ApplicationClass.PLAY)
        val playPendingIntent = PendingIntent.getBroadcast(baseContext, 1, playIntent, flag)

        /// for  notification previous
        val nextIntent =
            Intent(baseContext, NotificationReciver::class.java).setAction(ApplicationClass.NEXT)
        val nextPendingIntent = PendingIntent.getBroadcast(baseContext, 0, nextIntent, flag)

        /// for  notification previous
        val exitIntent =
            Intent(baseContext, NotificationReciver::class.java).setAction(ApplicationClass.EXIT)
        val exitPendingIntent = PendingIntent.getBroadcast(baseContext, 0, exitIntent, flag)


        //
        Glide.with(baseContext)
            .asBitmap()
            .load(PlayerActivity.musicListPA[PlayerActivity.songPosition].image)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val notificationManager = NotificationManagerCompat.from(baseContext)
                    val notification =NotificationCompat.Builder(baseContext,ApplicationClass.CHANNEL_ID)
                        .setSmallIcon(R.drawable.musicplayer)
                        .setContentTitle(PlayerActivity.musicListPA[PlayerActivity.songPosition].title)
                        .setContentText(PlayerActivity.musicListPA[PlayerActivity.songPosition].singer)
                        .setLargeIcon(resource)
                        .setStyle(
                            androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediaSession.sessionToken)
                        )
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setOnlyAlertOnce(true)
                        .addAction(R.drawable.previous, "Previous", prevPendingIntent)
                        .addAction(playPause, "Play", playPendingIntent)
                        .addAction(R.drawable.nextbutton, "next", nextPendingIntent)
                        .addAction(R.drawable.backrrow, "exit", exitPendingIntent)
                        .build()

                    startForeground(13,notification)
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }
     fun createMediaPlayer() {

        // in this function create media

        try {
            if (musicService!!.mediaPlayer == null) musicService!!.mediaPlayer = MediaPlayer()
            musicService!!.mediaPlayer!!.reset()
            musicService!!.mediaPlayer!!.setDataSource(musicListPA[songPosition].url)
            musicService!!.mediaPlayer!!.prepare()

            musicService!!.showNotificatin(R.drawable.pause)

            binding.pausePlay.setImageResource(R.drawable.pause)
        } catch (e: Exception) {
            return
        }

    }

}