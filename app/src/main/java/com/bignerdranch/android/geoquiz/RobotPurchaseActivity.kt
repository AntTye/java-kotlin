package com.bignerdranch.android.geoquiz
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity

private const val EXTRA_ROBOT_ENERGY = "com.bignerdranch.android.geoquiz.EXTRA_ROBOT_ENERGY"
private const val EXTRA_ROBOT_TURN = "com.bignerdranch.android.geoquiz.EXTRA_ROBOT_TURN"

class RobotPurchaseActivity : ComponentActivity() {

    private lateinit var energy_robot: ImageView
    private lateinit var energy_amount: TextView
    private lateinit var reward_a: TextView
    private lateinit var reward_b: TextView
    private lateinit var reward_c: TextView
    private lateinit var reward_d: TextView
    private lateinit var reward_e: TextView
    private lateinit var reward_f: TextView
    private lateinit var reward_g: TextView
    private lateinit var random_reward: TextView

    private var robot_turn_main: Int = 0
    private var energy_amount_main: Int = 0
    private val purchasedRewards = mutableListOf<Int>()

    companion object {
        fun newIntent(packageContext: Context, robotEnergy: Int, robot_turn_main: Int): Intent {
            return Intent(packageContext, RobotPurchaseActivity::class.java).apply {
                putExtra(EXTRA_ROBOT_ENERGY, robotEnergy)
                putExtra(EXTRA_ROBOT_TURN, robot_turn_main)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.robot_purchase)
        energy_robot = findViewById(R.id.energy_robot)
        energy_amount = findViewById(R.id.energy_amount)
        reward_a = findViewById(R.id.reward_a)
        reward_b = findViewById(R.id.reward_b)
        reward_c = findViewById(R.id.reward_c)
        reward_d = findViewById(R.id.reward_d)
        reward_e = findViewById(R.id.reward_e)
        reward_f = findViewById(R.id.reward_f)
        reward_g = findViewById(R.id.reward_g)
        random_reward = findViewById(R.id.random_reward)

        robot_turn_main = intent.getIntExtra(EXTRA_ROBOT_TURN, 0)
        energy_amount_main = intent.getIntExtra(EXTRA_ROBOT_ENERGY, 0)

        setRobotTurn()

        reward_a.setOnClickListener     {
            makePurchase(1)
            val intent = MainActivity.newIntent(this, energy_amount_main)
            startActivity(intent)
        }
        reward_b.setOnClickListener     {
            makePurchase(2)
            val intent = MainActivity.newIntent(this, energy_amount_main)
            startActivity(intent)
        }
        reward_c.setOnClickListener     {
            makePurchase(3)
            val intent = MainActivity.newIntent(this, energy_amount_main)
            startActivity(intent)
        }
        reward_d.setOnClickListener     {
            makePurchase(3)
            val intent = MainActivity.newIntent(this, energy_amount_main)
            startActivity(intent)
        }
        reward_e.setOnClickListener     {
            makePurchase(4)
            val intent = MainActivity.newIntent(this, energy_amount_main)
            startActivity(intent)
        }
        reward_f.setOnClickListener     {
            makePurchase(4)
            val intent = MainActivity.newIntent(this, energy_amount_main)
            startActivity(intent)
        }
        reward_g.setOnClickListener     {
            makePurchase(7)
            val intent = MainActivity.newIntent(this, energy_amount_main)
            startActivity(intent)
        }
        random_reward.setOnClickListener{
            val randomCost = (1..7).random()
            makePurchase(randomCost)
            val intent = MainActivity.newIntent(this, energy_amount_main)
            startActivity(intent)
        }
    }

    private fun makePurchase(costOfPurchase : Int){
        val rewards = listOf(R.string.reward_a,R.string.reward_b,R.string.reward_c,R.string.reward_d,R.string.reward_f,R.string.reward_g,R.string.random_reward)
        if (energy_amount_main >= costOfPurchase) {
            val indy = costOfPurchase - 1
            if (purchasedRewards.contains(indy)) {
                Toast.makeText(this, "You already purchased this reward", Toast.LENGTH_SHORT).show()
            } else {
                val s1 = getString(rewards[indy])
                val s2 = "$s1 purchased"
                energy_amount_main -= costOfPurchase
                energy_amount.text = energy_amount_main.toString()
                purchasedRewards.add(indy)
                Toast.makeText(this, s2, Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Insufficient Energy", Toast.LENGTH_SHORT).show()
            //finish()
        }
    }
    private fun setRobotTurn() {
        if (robot_turn_main == 1) {
            energy_robot.setImageResource(R.drawable.robot_red_large)
            energy_amount.text = energy_amount_main.toString()
        }else if (robot_turn_main == 2){
            energy_robot.setImageResource(R.drawable.robot_white_large)
            energy_amount.text = energy_amount_main.toString()
        }else if (robot_turn_main == 3){
            energy_robot.setImageResource(R.drawable.robot_yellow_large)
            energy_amount.text = energy_amount_main.toString()
        } else {
            energy_robot.setImageResource(R.drawable.robot_red_large)
            energy_amount.text = energy_amount_main.toString()
        }
    }

}








