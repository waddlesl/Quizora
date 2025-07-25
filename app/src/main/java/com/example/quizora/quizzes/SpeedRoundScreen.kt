package com.example.quizora.quizzes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.quizora.auth.LoginViewModel
import kotlinx.coroutines.delay
import org.json.JSONArray
import org.json.JSONException


val quizQuestions = mutableStateListOf<Question>()

data class Question(
    val questionText: String,
    val choices: List<String>,
    val answer: String
)

@Composable
fun SpeedRoundScreen(courseCode: String? = null, navController: NavHostController, viewModel: LoginViewModel) {
    GetQuestionScreen(courseCode = courseCode)
    start(navController = navController, viewModel = viewModel)
}

@Composable
fun start(navController: NavHostController,viewModel: LoginViewModel) {
    var showGame by remember { mutableStateOf(false) }

    if (showGame) {
        play(viewModel = viewModel,onExit = { showGame = false })
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background),
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
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

            LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
                item{
                    Text("Ready?", style = MaterialTheme.typography.headlineMedium, color = Color.White)
                }
                item {
                    Button(onClick = { showGame = true }) {
                        Text("Start Game")
                    }
                }
            }
        }
    }
}}


@Composable
fun play(viewModel: LoginViewModel, onExit: () -> Unit) {
    var retryKey by remember { mutableIntStateOf(0) }

    QuizScreen(
        viewModel = viewModel,
        questions = quizQuestions,
        onRetry = { retryKey++ },
        onExit = onExit,
        key = retryKey
    )
}

@Composable
fun QuizScreen(
    questions: List<Question>,
    onRetry: () -> Unit,
    onExit: () -> Unit,
    key: Int,
    viewModel: LoginViewModel
) {

    val userId = viewModel.currentUser?.id
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var currentIndex by remember(key) { mutableIntStateOf(0) }
    var selectedOption by remember(key) { mutableStateOf<String?>(null) }
    var score by remember(key) { mutableIntStateOf(0) }

    var showFeedback by remember(key) { mutableStateOf(false) }
    var isCorrect by remember(key) { mutableStateOf(false) }
    var timeLeft by remember(key) { mutableIntStateOf(10) }

    val animatedTimeLeft by animateIntAsState(
        targetValue = timeLeft,
        label = "Timer"
    )

    if (currentIndex < questions.size) {
        val question = questions[currentIndex]

        LaunchedEffect(currentIndex) {
            timeLeft = 10
            if (!showFeedback) {
                while (timeLeft > 0) {
                    delay(1000)
                    timeLeft--
                }
                if (!showFeedback) {
                    currentIndex++
                    selectedOption = null
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // TOP BAR
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { onExit() }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Exit Quiz",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(10.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF2C3E50))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(fraction = timeLeft / 10f)
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(Color(0xFF2F8EFF), Color(0xFF00C6FF))
                                    )
                                )
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "$animatedTimeLeft",
                        color = if (timeLeft <= 3) Color.Red else Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 80.dp, start = 24.dp, end = 24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {

                Text(question.questionText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(16.dp))

                question.choices.forEachIndexed { index, option ->
                    OutlinedButton(
                        onClick = {
                            if (!showFeedback) selectedOption = option
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(
                            1.dp,
                            if (selectedOption == option) Color(0xFF2F8EFF) else Color.Gray
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            contentColor = Color.White
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .background(
                                        if (selectedOption == option) Color(0xFF2F8EFF) else Color.Gray,
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "${index + 1}",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(option, fontSize = 16.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        onClick = {
                            if (!showFeedback) {
                                currentIndex++
                                selectedOption = null
                            }
                        },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.LightGray
                        ),
                        border = BorderStroke(1.dp, Color.Gray)
                    ) {
                        Text("SKIP")
                    }

                    Button(
                        onClick = {
                            if (selectedOption != null) {
                                isCorrect = selectedOption == question.answer
                                if (isCorrect) score++
                                showFeedback = true
                            }
                        },
                        enabled = selectedOption != null && !showFeedback,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2F8EFF),
                            contentColor = Color.White,
                            disabledContainerColor = Color.DarkGray
                        )
                    ) {
                        Text("CHECK")
                    }
                }
            }

            AnimatedVisibility(
                visible = showFeedback,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it }),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(if (isCorrect) Color(0xFF1B5E20) else Color(0xFFB71C1C))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            if (isCorrect) "✅ Great!" else "❌ Correct solution:",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (!isCorrect) {
                            Text(
                                question.answer,
                                color = Color.White,
                                fontSize = 16.sp
                            )
                        }
                    }

                    Button(
                        onClick = {
                            currentIndex++
                            selectedOption = null
                            showFeedback = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isCorrect) Color(0xFF66BB6A) else Color(0xFFEF5350),
                            contentColor = Color.Black
                        )
                    ) {
                        Text("CONTINUE")
                    }
                }
            }
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🎉 Quiz Finished!", color = Color.White, fontSize = 24.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Text("Your Score: $score / ${questions.size}", color = Color.LightGray)
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = onRetry) {
                    Text("Retry")
                }
                Button(onClick = {
                    viewModel.submitUserScore(score, context) { result ->
                        if (!result) {
                            Toast.makeText(context, "Score submission failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                    viewModel.updateStreak(userId, score) { success ->
                        if (success) {
                            Toast.makeText(context, "Streak updated!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Failed to update streak", Toast.LENGTH_SHORT).show()
                        }
                    }
                    onExit()
                }) {
                    Text("Submit")
                }
            }
        }
    }
}






@Composable
fun GetQuestionScreen(courseCode: String? = null) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val baseUrl = "http://192.168.1.9/quizora/REST/get_all_rows.php" //192.168.100.7:8080 databse URl
        val url = if (courseCode != null) "$baseUrl?course_code=$courseCode" else baseUrl

        val request = StringRequest(Request.Method.GET, url,
            { response ->
                quizQuestions.clear()

                val jsonArray = JSONArray(response)
                for (i in 0 until jsonArray.length()) {
                    try {
                        val row = jsonArray.getJSONObject(i)
                        val questionText = row.optString("question")
                        val choicesString = row.optString("choices")
                        val answer = row.optString("answer")

                        val choiceList = choicesString.split(",").map { it.trim() }
                        quizQuestions.add(Question(questionText,choiceList,answer))
                    } catch (e: JSONException) {
                        Log.e("JSONError", "Parsing failed: ${e.message}")
                    }
                }
            },
            { error -> Log.e("VolleyError", error.toString()) }
        )
        Volley.newRequestQueue(context).add(request)
    }

    quizQuestions.forEachIndexed { index, question ->
        Log.d("QuizItem", "Question ${index + 1}: ${question.questionText}")
        Log.d("QuizItem", "Choices: ${question.choices}")
        Log.d("QuizItem", "Answer: ${question.answer}")
    }

}

