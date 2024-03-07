package com.bignerdranch.android.geoquiz

//Necessary Imports
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity

// Used for logging states from the Activity lifecycle
private const val TAG = "RobotPurchaseActivity"

// EXTRA's used to save data from one Activity to the other, relies on our package in addition to EXTRA's
private const val EXTRA_ROBOT_ENERGY_RED = "com.bignerdranch.android.geoquiz.EXTRA_ROBOT_ENERGY_RED"
private const val EXTRA_ROBOT_ENERGY_WHITE = "com.bignerdranch.android.geoquiz.EXTRA_ROBOT_ENERGY_WHITE"
private const val EXTRA_ROBOT_ENERGY_YELLOW = "com.bignerdranch.android.geoquiz.EXTRA_ROBOT_ENERGY_YELLOW"
private const val EXTRA_ROBOT_TURN = "com.bignerdranch.android.geoquiz.EXTRA_ROBOT_TURN"
private const val EXTRA_PURCHASED_REWARDS = "com.bignerdranch.android.geoquiz.EXTRA_PURCHASED_REWARDS"

class RobotPurchaseActivity : ComponentActivity() {

    // Initializes robot_purchase.xml variables, allows for mutability (Lines 27 - 43)
    // Creates private Views and lists
    private lateinit var energy_robot :     ImageView
    private lateinit var energy_amount :    TextView
    private lateinit var reward_a :         TextView
    private lateinit var reward_b :         TextView
    private lateinit var reward_c :         TextView
    private lateinit var reward_d :         TextView
    private lateinit var reward_e :         TextView
    private lateinit var reward_f :         TextView
    private lateinit var reward_g :         TextView
    private lateinit var random_reward:     TextView

    // Creates values our companion object return/spit spit out.
    private var robotTurn:          Int = 0
    private var robotEnergy_Red:    Int = 0
    private var robotEnergy_White:  Int = 0
    private var robotEnergy_Yellow: Int = 0
    private lateinit var purchasedRewards : MutableList<Int>

    // Companion object retrieves data given as parameters. Such as robotViewModel attributes
    // Puts values from parameters of newIntent into the EXTRAS
    companion object {
        fun newIntent(packageContext: Context, robotEnergy_Red : Int, robotEnergy_White : Int, robotEnergy_Yellow : Int, robotTurn: Int, purchasedRewards : MutableList<Int>): Intent {
            return Intent(packageContext, RobotPurchaseActivity::class.java).apply {
                putExtra(EXTRA_ROBOT_ENERGY_RED, robotEnergy_Red)
                putExtra(EXTRA_ROBOT_ENERGY_WHITE, robotEnergy_White)
                putExtra(EXTRA_ROBOT_ENERGY_YELLOW, robotEnergy_Yellow)
                putExtra(EXTRA_ROBOT_TURN, robotTurn)
                putIntegerArrayListExtra(EXTRA_PURCHASED_REWARDS, ArrayList(purchasedRewards))
            }
        }
    }

    // Starts our RobotPurchaseActivity onCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.robot_purchase)

        energy_robot    = findViewById(R.id.energy_robot)
        energy_amount   = findViewById(R.id.energy_amount)
        reward_a        = findViewById(R.id.reward_a)
        reward_b        = findViewById(R.id.reward_b)
        reward_c        = findViewById(R.id.reward_c)
        reward_d        = findViewById(R.id.reward_d)
        reward_e        = findViewById(R.id.reward_e)
        reward_f        = findViewById(R.id.reward_f)
        reward_g        = findViewById(R.id.reward_g)
        random_reward   = findViewById(R.id.random_reward)

        // Retrieved values/list from Main Activity
        robotTurn =             intent.getIntExtra(EXTRA_ROBOT_TURN, 0)
        robotEnergy_Red =       intent.getIntExtra(EXTRA_ROBOT_ENERGY_RED, 0)
        robotEnergy_White =     intent.getIntExtra(EXTRA_ROBOT_ENERGY_WHITE, 0)
        robotEnergy_Yellow =    intent.getIntExtra(EXTRA_ROBOT_ENERGY_YELLOW, 0)
        purchasedRewards =      intent.getIntegerArrayListExtra(EXTRA_PURCHASED_REWARDS)?.toMutableList() ?: mutableListOf()

        // Sets our robot assets and energy.text
        setRobotTurn()

