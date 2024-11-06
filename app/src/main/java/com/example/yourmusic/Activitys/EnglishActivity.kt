package com.example.yourmusic.Activitys

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yourmusic.Adapter.SongsAdapter
import com.example.yourmusic.Model.SongModel
import com.example.yourmusic.R
import com.example.yourmusic.databinding.ActivityEnglishBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EnglishActivity : AppCompatActivity() {
 private val binding by lazy {
     ActivityEnglishBinding.inflate(layoutInflater)
 }
    private lateinit var databaseReference: DatabaseReference
    private  val songs= mutableListOf<SongModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        databaseReference=FirebaseDatabase.getInstance().reference

        val recyclerView=binding.englishRecycler
        recyclerView.layoutManager=LinearLayoutManager(this)
        val adapter=SongsAdapter(songs)
        recyclerView.adapter=adapter
        val  musicRef=databaseReference.child("images")
        musicRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                songs.clear()

                for (Snapshot in p0.children){
                    val data =Snapshot.getValue(SongModel::class.java)
                    if (data!=null){
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
    }
}