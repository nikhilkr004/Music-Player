package com.example.yourmusic.Activitys

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.util.Util
import com.example.yourmusic.MainActivity
import com.example.yourmusic.Model.SongModel
import com.example.yourmusic.R
import com.example.yourmusic.Utils
import com.example.yourmusic.databinding.ActivityPlayerBinding
import com.google.firebase.database.core.utilities.Utilities
import kotlinx.coroutines.Runnable


class PlayerActivity : AppCompatActivity(){
    private val binding by lazy {
        ActivityPlayerBinding.inflate(layoutInflater)
    }

    companion object {
        var REQUEST_CODE = 1
        lateinit var musicListPA: ArrayList<SongModel>
        var songPosition: Int = -1
        var mediaPlayer: MediaPlayer? = null
        var isPlay: Boolean = false
    }

    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initalizedLayout()
        //play pause btn
        binding.pausePlay.setOnClickListener {
            if (isPlay) pauseMusic()
            else playMusic()
        }

        /// next and prev

        binding.nextBtn.setOnClickListener { prevNextSong(increment = true) }
        binding.previusBtn.setOnClickListener { prevNextSong(increment = false) }


    }

    private fun setLayout() {
        Glide.with(this).load(musicListPA[songPosition].image).into(binding.musicImage)
        binding.songName.text = musicListPA[songPosition].title.toString()
        binding.singerName.text = musicListPA[songPosition].singer.toString()

    }

    private fun createMediaPlayer() {

        // in this function create media

        try {
            if (mediaPlayer == null) mediaPlayer = MediaPlayer()
            mediaPlayer!!.reset()
            mediaPlayer!!.setDataSource(musicListPA[songPosition].url)
            mediaPlayer!!.prepare()
            mediaPlayer!!.start()

            initializeSeekbar()

            binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) mediaPlayer!!.seekTo(progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {

                }
            })

            isPlay = true
            binding.pausePlay.setImageResource(R.drawable.pause)
        } catch (e: Exception) {
            return
        }

    }

    private fun initalizedLayout() {
        songPosition = intent.getIntExtra("index", 0)
        val identifierr = intent.getStringExtra("class").toString()
        ///jo mene class pass ki hai agar vo PopularAdapter hai tho kya karna hai


        when (identifierr) {
            "hindi" -> {
                musicListPA = ArrayList()
                musicListPA!!.addAll(MainActivity.hindi)
                setLayout()
                createMediaPlayer()
            }

            "english" -> {
                musicListPA = ArrayList()
                musicListPA!!.addAll(MainActivity.songs)
                setLayout()
                createMediaPlayer()
            }

            "PopularAdapter" -> {
                musicListPA = ArrayList()
                musicListPA!!.addAll(MainActivity.hindi)
                setLayout()
                createMediaPlayer()
            }
        }

    }

    private fun playMusic() {
        binding.pausePlay.setImageResource(R.drawable.pause)
        isPlay = true
        mediaPlayer!!.start()
    }

    private fun pauseMusic() {
        binding.pausePlay.setImageResource(R.drawable.play)
        isPlay = false
        mediaPlayer!!.pause()
    }

    private fun prevNextSong(increment: Boolean) {
        if (increment) {
            setSongPosition(increment = true)
            setLayout()
            createMediaPlayer()
        } else {
            setSongPosition(increment = false)
            setLayout()
            createMediaPlayer()
        }

    }

    private fun setSongPosition(increment: Boolean) {
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

    private fun initializeSeekbar() {
        binding.seekBar.max = mediaPlayer!!.duration

        val handler = Handler()
        handler.postDelayed(object : Runnable {
            @SuppressLint("SuspiciousIndentation")
            override fun run() {
                try {
                    binding.seekBar.progress = mediaPlayer!!.currentPosition
                    handler.postDelayed(this, 1000)
                    val time = mediaPlayer!!.currentPosition
                    updateUI(time)
                    val totalDuratin = mediaPlayer!!.duration

                    val formatDuration = formatDuration(totalDuratin)
                    binding.endTime.text = formatDuration.toString()
                } catch (e: Exception) {
                    binding.seekBar.progress = 0
                }
            }
        }, 0)
    }

    fun updateUI(currentPosition: Int) {
        // Convert milliseconds to minutes and seconds if needed
        val minutes = (currentPosition / 1000) / 60
        val seconds = (currentPosition / 1000) % 60
        val timeString = String.format("%02d:%02d", minutes, seconds)

        // Update your UI component, e.g., TextView or SeekBar

        binding.timeStart.text = timeString
    }

    fun formatDuration(milliseconds: Int): String {
        val minutes = (milliseconds / 1000) / 60
        val seconds = (milliseconds / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}