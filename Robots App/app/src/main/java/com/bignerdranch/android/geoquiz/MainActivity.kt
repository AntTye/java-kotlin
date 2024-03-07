package com.bignerdranch.android.geoquiz

// Necessary Imports

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

// Used for logging or identifying logs related to the MainActivity
private const val TAG = "MainActivity"

// EXTRA's used to save data from one Activity to the other, relies on our package in addition to EXTRA's
private const val EXTRA_ROBOT_ENERGY_NEW_RED = "com.bignerdranch.android.geoquiz.EXTRA_ROBOT_ENERGY_NEW_RED"
private const val EXTRA_ROBOT_ENERGY_NEW_WHITE = "com.bignerdranch.android.geoquiz.EXTRA_ROBOT_ENERGY_NEW_WHITE"
private const val EXTRA_ROBOT_ENERGY_NEW_YELLOW = "com.bignerdranch.android.geoquiz.EXTRA_ROBOT_ENERGY_NEW_YELLOW"
private const val EXTRA_ROBOT_ENERGY_NEW_TURN = "com.bignerdranch.android.geoquiz.EXTRA_ROBOT_ENERGY_NEW_TURN"
private const val EXTRA_ROBOT_PURCHASED_REWARDS = "com.bignerdranch.android.geoquiz.EXTRA_ROBOT_PURCHASED_REWARDS"

class MainActivity : ComponentActivity(){

    // Initializes activity_main.xml variables, allows for mutability (Lines 31 - 61)
    // Creates private Views and lists
    private lateinit var red_img            : ImageView
    private lateinit var white_img          : ImageView
    private lateinit var yellow_img         : ImageView
    private lateinit var rot_clock          : ImageView
    private lateinit var rot_counter        : ImageView

    private var newRobotEnergy_Red          : Int = 0
    private var newRobotEnergy_White        : Int = 0
    private var newRobotEnergy_Yellow       : Int = 0
    private var newRobotTurn                : Int = 0

    private lateinit var newPurchasedRewards: MutableList<Int>

    private lateinit var robotImages        : MutableList<ImageView>

    private lateinit var message_box        : TextView
    private lateinit var purchaseBox        : TextView
    private lateinit var reset_all          : TextView
    private lateinit var reset_purchase     : TextView

    // Creates robots list which sets the robot class with its respective attributes
    private val robots = listOf(
        Robot(false, R.drawable.robot_red_large, R.drawable.robot_red_small),
        Robot(false, R.drawable.robot_white_large, R.drawable.robot_white_small),
        Robot(false, R.drawable.robot_yellow_large, R.drawable.robot_yellow_small))

    // Essentially sets the title to robots turn, we are able to index in order to find the right "string"
    private val messageResources = listOf(
        R.string.robot_red_large,
        R.string.robot_white_large,
        R.string.robot_yellow_large)

    // Creates private value(s) associated in robotViewModel for this Activity, separate from RobotPurchaseActivity until data linking
    // Allows us to access values/data in RobotViewModel's class
    private val robotViewModel : RobotViewModel by viewModels()

    // Creates companion object that is public for newIntents. Takes specific values passed from RobotPurchaseActivity as EXTRAS
    // Int's just need regular initiation. Arrays needs null checker
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

    // View Binding provides direct, type-safe access to views in XML layout files, such as activity_main.xml layout file
    private lateinit var binding: ActivityMainBinding

