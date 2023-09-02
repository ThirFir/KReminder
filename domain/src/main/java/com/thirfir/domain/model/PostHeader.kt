package com.thirfir.domain.model

import android.os.Parcel
import android.os.Parcelable


data class PostHeader(
    val pid: Int,
    val title: String,
    val author: String,
    val date: String,
    val isTopFixed: Boolean,
    val category: String,
    val highlight: Boolean,
    val viewCount: Int
    // TODO : 데이터 정의
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(pid)
        parcel.writeString(title)
        parcel.writeString(author)
        parcel.writeString(date)
        parcel.writeByte(if (isTopFixed) 1 else 0)
        parcel.writeString(category)
        parcel.writeByte(if (highlight) 1 else 0)
        parcel.writeInt(viewCount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PostHeader> {
        override fun createFromParcel(parcel: Parcel): PostHeader {
            return PostHeader(parcel)
        }

        override fun newArray(size: Int): Array<PostHeader?> {
            return arrayOfNulls(size)
        }
    }

}
