package com.example.yourmusic.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.yourmusic.Activitys.PlayerActivity
import com.example.yourmusic.Activitys.PlayerActivity.Companion
import com.example.yourmusic.Activitys.PlayerActivity.Companion.musicListPA
import com.example.yourmusic.Activitys.PlayerActivity.Companion.songPosition
import com.example.yourmusic.R
import com.example.yourmusic.databinding.FragmentNowPlayingBinding


class NowPlaying : Fragment() {
companion object{
    lateinit var binding: FragmentNowPlayingBinding
}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_now_playing, container, false)
        binding=FragmentNowPlayingBinding.bind(view)
        binding.root.visibility=View.INVISIBLE

        binding.playPause.setOnClickListener {
            if (PlayerActivity.isPlay)pauseMusic() else playMusic()
        }

        binding.root.setOnClickListener {
            val intent=Intent(requireContext(),PlayerActivity::class.java)
            intent.putExtra("index",PlayerActivity.songPosition)
            intent.putExtra("class","NowPlaying")
            ContextCompat.startActivity(requireContext(),intent,null)
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        if (PlayerActivity.musicService!=null){
            binding.root.visibility=View.VISIBLE
            Glide.with(this).load(musicListPA[songPosition].image).into(binding.songCoverImage)
            binding.songTitle.text = musicListPA[songPosition].title.toString()
            binding.textView8.text = musicListPA[songPosition].singer.toString()

            if (PlayerActivity.isPlay) binding.playPause.setImageResource(R.drawable.pause)
            else binding.playPause.setImageResource(R.drawable.play)
        }

    }
    private fun playMusic(){
        PlayerActivity.musicService!!.mediaPlayer!!.start()
        binding.playPause.setImageResource(R.drawable.pause)
        PlayerActivity!!.musicService!!.showNotificatin(R.drawable.pause)
        PlayerActivity.binding.pausePlay.setImageResource(R.drawable.pause)
        PlayerActivity.isPlay=true
    }

    private fun pauseMusic(){
        PlayerActivity.musicService!!.mediaPlayer!!.pause()
        binding.playPause.setImageResource(R.drawable.play)
        PlayerActivity!!.musicService!!.showNotificatin(R.drawable.play)
        PlayerActivity.binding.pausePlay.setImageResource(R.drawable.play)
        PlayerActivity.isPlay=false
    }
}