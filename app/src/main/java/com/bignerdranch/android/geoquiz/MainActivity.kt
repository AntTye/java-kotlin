package com.bignerdranch.android.geoquiz

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.bignerdranch.android.geoquiz.databinding.ActivityMainBinding

private const val TAG = "MainActivity"
private const val EXTRA_ROBOT_ENERGY_NEW_RED = "com.bignerdranch.android.geoquiz.EXTRA_ROBOT_ENERGY_NEW_RED"
private const val EXTRA_ROBOT_ENERGY_NEW_WHITE = "com.bignerdranch.android.geoquiz.EXTRA_ROBOT_ENERGY_NEW_WHITE"
private const val EXTRA_ROBOT_ENERGY_NEW_YELLOW = "com.bignerdranch.android.geoquiz.EXTRA_ROBOT_ENERGY_NEW_YELLOW"
private const val EXTRA_ROBOT_ENERGY_NEW_TURN = "com.bignerdranch.android.geoquiz.EXTRA_ROBOT_ENERGY_NEW_TURN"
private const val EXTRA_ROBOT_PURCHASED_REWARDS = "com.bignerdranch.android.geoquiz.EXTRA_ROBOT_PURCHASED_REWARDS"

class MainActivity : ComponentActivity(){

    private lateinit var red_img    : ImageView
    private lateinit var white_img  : ImageView
    private lateinit var yellow_img : ImageView
    private lateinit var rot_clock  : ImageView
    private lateinit var rot_counter: ImageView

    private var newRobotEnergy_Red : Int = 0
    private var newRobotEnergy_White : Int = 0
    private var newRobotEnergy_Yellow : Int = 0
    private var newRobotTurn : Int = 0

    private lateinit var newPurchasedRewards : MutableList<Int>

    private lateinit var robotImages: MutableList<ImageView>

    private lateinit var message_box: TextView
    private lateinit var purchaseBox:TextView

    private val robots = listOf(
        Robot(false, R.drawable.robot_red_large, R.drawable.robot_red_small),
        Robot(false, R.drawable.robot_white_large, R.drawable.robot_white_small),
        Robot(false, R.drawable.robot_yellow_large, R.drawable.robot_yellow_small)
    )
    private val messageResources = listOf(
        R.string.robot_red_large,
        R.string.robot_white_large,
        R.string.robot_yellow_large
    )
    private val robotViewModel : RobotViewModel by viewModels()

