package com.bignerdranch.android.geoquiz

import androidx.lifecycle.ViewModel

private const val TAG = "RobotViewModel"

class RobotViewModel : ViewModel() {

    private var _turnCount = 0
    var turnCount: Int
        get() = _turnCount
        set(value) {
            _turnCount = value
        }

    private var _redEnergy = 0
    var redEnergy: Int
        get() = _redEnergy
        set(value) {
            _redEnergy = value
        }

    private var _whiteEnergy = 0
    var whiteEnergy: Int
        get() = _whiteEnergy
        set(value) {
            _whiteEnergy = value
        }

    private var _yellowEnergy = 0
    var yellowEnergy: Int
        get() = _yellowEnergy
        set(value) {
            _yellowEnergy = value
        }

    private var _purchasedRewards = mutableListOf<Int>()
    var purchasedRewards: MutableList<Int>
        get() = _purchasedRewards
        set(value) {
            _purchasedRewards = value
        }
}