package com.bignerdranch.android.geoquiz
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

    private var robotTurn: Int = 0
    private var energyValue: Int = 0

    companion object {
        fun newIntent(packageContext: Context, robotEnergy: Int, robotTurn: Int): Intent {
            return Intent(packageContext, RobotPurchaseActivity::class.java).apply {
                putExtra(EXTRA_ROBOT_ENERGY, robotEnergy)
                putExtra(EXTRA_ROBOT_TURN, robotTurn)
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

        robotTurn = intent.getIntExtra(EXTRA_ROBOT_TURN, 0)
        energyValue = intent.getIntExtra(EXTRA_ROBOT_ENERGY, 0)

        setRobotTurn()

        reward_a.setOnClickListener { view: View ->
            Toast.makeText(this, "Turn Count: ${energyValue}", Toast.LENGTH_SHORT).show()
        }

    }

    private fun setRobotTurn() {
        if (robotTurn == 1) {
            energy_robot.setImageResource(R.drawable.robot_red_large)
            energy_amount.text = energyValue.toString()
        }else if (robotTurn == 2){
            energy_robot.setImageResource(R.drawable.robot_white_large)
            energy_amount.text = energyValue.toString()
        }else if (robotTurn == 3){
            energy_robot.setImageResource(R.drawable.robot_yellow_large)
            energy_amount.text = energyValue.toString()
        } else {
            energy_robot.setImageResource(R.drawable.robot_red_large)
            energy_amount.text = energyValue.toString()
        }
    }

}








