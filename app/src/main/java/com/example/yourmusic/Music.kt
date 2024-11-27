package com.example.yourmusic

import android.media.MediaMetadataRetriever
import com.example.yourmusic.Activitys.PlayerActivity
import com.example.yourmusic.Activitys.PlayerActivity.Companion.musicListPA
import com.example.yourmusic.Activitys.PlayerActivity.Companion.songPosition

 fun setSongPosition(increment: Boolean) {
     ///repeat
     if (!PlayerActivity.repeat){
         if (increment) {
             if (musicListPA.size - 1 == songPosition)
                 songPosition = 0
             else ++songPosition
         } else {
             if (0 == songPosition)
                 songPosition = musicListPA.size - 1
             else --songPosition
         }
     }
}
