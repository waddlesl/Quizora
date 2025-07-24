package com.example.quizora.quizzes

import android.content.Context
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
import com.example.quizora.network.LoginService.ScoreService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException


@Composable
fun FillinScreen(navController: NavHostController, viewModel: LoginViewModel) {
    fillGetQuestionScreen()
    fillstart(navController = navController, viewModel = viewModel)
}

@Composable
fun fillstart(navController: NavHostController,viewModel: LoginViewModel) {
    var showGame by remember { mutableStateOf(false) }

    if (showGame) {
        fillplay(viewModel = viewModel,onExit = { showGame = false })
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
fun fillplay(viewModel: LoginViewModel, onExit: () -> Unit) {
    var retryKey by remember { mutableIntStateOf(0) }

    fillQuizScreen(
        viewModel = viewModel,
        questions = quizQuestions,
        onRetry = { retryKey++ },
        onExit = onExit,
        key = retryKey
    )
}

@Composable
fun fillQuizScreen(
    questions: List<Question>,
    onRetry: () -> Unit,
    onExit: () -> Unit,
    key: Int,
    viewModel: LoginViewModel
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var currentIndex by remember(key) { mutableIntStateOf(0) }
    var userInput by remember(key) { mutableStateOf<String?>(null) }
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
                    userInput = ""
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

                var userInput by remember(key) { mutableStateOf("") }

                OutlinedTextField(
                    value = userInput,
                    onValueChange = { userInput = it },
                    label = { Text("Type your answer") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        onClick = {
                            if (!showFeedback) {
                                currentIndex++
                                userInput= ""
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
                            if (userInput.isNotBlank()) {
                                isCorrect = userInput.trim().equals(question.answer.trim(), ignoreCase = true)
                                if (isCorrect) score++
                                showFeedback = true
                            }
                        },
                        enabled = userInput.isNotBlank() && !showFeedback,
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
                            userInput = ""
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
                    coroutineScope.launch {
                        fillsubmitScore(context, viewModel, score)
                    }
                    onExit()
                }) {
                    Text("Submit")
                }
            }
        }
    }
}



suspend fun fillsubmitScore(context: Context, viewModel: LoginViewModel, score: Int) {
    val user = viewModel.currentUser
    user?.let {
        ScoreService.submitScore(
            userId = it.id,
            score = score,
            context = context
        )
    }
}



@Composable
fun fillGetQuestionScreen() {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val url = "http://192.168.1.9/quizora/REST/get_all_rows.php" //192.168.100.7:8080 databse URl
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

