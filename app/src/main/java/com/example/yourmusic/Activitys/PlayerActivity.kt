package com.example.yourmusic.Activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.RoundedCorner
import androidx.media3.exoplayer.ExoPlayer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.yourmusic.MyExoPlayer
import com.example.yourmusic.R
import com.example.yourmusic.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityPlayerBinding.inflate(layoutInflater)
    }
    private lateinit var exoPlayer: ExoPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        ///get current song  data that is playing

        MyExoPlayer.getCurrentSong()?.apply {
            binding.textView6.text=title
            binding.textView7.text=singer
            Glide.with(this@PlayerActivity).load(image).circleCrop().into(binding.imageView4)

            exoPlayer=MyExoPlayer.getInstance()!!
            binding.playerView.player=exoPlayer


        }

    }
}