        // Clicking each reward textView will make a purchase of its parameters and takes its index in the list of rewards
        // Sends us back to Main Activity with altered values, in main we will set robotViewModel attributes accordingly, not in here
        reward_a.setOnClickListener     {
            makePurchase(1, 0)
            val intent = MainActivity.newIntent(this, robotEnergy_Red, robotEnergy_White, robotEnergy_Yellow, robotTurn, purchasedRewards)
            startActivity(intent)
        }
        reward_b.setOnClickListener     {
            makePurchase(2, 1)
            val intent = MainActivity.newIntent(this, robotEnergy_Red, robotEnergy_White, robotEnergy_Yellow, robotTurn, purchasedRewards)
            startActivity(intent)
        }
        reward_c.setOnClickListener     {
            makePurchase(3, 2)
            val intent = MainActivity.newIntent(this, robotEnergy_Red, robotEnergy_White, robotEnergy_Yellow, robotTurn, purchasedRewards)
            startActivity(intent)
        }
        reward_d.setOnClickListener     {
            makePurchase(3, 3)
            val intent = MainActivity.newIntent(this, robotEnergy_Red, robotEnergy_White, robotEnergy_Yellow, robotTurn, purchasedRewards)
            startActivity(intent)
        }
        reward_e.setOnClickListener     {
            makePurchase(4,4)
            val intent = MainActivity.newIntent(this, robotEnergy_Red, robotEnergy_White, robotEnergy_Yellow, robotTurn, purchasedRewards)
            startActivity(intent)
        }
        reward_f.setOnClickListener     {
            makePurchase(4,5)
            val intent = MainActivity.newIntent(this, robotEnergy_Red, robotEnergy_White, robotEnergy_Yellow, robotTurn, purchasedRewards)
            startActivity(intent)
        }
        reward_g.setOnClickListener     {
            makePurchase(7,6)
            val intent = MainActivity.newIntent(this, robotEnergy_Red, robotEnergy_White, robotEnergy_Yellow, robotTurn, purchasedRewards)
            startActivity(intent)
        }
        random_reward.setOnClickListener{
            makePurchase(((1..7).random()), 7)
            val intent = MainActivity.newIntent(this, robotEnergy_Red, robotEnergy_White, robotEnergy_Yellow, robotTurn, purchasedRewards)
            startActivity(intent)
        }
    }

    // Function that allows us to purchase rewards based on energy levels of each robot
    private fun makePurchase(costOfPurchase : Int, indy : Int){
        // Initial set of rewards, later we'll check it in order to see rewards have been purchased yet
        val rewards = listOf(R.string.reward_a,
            R.string.reward_b,R.string.reward_c,
            R.string.reward_d,R.string.reward_e,
            R.string.reward_f, R.string.reward_g,R.string.random_reward)
        // Each if checks for which robots turn it is, update values accordingly if they are able to purchase
        if ((robotTurn == 1) or (robotTurn == 0)){
            if (robotEnergy_Red >= costOfPurchase) {
                // Checks for already purchased rewards
                if (purchasedRewards.contains(indy)) {
                    Toast.makeText(this, "You already purchased this reward", Toast.LENGTH_SHORT).show()
                } else {
                    val s1 = getString(rewards[indy])
                    val s2 = "$s1 purchased"
                    robotEnergy_Red -= costOfPurchase
                    energy_amount.text = robotEnergy_Red.toString()
                    // Adds the reward to already purchased list, purchasedRewards
                    purchasedRewards.add(indy)
                    Toast.makeText(this, s2, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Insufficient Energy", Toast.LENGTH_SHORT).show()
            }
        }else if (robotTurn == 2){
            if (robotEnergy_White >= costOfPurchase) {
                if (purchasedRewards.contains(indy)) {
                    Toast.makeText(this, "You already purchased this reward", Toast.LENGTH_SHORT).show()
                } else {
                    val s1 = getString(rewards[indy])
                    val s2 = "$s1 purchased"
                    robotEnergy_White -= costOfPurchase
                    energy_amount.text = robotEnergy_White.toString()
                    purchasedRewards.add(indy)
                    Toast.makeText(this, s2, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Insufficient Energy", Toast.LENGTH_SHORT).show()
            }
        }else{
            if (robotEnergy_Yellow >= costOfPurchase) {
                if (purchasedRewards.contains(indy)) {
                    Toast.makeText(this, "You already purchased this reward", Toast.LENGTH_SHORT).show()
                } else {
                    val s1 = getString(rewards[indy])
                    val s2 = "$s1 purchased"
                    robotEnergy_Yellow -= costOfPurchase
                    energy_amount.text = robotEnergy_Yellow.toString()
                    purchasedRewards.add(indy)
                    Toast.makeText(this, s2, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Insufficient Energy", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Sets the Robot, energy.robot, image and how much energy the respective robot has
    private fun setRobotTurn() {
        if ((robotTurn == 1) or (robotTurn == 0)) {
            energy_robot.setImageResource(R.drawable.robot_red_large)
            energy_amount.text = robotEnergy_Red.toString()
        }else if (robotTurn == 2){
            energy_robot.setImageResource(R.drawable.robot_white_large)
            energy_amount.text = robotEnergy_White.toString()
        }else if (robotTurn == 3){
            energy_robot.setImageResource(R.drawable.robot_yellow_large)
            energy_amount.text = robotEnergy_Yellow.toString()
        } else if (robotTurn == 0){
            energy_robot.setImageResource(R.drawable.robot_red_large)
            energy_amount.text = robotEnergy_Red.toString()
        }
    }

    // Activity lifecycle logging using tag as log title. All inclusive
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
}








