package com.example.yourmusic.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.yourmusic.Activitys.PlayerActivity
import com.example.yourmusic.Model.SongModel
import com.example.yourmusic.databinding.MusicItemBinding



class PopularSongAdapter(val songs:List<SongModel>,val tage:String,val context: Context): RecyclerView.Adapter<PopularSongAdapter.ViewHolder>() {
    class ViewHolder(val binding: MusicItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(song: SongModel, tage: String) {
            val context=binding.root.context
            binding.songname.text=song.title.toString()

            Glide.with(context).load(song.image).into(binding.songCover)


        }


    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val inflater= LayoutInflater.from(p0.context)
        val binding= MusicItemBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val song=songs[p1]
        p0.bind(song,tage)

        p0.binding.root.setOnClickListener {
            val intent=Intent(context,PlayerActivity::class.java)
            intent.putExtra("index",p1)
            intent.putExtra("class",tage)
            ContextCompat.startActivity(context,intent,null)

        }
    }
}