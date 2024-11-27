package com.example.yourmusic.Activitys

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.util.Util
import com.example.yourmusic.MainActivity
import com.example.yourmusic.Model.SongModel
import com.example.yourmusic.R
import com.example.yourmusic.Services.MusicService
import com.example.yourmusic.Utils
import com.example.yourmusic.databinding.ActivityPlayerBinding

import com.example.yourmusic.setSongPosition
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.core.utilities.Utilities
import kotlinx.coroutines.Runnable


class PlayerActivity : AppCompatActivity(),ServiceConnection, MediaPlayer.OnCompletionListener{


    companion object {
        var REQUEST_CODE = 1
        lateinit var musicListPA: ArrayList<SongModel>
        var songPosition: Int = 0
        var isPlay: Boolean = false
        var musicService:MusicService?=null
       lateinit var binding:ActivityPlayerBinding
       var repeat:Boolean=false
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /////////////////////////////////////

        initalizedLayout()
        //play pause btn
        binding.pausePlay.setOnClickListener {
            if (isPlay) pauseMusic()
            else playMusic()
        }

        /////back button////////////
        binding.imageView4.setOnClickListener { finish() }





        ////repeat
        binding.repeat!!.setOnClickListener {
            if (!repeat){
                repeat=true
                binding.repeat!!.setColorFilter(ContextCompat.getColor(this,R.color.green))
            }else{
                repeat=false
                binding.repeat!!.setColorFilter(ContextCompat.getColor(this,R.color.black))
            }
        }
        /// next and prev

        binding.nextBtn.setOnClickListener { prevNextSong(increment = true) }
        binding.previusBtn.setOnClickListener { prevNextSong(increment = false) }


    }

    private fun handleLike() {

        if (binding.imageView9.tag=="Like"){
            val image= musicListPA[songPosition].image
            val url= musicListPA[songPosition].url
            val uid= musicListPA[songPosition].uid
            val title= musicListPA[songPosition].title
            val singer= musicListPA[songPosition].singer
            val song= SongModel(
                image=image,
                title = title,
                singer = singer,
                url = url,
                uid = uid)
            FirebaseDatabase.getInstance().reference.child("Likes")
                .child(Utils.currentUserId())
                .child(musicListPA[songPosition].uid!!)
                .setValue(song).addOnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(this, "song liked!!", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        else{
            FirebaseDatabase.getInstance().reference.child("Likes")
                .child(Utils.currentUserId())
                .child(musicListPA[songPosition].uid!!)
                .removeValue()
            Toast.makeText(this, "song dislike!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkUserLikeOrNot() {
        val likeRef = FirebaseDatabase.getInstance().reference
            .child("Likes").child(Utils.currentUserId())

        likeRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(musicListPA[songPosition].uid!!).exists()) {
                    binding.imageView9.setImageResource(R.drawable.check)
                    binding.imageView9.tag = "Liked"
                } else {
                    binding.imageView9.setImageResource(R.drawable.add)
                    binding.imageView9.tag = "Like"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }




    private fun setLayout() {
        Glide.with(this).load(musicListPA[songPosition].image).into(binding.musicImage)
        binding.songName.text = musicListPA[songPosition].title.toString()
        binding.singerName.text = musicListPA[songPosition].singer.toString()
        if (repeat)  binding.repeat!!.setColorFilter(ContextCompat.getColor(this,R.color.green))

    }

     fun createMediaPlayer() {
        // in this function create media

        try {
            if (musicService!!.mediaPlayer == null) musicService!!.mediaPlayer = MediaPlayer()
            musicService!!.mediaPlayer!!.reset()
            musicService!!.mediaPlayer!!.setDataSource(musicListPA[songPosition].url)
            musicService!!.mediaPlayer!!.prepare()
            musicService!!.mediaPlayer!!.start()
            musicService!!.showNotificatin(R.drawable.pause)

            ////////////
            checkUserLikeOrNot()
            ///this is for then song coplete then play next song
            musicService!!.mediaPlayer!!.setOnCompletionListener(this)
            initializeSeekbar()

            binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser)  musicService!!.mediaPlayer!!.seekTo(progress)
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
                //background services
                val intent=Intent(this,MusicService::class.java)
                bindService(intent,this, BIND_AUTO_CREATE)
                startService(intent)

                musicListPA = ArrayList()
                musicListPA!!.addAll(MainActivity.hindi)
                setLayout()

            }

            "english" -> {
                //background services
                val intent=Intent(this,MusicService::class.java)
                bindService(intent,this, BIND_AUTO_CREATE)
                startService(intent)

                musicListPA = ArrayList()
                musicListPA!!.addAll(MainActivity.songs)
                setLayout()

            }

            "PopularAdapter" -> {
                //background services
                val intent=Intent(this,MusicService::class.java)
                bindService(intent,this, BIND_AUTO_CREATE)
                startService(intent)

                musicListPA = ArrayList()
                musicListPA!!.addAll(MainActivity.hindi)
                setLayout()
            }

            "likedSong" -> {
                //background services
                val intent=Intent(this,MusicService::class.java)
                bindService(intent,this, BIND_AUTO_CREATE)
                startService(intent)

                musicListPA = ArrayList()
                musicListPA!!.addAll(LikedSongActivity.likedSong)
                setLayout()
            }
            "NowPlaying" -> {
                binding.seekBar.progress= musicService!!.mediaPlayer!!.currentPosition
                binding.seekBar.max= musicService!!.mediaPlayer!!.duration
                binding.timeStart.text=formatDuration(musicService!!.mediaPlayer!!.currentPosition)
                binding.endTime.text=formatDuration(musicService!!.mediaPlayer!!.duration)
             setLayout()
            }
        }

    }

    private fun playMusic() {
        binding.pausePlay.setImageResource(R.drawable.pause)
        musicService!!.showNotificatin(R.drawable.pause)
        isPlay = true
        musicService!!.mediaPlayer!!.start()
    }

    private fun pauseMusic() {
        binding.pausePlay.setImageResource(R.drawable.play)
        musicService!!.showNotificatin(R.drawable.play)
        isPlay = false
        musicService!!.mediaPlayer!!.pause()
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



    private fun initializeSeekbar() {
        binding.seekBar.max =  musicService!!.mediaPlayer!!.duration

        val handler = Handler()
        handler.postDelayed(object : Runnable {
            @SuppressLint("SuspiciousIndentation")
            override fun run() {
                try {
                    binding.seekBar.progress =  musicService!!.mediaPlayer!!.currentPosition
                    handler.postDelayed(this, 1000)
                    val time =  musicService!!.mediaPlayer!!.currentPosition
                    updateUI(time)
                    val totalDuratin =  musicService!!.mediaPlayer!!.duration

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

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        /// it means background service is called then what we want to do

        val binder=service as MusicService.MyBinder
        musicService=binder.currentService()

        createMediaPlayer()


        binding.imageView9.setOnClickListener {
            handleLike()
        }
    }

    ///when service is

    override fun onServiceDisconnected(name: ComponentName?) {
        ///when service id disconnect then what we want to do
        musicService=null
    }

    override fun onCompletion(mp: MediaPlayer?) {
        setSongPosition(true)
        createMediaPlayer()
        setLayout()
    }
}



