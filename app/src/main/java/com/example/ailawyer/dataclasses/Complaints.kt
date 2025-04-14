package com.example.ailawyer.dataclasses

import android.os.Parcel
import android.os.Parcelable

// Data class for Complaint
data class Complaint(
    var title: String,
    var details: String,
    var state: String,
    var progress: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(details)
        parcel.writeString(state)
        parcel.writeString(progress)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Complaint> {
        override fun createFromParcel(parcel: Parcel): Complaint {
            return Complaint(parcel)
        }

        override fun newArray(size: Int): Array<Complaint?> {
            return arrayOfNulls(size)
        }
    }
}