    // Creates our Bundle, Includes all assets in activity_main
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "(Created) got a robotViewModel @: $robotViewModel")

        red_img     = findViewById(R.id.robot_red_large)
        white_img   = findViewById(R.id.robot_white_large)
        yellow_img  = findViewById(R.id.robot_yellow_large)
        rot_clock   = findViewById(R.id.rot_clock)
        rot_counter = findViewById(R.id.rot_counter)
        robotImages = mutableListOf(red_img, white_img, yellow_img)

        message_box = findViewById (R.id.robot_turn)
        purchaseBox = findViewById(R.id.make_purchase)

        reset_purchase  = findViewById(R.id.reset_rewards)
        reset_all       = findViewById(R.id.reset_all)

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

        // Clicking on Images makes Toast pop ups displaying its energy
        red_img.setOnClickListener { view : View ->
            Toast.makeText(this, "Red Energy: ${robotViewModel.redEnergy}", Toast.LENGTH_SHORT).show() }
        white_img.setOnClickListener { view : View ->
            Toast.makeText(this, "White Energy: ${robotViewModel.whiteEnergy}", Toast.LENGTH_SHORT).show() }
        yellow_img.setOnClickListener { view : View ->
            Toast.makeText(this, "Yellow Energy: ${robotViewModel.yellowEnergy}", Toast.LENGTH_SHORT).show() }

        // Clicking on the rotation images, advance turn by -1 or +1, clockwise, counter clockwise
        rot_clock.setOnClickListener    { advanceTurn(true) }
        rot_counter.setOnClickListener  { advanceTurn(false) }

        // Clicking on the purchaseBox sends us to the new Activity with values present in robotViewModel. StartActivity(intent) sends us to new Activity
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

        // Clicking on reset buttons will reset values in robotViewModel Accordingly, everything or only the purchased rewards list
        reset_all.setOnClickListener { resetValues(0)
            Toast.makeText(this, "Everything have been reset", Toast.LENGTH_SHORT).show()}
        reset_purchase.setOnClickListener { resetValues(1)
            Toast.makeText(this, "Purchased Rewards have been reset", Toast.LENGTH_SHORT).show()}

        //Initial Set Up Conditions for starting application
        if (robotViewModel.turnCount == 0){
            resetValues(0) }
        setRobotTurn()
        setImages()

        // Checks if there's saved instance state data associated with the activity
        // Retrieves an integer "turnCount" from state bundle and assigns it to turnCount
        // For configuration changes, screen rotations, to preserve data (turnCount)
        if (savedInstanceState != null) {
            val savedCount = savedInstanceState.getInt("turnCount", 0)
            robotViewModel.turnCount = savedCount
        }
    }

    // Advances our turn with a boolean, can also be int. Initial check for turns ! in 1,2,3
    private fun advanceTurn(clockwise: Boolean) {
        if (robotViewModel.turnCount ==0){
            robotViewModel.turnCount = 1
        }
        // Value increment passed through respective turnCount robots, modulo ensures index remains in range
        val increment = if (clockwise) -1 else 1
        robotViewModel.turnCount = (robotViewModel.turnCount + increment + 2) % 3 + 1
        // Increments energy value of robotViewModel according to turn
        if (robotViewModel.turnCount == 1){
            robotViewModel.redEnergy++
        }else if (robotViewModel.turnCount == 2){
            robotViewModel.whiteEnergy++
        }else if (robotViewModel.turnCount == 3) {
            robotViewModel.yellowEnergy++
        }
        // Set new parameters/values
        setRobotTurn()
        setImages()
    }

    // Sets which robots turn is it. Comes before setImages in order to see which turn. TurnCount initial check for first if statement
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

    // Sets the image for robot
    // Sets large version for robot if true size value in Robot class else smaller version
    // Sets title as respective robot Turn(s)
    private fun setImages() {
        var indy = 0
        // Size of image
        while (indy < robotImages.size && indy < robots.size) {
            if (robots[indy].myTurn) {
                robotImages[indy].setImageResource(robots[indy].largeImgRes)
                // Sets title
                if (indy < messageResources.size) {
                    message_box.setText(messageResources[indy])
                }
            } else {
                robotImages[indy].setImageResource(robots[indy].smallImgRes)
            }
            indy++
        }
    }

    // Resets certain values in robotViewModel depending on parameter: resets : Int
    private fun resetValues(resets: Int) {
        if (resets == 0){
            robotViewModel.turnCount = 1
            robotViewModel.redEnergy = 1
            robotViewModel.whiteEnergy = 0
            robotViewModel.yellowEnergy = 0
            robotViewModel.purchasedRewards = mutableListOf<Int>()

            setRobotTurn()
            setImages()
        } else if (resets == 1) {
            robotViewModel.purchasedRewards = mutableListOf<Int>()
            setRobotTurn()
            setImages()
        }
    }

    // Activity lifecycle logging. All inclusive
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

    // Saves values present in robotViewModel if we rotate app. Data is preserved
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("turnCount", robotViewModel.turnCount)
        outState.putInt("redEnergy", robotViewModel.redEnergy)
        outState.putInt("whiteEnergy", robotViewModel.whiteEnergy)
        outState.putInt("yellowEnergy", robotViewModel.yellowEnergy)
        outState.putIntegerArrayList("purchasedRewards", ArrayList(robotViewModel.purchasedRewards))
    }

    // Retrieves values/data present in previous instance. Sets robotViewModel values and sets the Activity to reflect saved values
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
