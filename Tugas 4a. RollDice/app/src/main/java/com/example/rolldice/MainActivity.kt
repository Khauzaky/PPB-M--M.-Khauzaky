package com.example.rolldice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.rolldice.ui.theme.RollDiceTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.animation.core.tween
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.graphics.graphicsLayer


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RollDiceTheme {
                DiceRollerApp()
            }
        }
    }
}

@Composable
fun DiceWithButtonandImage(modifier: Modifier = Modifier) {
    var result by remember { mutableIntStateOf(1) }
    var rotateDegrees by remember { mutableFloatStateOf(0f) }

    val rotationAnimation: Float by animateFloatAsState(
        targetValue = rotateDegrees,
        animationSpec = tween(durationMillis = 450, easing = FastOutSlowInEasing)
    )

    val imageOfDice = when (result){
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        else -> R.drawable.dice_6
    }

    Column (
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = imageOfDice),
            contentDescription = "Dice image",
            modifier = Modifier.graphicsLayer(
                rotationZ =  rotationAnimation
            )
        )

        Button(onClick = {
            result = (1..6).random()
            rotateDegrees += 360f
        }){
            Text(text = "Roll")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DiceRollerApp (){
    DiceWithButtonandImage(modifier = Modifier
        .fillMaxSize()
        .wrapContentSize(align = Alignment.Center)
    )
}
