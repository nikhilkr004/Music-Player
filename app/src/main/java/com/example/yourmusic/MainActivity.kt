package com.example.yourmusic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.yourmusic.Activitys.EnglishActivity
import com.example.yourmusic.Activitys.HindiActivity
import com.example.yourmusic.Activitys.PlayerActivity
import com.example.yourmusic.Adapter.PopularSongAdapter
import com.example.yourmusic.Adapter.SongsAdapter
import com.example.yourmusic.Model.SongModel
import com.example.yourmusic.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private var exoPlayer: ExoPlayer? = null
    private lateinit var databaseReference: DatabaseReference
    private val songs = mutableListOf<SongModel>()
    private val hindi = mutableListOf<SongModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.english.setOnClickListener {
            val intent = Intent(this, EnglishActivity::class.java)
            startActivity(intent)
        }
        binding.hindi.setOnClickListener {
            val intent = Intent(this, HindiActivity::class.java)
            startActivity(intent)
        }


        databaseReference = FirebaseDatabase.getInstance().reference

        val recyclerView = binding.recyclerview
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val adapter = PopularSongAdapter(songs)
        recyclerView.adapter = adapter
        val musicRef = databaseReference.child("images")
        musicRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                songs.clear()

                for (Snapshot in p0.children) {
                    val data = Snapshot.getValue(SongModel::class.java)
                    if (data != null) {
                        songs.add(data)
                        songs.reverse()

                    }
                }

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


        val recycler = binding.df
        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val hindiAdapter = PopularSongAdapter(hindi)
        recycler.adapter = hindiAdapter
        val hindiRef = databaseReference.child("hindi")
        hindiRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                hindi.clear()

                for (Snapshot in p0.children) {
                    val data = Snapshot.getValue(SongModel::class.java)
                    if (data != null) {
                        hindi.add(data)



                        hindi.reverse()

                    }
                }

                hindiAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


    }

    override fun onResume() {
        super.onResume()
        showPlayerView()
    }


    fun showPlayerView() {
        MyExoPlayer.getCurrentSong()?.let {
            binding.customPlayer.setOnClickListener {
                val intent = Intent(this, PlayerActivity::class.java)
                startActivity(intent)
            }
            binding.customPlayer.visibility = View.VISIBLE
            binding.songTitle.text = it.title.toString()
            binding.textView8.text = it.singer.toString()
            Glide.with(this).load(it.image).circleCrop().into(binding.songCoverImage)
        } ?: run {
            binding.customPlayer.visibility = View.GONE
        }

        MyExoPlayer.getInstance()?.let { player: ExoPlayer ->
            exoPlayer = player
            AllFunction()
        }
    }

    fun AllFunction() {
        binding.imageView5.setOnClickListener {
            if (exoPlayer!!.isPlaying) pauseAudio()
            else playAudio()
        }
    }

    fun playAudio() {
        exoPlayer?.play()
        binding.imageView5.setImageResource(R.drawable.pause)
    }

    fun pauseAudio() {
        exoPlayer?.pause()
        binding.imageView5.setImageResource(R.drawable.play)
    }
}