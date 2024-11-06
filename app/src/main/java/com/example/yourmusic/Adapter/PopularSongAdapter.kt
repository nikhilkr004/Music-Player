package com.example.yourmusic.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.yourmusic.Activitys.PlayerActivity
import com.example.yourmusic.Model.SongModel
import com.example.yourmusic.MyExoPlayer
import com.example.yourmusic.databinding.MusicItemBinding
import com.example.yourmusic.databinding.SongItemBinding


class PopularSongAdapter(val songs:List<SongModel>): RecyclerView.Adapter<PopularSongAdapter.ViewHolder>() {
    class ViewHolder(val binding: MusicItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(song: SongModel) {
            val context=binding.root.context
            binding.songname.text=song.title.toString()

            Glide.with(context).load(song.image).into(binding.songCover)

            binding.root.setOnClickListener {
                MyExoPlayer.staryPlaying(context,song)
                it.context.startActivity(Intent(it.context, PlayerActivity::class.java))
            }
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
        p0.bind(song)
    }
}