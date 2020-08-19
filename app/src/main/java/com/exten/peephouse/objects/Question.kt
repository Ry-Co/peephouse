package com.exten.peephouse.objects

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Question(
    var id: String = "",
    val text: String = "",
    val yay_votes: Int = 0,
    val nay_votes: Int = 0
): Parcelable