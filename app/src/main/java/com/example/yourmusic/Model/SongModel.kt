package com.example.yourmusic.Model

import android.os.Parcel
import android.os.Parcelable

data class SongModel(
    val title:String?="null",
    val singer:String?="null",
    val url:String?="null",
    val image:String?="null",
    var uid:String?="null",
    val like:Int?=0
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(singer)
        parcel.writeString(url)
        parcel.writeString(image)
        parcel.writeString(uid)
        parcel.writeValue(like)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SongModel> {
        override fun createFromParcel(parcel: Parcel): SongModel {
            return SongModel(parcel)
        }

        override fun newArray(size: Int): Array<SongModel?> {
            return arrayOfNulls(size)
        }
    }
}