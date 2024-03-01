package com.bignerdranch.android.geoquiz

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bignerdranch.android.geoquiz.databinding.ActivityMainBinding
import com.bignerdranch.android.geoquiz.ui.theme.GeoQuizTheme

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {

    private lateinit var red_img    : ImageView
    private lateinit var white_img  : ImageView
    private lateinit var yellow_img : ImageView
    private lateinit var rot_clock  : ImageView
    private lateinit var rot_counter: ImageView
    private lateinit var message_box: TextView
    private lateinit var robotImages: MutableList<ImageView>

    private val robots = listOf(
        Robot(false, R.drawable.robot_red_large, R.drawable.robot_red_small),
        Robot(false, R.drawable.robot_white_large, R.drawable.robot_white_small),
        Robot(false, R.drawable.robot_yellow_large, R.drawable.robot_yellow_small)
    )
    private val robotViewModel : RobotViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "got a robotViewModel @: $robotViewModel")

        red_img   = findViewById(R.id.robot_red_large)
        white_img = findViewById(R.id.robot_white_large)
        yellow_img= findViewById(R.id.robot_yellow_large)
        message_box=findViewById(R.id.robot_turn)
        rot_clock = findViewById(R.id.rot_clock)
        rot_counter=findViewById(R.id.rot_counter)
        robotImages = mutableListOf(red_img, white_img, yellow_img)


        red_img.setOnClickListener { view : View ->
            Toast.makeText(this, "Turn: ${robotViewModel.turnCount}", Toast.LENGTH_SHORT).show()
        }

        rot_counter.setOnClickListener { advanceTurn() }




    }

    private fun advanceTurn(){
        robotViewModel.turnCount += 1
        if(robotViewModel.turnCount > 3 ){
            robotViewModel.turnCount = 1
        }
        setRobotTurn()
        setImages()
    }

    private fun setRobotTurn(){
        for(robot in robots){
            robot.myTurn = false
        }
        robots[robotViewModel.turnCount-1].myTurn = true
    }
    private fun setImages() {
        var indy = 0
        while (indy < robotImages.size && indy < robots.size) {
            if (robots[indy].myTurn) {
                robotImages[indy].setImageResource(robots[indy].largeImgRes)
            } else {
                // Set a different image resource for robots when it's not their turn
                robotImages[indy].setImageResource(robots[indy].smallImgRes)
            }
            indy++
        }
    }

}
