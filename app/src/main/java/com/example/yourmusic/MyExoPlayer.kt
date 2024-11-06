package com.example.yourmusic

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.yourmusic.Model.SongModel
import com.example.yourmusic.PlayOnNotification.MusicService

object MyExoPlayer :AppCompatActivity(),ServiceConnection{
    private var exoPlayer: ExoPlayer?=null
    private var currentSong :SongModel?=null
    var musicService:MusicService?=null

    fun getInstance():ExoPlayer?{
        return exoPlayer
    }

    fun getCurrentSong():SongModel?{
        return currentSong
    }


    fun staryPlaying(context: Context,song:SongModel){
        if (exoPlayer==null)
           exoPlayer=ExoPlayer.Builder(context).build()

        if (currentSong!=song){
            ///if  this song already play then not restary it
            currentSong=song
            currentSong?.url?.apply {
                val mediaItem=MediaItem.fromUri(this)
                exoPlayer?.setMediaItem(mediaItem)
                exoPlayer?.prepare()
                exoPlayer?.play()
            }
        }

    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        var binder=service as MusicService.MyBinder
        musicService=binder.currentSurvice()
    }

    override fun onServiceDisconnected(name: ComponentName?) {
       musicService=null



    }


}