    companion object {
        fun newIntent(packageContext: Context,
                      newRobotEnergy_Red : Int, newRobotEnergy_White : Int,newRobotEnergy_Yellow : Int,
                      newRobotTurn : Int, purchaseRewards : MutableList<Int>): Intent {
            return Intent(packageContext, MainActivity::class.java).apply {
                putExtra(EXTRA_ROBOT_ENERGY_NEW_RED, newRobotEnergy_Red)
                putExtra(EXTRA_ROBOT_ENERGY_NEW_WHITE, newRobotEnergy_White)
                putExtra(EXTRA_ROBOT_ENERGY_NEW_YELLOW, newRobotEnergy_Yellow)
                putExtra(EXTRA_ROBOT_ENERGY_NEW_TURN, newRobotTurn)
                putIntegerArrayListExtra(EXTRA_ROBOT_PURCHASED_REWARDS, ArrayList(purchaseRewards))
            }
        }
    }

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "got a robotViewModel @: $robotViewModel")

        red_img   = findViewById(R.id.robot_red_large)
        white_img = findViewById(R.id.robot_white_large)
        yellow_img= findViewById(R.id.robot_yellow_large)
        rot_clock = findViewById(R.id.rot_clock)
        rot_counter=findViewById(R.id.rot_counter)
        robotImages = mutableListOf(red_img, white_img, yellow_img)

        message_box=findViewById (R.id.robot_turn)
        purchaseBox=findViewById(R.id.make_purchase)

        newRobotEnergy_Red = intent.getIntExtra(EXTRA_ROBOT_ENERGY_NEW_RED, 0)
        newRobotEnergy_White = intent.getIntExtra(EXTRA_ROBOT_ENERGY_NEW_WHITE, 0)
        newRobotEnergy_Yellow = intent.getIntExtra(EXTRA_ROBOT_ENERGY_NEW_YELLOW, 0)
        newRobotTurn = intent.getIntExtra(EXTRA_ROBOT_ENERGY_NEW_TURN, 0)
        newPurchasedRewards = intent.getIntegerArrayListExtra(EXTRA_ROBOT_PURCHASED_REWARDS)?.toMutableList() ?: mutableListOf()

        robotViewModel.redEnergy = intent.getIntExtra(EXTRA_ROBOT_ENERGY_NEW_RED, 0)
        robotViewModel.whiteEnergy = intent.getIntExtra(EXTRA_ROBOT_ENERGY_NEW_WHITE, 0)
        robotViewModel.yellowEnergy = intent.getIntExtra(EXTRA_ROBOT_ENERGY_NEW_YELLOW, 0)
        robotViewModel.turnCount = intent.getIntExtra(EXTRA_ROBOT_ENERGY_NEW_TURN, 0)
        robotViewModel.purchasedRewards = intent.getIntegerArrayListExtra(EXTRA_ROBOT_PURCHASED_REWARDS)?.toMutableList() ?: mutableListOf()

        red_img.setOnClickListener { view : View ->
            Toast.makeText(this, "Red Energy: ${robotViewModel.redEnergy}", Toast.LENGTH_SHORT).show()
        }
        white_img.setOnClickListener { view : View ->
            Toast.makeText(this, "White Energy: ${robotViewModel.whiteEnergy}", Toast.LENGTH_SHORT).show()
        }
        yellow_img.setOnClickListener { view : View ->
            Toast.makeText(this, "Yellow Energy: ${robotViewModel.yellowEnergy}", Toast.LENGTH_SHORT).show()
        }

        rot_clock.setOnClickListener    { advanceTurn(true) }
        rot_counter.setOnClickListener  { advanceTurn(false) }

        purchaseBox.setOnClickListener {
            if (robotViewModel.turnCount ==1) {
                val intent = RobotPurchaseActivity.newIntent(this, robotViewModel.redEnergy, robotViewModel.whiteEnergy, robotViewModel.yellowEnergy, robotViewModel.turnCount, robotViewModel.purchasedRewards)
                startActivity(intent)

            }else if (robotViewModel.turnCount ==2){
                val intent = RobotPurchaseActivity.newIntent(this, robotViewModel.redEnergy, robotViewModel.whiteEnergy, robotViewModel.yellowEnergy, robotViewModel.turnCount, robotViewModel.purchasedRewards)
                startActivity(intent)

            }else if(robotViewModel.turnCount ==3){
                val intent = RobotPurchaseActivity.newIntent(this, robotViewModel.redEnergy, robotViewModel.whiteEnergy, robotViewModel.yellowEnergy, robotViewModel.turnCount, robotViewModel.purchasedRewards)
                startActivity(intent)

            }else{
                robotViewModel.redEnergy = newRobotEnergy_Red
                val intent = RobotPurchaseActivity.newIntent(this, robotViewModel.redEnergy, robotViewModel.whiteEnergy, robotViewModel.yellowEnergy, robotViewModel.turnCount, robotViewModel.purchasedRewards)
                startActivity(intent)
            }
        }

        setRobotTurn()
        setImages()


        if (savedInstanceState != null) {
            val savedCount = savedInstanceState.getInt("turnCount", 0)
            robotViewModel.turnCount = savedCount
        }
    }

    private fun advanceTurn(clockwise: Boolean) {
        if (robotViewModel.turnCount ==0){
            robotViewModel.turnCount = 1
        }
        val increment = if (clockwise) -1 else 1
        robotViewModel.turnCount = (robotViewModel.turnCount + increment + 2) % 3 + 1

        if (robotViewModel.turnCount == 1){
            robotViewModel.redEnergy++
        }else if (robotViewModel.turnCount == 2){
            robotViewModel.whiteEnergy++
        }else if (robotViewModel.turnCount == 3) {
            robotViewModel.yellowEnergy++
        }

        setRobotTurn()
        setImages()
    }

    private fun setRobotTurn(){
        if (robotViewModel.turnCount ==0) {
            for (robot in robots) {
                robot.myTurn = false
            }
            robots[robotViewModel.turnCount].myTurn = true
            robotViewModel.redEnergy ++
        }else{
            for(robot in robots){
                robot.myTurn = false
            }
            robots[robotViewModel.turnCount-1].myTurn = true
        }
    }



    private fun setImages() {
        var indy = 0
        while (indy < robotImages.size && indy < robots.size) {
            if (robots[indy].myTurn) {
                robotImages[indy].setImageResource(robots[indy].largeImgRes)
                if (indy < messageResources.size) {
                    message_box.setText(messageResources[indy])
                }
            } else {
                robotImages[indy].setImageResource(robots[indy].smallImgRes)
            }
            indy++
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG,"onStop() entered")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG,"onResume() entered")
    }
    override fun onPause() {
        super.onPause()
        Log.d(TAG,"onPause() entered")
    }
    override fun onStop() {
        super.onStop()
        Log.d(TAG,"onStop() entered")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"onDestory() entered")
    }
    override fun onRestart() {
        super.onRestart()
        Log.d(TAG,"onRestart()entered")
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("turnCount", robotViewModel.turnCount)
        outState.putInt("redEnergy", robotViewModel.redEnergy)
        outState.putInt("whiteEnergy", robotViewModel.whiteEnergy)
        outState.putInt("yellowEnergy", robotViewModel.yellowEnergy)
        outState.putIntegerArrayList("purchasedRewards", ArrayList(robotViewModel.purchasedRewards))
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        robotViewModel.turnCount = savedInstanceState.getInt("turnCount", 0)
        robotViewModel.redEnergy = savedInstanceState.getInt("redEnergy", 0)
        robotViewModel.whiteEnergy = savedInstanceState.getInt("whiteEnergy", 0)
        robotViewModel.yellowEnergy = savedInstanceState.getInt("yellowEnergy", 0)
        robotViewModel.purchasedRewards =
            savedInstanceState.getIntegerArrayList("purchasedRewards")?.toMutableList() ?: mutableListOf()

        setRobotTurn()
        setImages()
    }

}
