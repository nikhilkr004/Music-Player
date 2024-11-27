package com.example.yourmusic.Services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.yourmusic.Activitys.PlayerActivity
import com.example.yourmusic.Activitys.PlayerActivity.Companion.musicListPA
import com.example.yourmusic.Activitys.PlayerActivity.Companion.musicService
import com.example.yourmusic.Activitys.PlayerActivity.Companion.songPosition
import com.example.yourmusic.Fragment.NowPlaying
import com.example.yourmusic.PlayOnNotification.ApplicationClass
import com.example.yourmusic.R
import com.example.yourmusic.setSongPosition
import kotlin.system.exitProcess

class NotificationReciver:BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when(intent?.action){
            ApplicationClass.PREVIOUS-> prevNextSong(false,context!!)
            ApplicationClass.PLAY-> if (PlayerActivity.isPlay) pauseMusic() else playMusic()
            ApplicationClass.NEXT-> prevNextSong(true,context!!)
            ApplicationClass.EXIT-> {
                musicService!!.stopForeground(true)
                musicService =null
                exitProcess(1)

            }
        }
    }
    private fun playMusic(){
        PlayerActivity.isPlay=true
        musicService!!.mediaPlayer!!.start()
        musicService!!.showNotificatin(R.drawable.pause)
        PlayerActivity.binding.pausePlay.setImageResource(R.drawable.pause)
        NowPlaying.binding.playPause.setImageResource(R.drawable.pause)
    }

    private fun pauseMusic(){
        PlayerActivity.isPlay=false
        musicService!!.mediaPlayer!!.pause()
        musicService!!.showNotificatin(R.drawable.play)
        PlayerActivity.binding.pausePlay.setImageResource(R.drawable.play)
        NowPlaying.binding.playPause.setImageResource(R.drawable.play)
    }

    private fun prevNextSong(increment:Boolean, context: Context){
        setSongPosition(increment)
        PlayerActivity.musicService!!.createMediaPlayer()
        Glide.with(context).load(PlayerActivity.musicListPA[PlayerActivity.songPosition].image).into(PlayerActivity.binding.musicImage)
        PlayerActivity.binding.songName.text=PlayerActivity.musicListPA[PlayerActivity.songPosition].title.toString()
        PlayerActivity.binding.singerName.text=PlayerActivity.musicListPA[PlayerActivity.songPosition].singer.toString()
        playMusic()

    }
}