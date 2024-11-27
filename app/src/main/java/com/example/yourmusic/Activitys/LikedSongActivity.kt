package com.example.yourmusic.Activitys

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yourmusic.Adapter.PopularSongAdapter
import com.example.yourmusic.Adapter.SongsAdapter
import com.example.yourmusic.MainActivity.Companion.songs
import com.example.yourmusic.Model.SongModel
import com.example.yourmusic.R
import com.example.yourmusic.Utils
import com.example.yourmusic.databinding.ActivityLikedSongBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LikedSongActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityLikedSongBinding.inflate(layoutInflater)
    }
    lateinit var databaseReference:DatabaseReference
    companion object{
        lateinit var likedSong:ArrayList<SongModel>
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        /// to finish and end the activity

        binding

        likedSong= ArrayList()
        databaseReference=FirebaseDatabase.getInstance().reference

        val recyclerView = binding.likesRecyclerview
        recyclerView.layoutManager =
            LinearLayoutManager(this)
        val adapter = SongsAdapter(likedSong,"likedSong",this)
        recyclerView.adapter = adapter
        val musicRef = databaseReference.child("Likes").child(Utils.currentUserId())
        musicRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                likedSong.clear()

                for (Snapshot in p0.children) {
                    val data = Snapshot.getValue(SongModel::class.java)
                    if (data != null) {
                        likedSong.add(data)
                        likedSong.reverse()

                    }
                }

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }
}