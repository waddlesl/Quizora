package com.example.quizora.quizzes

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.quizora.data.FlashCard
import kotlin.math.roundToInt


@Composable
fun FlashcardSwipeScreen(
    viewModel: FlashcardViewModel = viewModel(),
    navController: NavHostController
) {

    val density = LocalDensity.current.density
    val originalFlashcards by viewModel.flashcards.collectAsState()
    val flashcards = remember { mutableStateListOf<FlashCard>() }

    LaunchedEffect(originalFlashcards) {
        flashcards.clear()
        flashcards.addAll(originalFlashcards)
    }

    var isFlipped by remember { mutableStateOf(false) }
    var showBack by remember { mutableStateOf(false) }
    var offsetX by remember { mutableStateOf(0f) }
    var targetRotation by remember { mutableStateOf(0f) }

    val animatedRotationY by animateFloatAsState(
        targetValue = targetRotation,
        animationSpec = tween(durationMillis = 400),
        label = "flipAnimation"
    )

    // Delay text change based on rotation
    LaunchedEffect(animatedRotationY ) {
        if (animatedRotationY  >= 90f && !showBack && isFlipped) {
            showBack = true
        } else if (animatedRotationY  < 90f && showBack && !isFlipped) {
            showBack = false
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.Black)
        )
        {
            BackButton(navController)

            if (flashcards.isEmpty()) {

                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("You're done reviewing!", color = Color.White)
                }
                return
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center,
            ) {
                flashcards.forEachIndexed { index, card ->
                    val scale = 1f - (0.05f * index)
                    val cardOffsetX = if (index == 0) offsetX else 0f
                    val visualOffsetX = if (index == 0 && isFlipped) -offsetX else if (index == 0) offsetX else 0f


                    Card(
                        modifier = Modifier
                            .graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                                translationX = visualOffsetX
                                rotationY = if (index == 0) animatedRotationY else 0f
                                cameraDistance = 8 * density
                            }
                            .offset { IntOffset(visualOffsetX.roundToInt(), 0) }
                            .fillMaxWidth()
                            .height(250.dp)
                            .offset { IntOffset(cardOffsetX.roundToInt(), 0) }
                            .zIndex((100 - index).toFloat())
                            .pointerInput(index)
                            {
                                if (index == 0) {
                                    detectDragGestures(
                                        onDrag = { change, dragAmount ->
                                            change.consume()
                                            offsetX += dragAmount.x
                                        },
                                        onDragEnd = {
                                            when {
                                                offsetX > 300f -> {
                                                    if (flashcards.size > 1) {
                                                        flashcards.removeAt(0)
                                                    } else {
                                                        flashcards.clear()
                                                    }
                                                    offsetX = 0f
                                                    isFlipped = false
                                                    showBack = false
                                                    targetRotation = 0f
                                                }

                                                offsetX < -300f -> {
                                                    if (flashcards.size > 1) {
                                                        val moved = flashcards.removeAt(0)
                                                        flashcards.add(moved)
                                                    }
                                                    offsetX = 0f
                                                    isFlipped = false
                                                    showBack = false
                                                    targetRotation = 0f
                                                }

                                                else -> {
                                                    offsetX = 0f
                                                }
                                            }
                                        }
                                    )
                                }
                            }
                            .clickable {
                                if (index == 0) {
                                    isFlipped = !isFlipped
                                    targetRotation = if (isFlipped) 180f else 0f
                                }
                            },
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (index == 0 && showBack) {

                                Text(
                                    text = card.answer,
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.graphicsLayer {
                                        rotationY = 180f // keep the back text upright
                                    }
                                )
                            } else {

                                Text(
                                    text = card.question,
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun BackButton(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.TopStart
    ) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}
