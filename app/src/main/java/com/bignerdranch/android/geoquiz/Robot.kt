package com.bignerdranch.android.geoquiz

import android.health.connect.datatypes.units.Energy

private const val TAG = "Robot"
data class Robot(
    var myTurn      : Boolean,
    val largeImgRes : Int,
    val smallImgRes : Int,
)
