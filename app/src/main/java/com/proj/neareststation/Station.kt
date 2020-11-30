package com.proj.neareststation

import android.graphics.Bitmap
import android.graphics.BitmapFactory

data class Station(
    val id: Int,
    val address: String,
    val distance: Int,
    val iconClass: String) {

    /*public val bitmap : Bitmap? = BitmapFactory.decodeFile("C:\\Users\\Kevin\\" +
                "AndroidStudioProjects\\NearestStation" +
                "\\app\\src\\main\\res\\drawable\\adr.png")*/
}