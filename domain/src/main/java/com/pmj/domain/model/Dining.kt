package com.pmj.domain.model

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep

@Keep
data class Dining(
    val offer: String? = null,
    val address: String? = null,
    val price: String? = null,
    val imageUrl: String? = null,
    val rating: Float? = null,
    val title: String? = null,
    val desc: String? = null,
    val timing: String? = null,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Float::class.java.classLoader) as? Float,
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(offer)
        parcel.writeString(address)
        parcel.writeString(price)
        parcel.writeString(imageUrl)
        parcel.writeValue(rating)
        parcel.writeString(title)
        parcel.writeString(desc)
        parcel.writeString(timing)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Dining> {
        override fun createFromParcel(parcel: Parcel): Dining {
            return Dining(parcel)
        }

        override fun newArray(size: Int): Array<Dining?> {
            return arrayOfNulls(size)
        }
    }
}

