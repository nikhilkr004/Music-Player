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
import com.example.yourmusic.databinding.SongItemBinding

class SongsAdapter(val songs:List<SongModel> ,val tage:String,val context: Context):RecyclerView.Adapter<SongsAdapter.ViewHolder>() {
    class ViewHolder(val binding:SongItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(song: SongModel, tage: String) {
            val context=binding.root.context
            binding.textView4.text=song.title.toString()
            binding.singer.text=song.singer.toString()
            Glide.with(context).load(song.image).circleCrop().into(binding.imageView3)
            // here  we pass the actul music
            binding.root.setOnClickListener {
                val intent=Intent(context,PlayerActivity::class.java)
                intent.putExtra("index",position)
                intent.putExtra("class",tage)
                ContextCompat.startActivity(context,intent,null)
            }

        }


    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
   val inflater=LayoutInflater.from(p0.context)
val binding=SongItemBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
     return songs.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
       val song=songs[p1]
        p0.bind(song,tage)


    }
}