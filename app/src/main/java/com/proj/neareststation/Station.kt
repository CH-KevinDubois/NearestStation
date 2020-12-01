package com.proj.neareststation

import android.graphics.Bitmap
import android.graphics.BitmapFactory

data class Station(
    val id: Int,
    val address: String,
    val distance: Int,
    val iconClass: String) {

}