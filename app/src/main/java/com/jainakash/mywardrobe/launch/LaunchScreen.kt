package com.jainakash.mywardrobe.launch

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jainakash.mywardrobe.theme.WardrobeInk
import com.jainakash.mywardrobe.theme.WardrobeMist
import com.jainakash.mywardrobe.theme.WardrobeRose
import com.jainakash.mywardrobe.theme.WardrobeTeal
import kotlinx.coroutines.delay

private const val LaunchDurationMillis = 1_000L
private const val DoorAnimationMillis = 780

@Composable
fun LaunchScreen(onFinished: () -> Unit) {
    var open by remember { mutableStateOf(false) }
    val leftDoorRotation by animateFloatAsState(
        targetValue = if (open) -14f else 0f,
        animationSpec = tween(durationMillis = DoorAnimationMillis),
        label = "leftDoorRotation"
    )
    val rightDoorRotation by animateFloatAsState(
        targetValue = if (open) 14f else 0f,
        animationSpec = tween(durationMillis = DoorAnimationMillis),
        label = "rightDoorRotation"
    )

    LaunchedEffect(Unit) {
        open = true
        delay(LaunchDurationMillis)
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(28.dp),
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            WardrobeOpeningIllustration(
                leftDoorRotation = leftDoorRotation,
                rightDoorRotation = rightDoorRotation
            )
            Text(
                text = "My Wardrobe",
                style = MaterialTheme.typography.headlineSmall,
                color = WardrobeInk
            )
        }
    }
}

@Composable
private fun WardrobeOpeningIllustration(
    leftDoorRotation: Float,
    rightDoorRotation: Float
) {
    Box(
        modifier = Modifier
            .width(240.dp)
            .height(210.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .width(184.dp)
                .height(184.dp)
                .background(WardrobeTeal.copy(alpha = 0.14f), RoundedCornerShape(28.dp))
                .align(Alignment.BottomCenter)
        )
        PersonSilhouette(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = 28.dp, y = (-4).dp)
        )
        WardrobeCabinet(
            leftDoorRotation = leftDoorRotation,
            rightDoorRotation = rightDoorRotation,
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }
}

@Composable
private fun PersonSilhouette(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(WardrobeRose, CircleShape)
        )
        Box(
            modifier = Modifier
                .width(46.dp)
                .height(76.dp)
                .background(
                    WardrobeRose.copy(alpha = 0.88f),
                    RoundedCornerShape(24.dp, 24.dp, 12.dp, 12.dp)
                )
        )
    }
}

@Composable
private fun WardrobeCabinet(
    leftDoorRotation: Float,
    rightDoorRotation: Float,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(146.dp)
            .height(174.dp)
            .background(WardrobeInk, RoundedCornerShape(18.dp))
            .padding(7.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            WardrobeDoor(
                rotation = leftDoorRotation,
                knobAlignment = Alignment.CenterEnd,
                modifier = Modifier.weight(1f)
            )
            WardrobeDoor(
                rotation = rightDoorRotation,
                knobAlignment = Alignment.CenterStart,
                modifier = Modifier.weight(1f)
            )
        }
        ClothingHint(
            color = WardrobeRose,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = 20.dp)
        )
        ClothingHint(
            color = WardrobeTeal,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = 56.dp)
        )
    }
}

@Composable
private fun WardrobeDoor(
    rotation: Float,
    knobAlignment: Alignment,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .rotate(rotation)
            .background(WardrobeMist, RoundedCornerShape(12.dp))
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .background(WardrobeTeal, CircleShape)
                .align(knobAlignment)
        )
    }
}

@Composable
private fun ClothingHint(color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(74.dp)
            .height(22.dp)
            .background(color.copy(alpha = 0.92f), RoundedCornerShape(12.dp))
    )
}
