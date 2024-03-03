package com.bignerdranch.android.geoquiz

import androidx.lifecycle.ViewModel

private const val TAG = "RobotViewModel"

class RobotViewModel : ViewModel() {

    private var _turnCount = 0
    var turnCount : Int
        get () = _turnCount
        set(value) {_turnCount = value}

}