package com.example.yourmusic

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.example.yourmusic.databinding.ProgressDialogBinding
import com.google.firebase.auth.FirebaseAuth

object Utils {
    private val auth= FirebaseAuth.getInstance()
    private var dialog:AlertDialog?=null
        fun milliSecondsToTimer(milliseconds: Long): String {
            val seconds = (milliseconds / 1000) % 60
            val minutes = (milliseconds / (1000 * 60) % 60)
            val hours = (milliseconds / (1000 * 60 * 60) % 24)

            return if (hours > 0) {
                String.format("%02d:%02d:%02d", hours, minutes, seconds)
            } else {
                String.format("%02d:%02d", minutes, seconds)
            }
        }
    fun getProgressPercentage(currentDuration: Long, totalDuration: Long): Int {
        var percentage = 0.0

        val currentSeconds = (currentDuration / 1000).toInt().toLong()
        val totalSeconds = (totalDuration / 1000).toInt().toLong()


        // calculating percentage
        percentage = ((currentSeconds.toDouble()) / totalSeconds) * 100


        // return percentage
        return percentage.toInt()
    }

    fun currentUserId(): String {
        return auth.currentUser!!.uid
    }

    fun showDialog(context: Context, message:String){
        val process= ProgressDialogBinding.inflate(LayoutInflater.from(context))
        process.text.text=message.toString()

        dialog= AlertDialog.Builder(context).setView(process.root).setCancelable(false).create()
        dialog!!.show()
    }

    fun hideDialog(){
        dialog!!.dismiss()
    }

}