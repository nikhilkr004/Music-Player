package com.example.yourmusic

object Utils {

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


}