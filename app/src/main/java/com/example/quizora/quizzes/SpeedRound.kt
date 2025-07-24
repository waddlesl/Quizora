package com.example.quizora.quizzes

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.android.volley.toolbox.StringRequest
import kotlinx.coroutines.delay
import org.json.JSONArray
import com.android.volley.Request
import com.android.volley.toolbox.Volley
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.json.JSONException
import kotlin.collections.forEachIndexed
import kotlin.collections.map
import kotlin.ranges.until
import kotlin.text.split
import kotlin.text.trim
import kotlin.toString
import androidx.navigation.compose.rememberNavController


class SpeedRound(navController: NavHostController) : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme (
                    colorScheme = darkColorScheme(
                        background = Color(0xFF12181C),
                        surface = Color(0xFF1E1E1E),
                        primary = Color(0xFF2F8EFF)
                    )
            ) {
                val navController = rememberNavController()
                SpeedroundQuiz(navController = navController)
            }
        }
    }

    @Composable
    fun SpeedroundQuiz(navController: NavHostController){
        GetQuestionScreen()
        start()

    }

    val quizQuestions = mutableStateListOf<Question>()

    data class Question(
        val questionText: String,
        val choices: List<String>,
        val answer: String
    )

    @Composable
    fun start() {
        var showGame by remember { mutableStateOf(false) }

        if (showGame) {
            play(onExit = { showGame = false })
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Button(onClick = { showGame = true }) {
                            Text("Start Game")
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun play(onExit: () -> Unit) {
        var retryKey by remember { mutableIntStateOf(0) }

        QuizScreen(
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
        key: Int
    ) {
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
                }
            }
        }
    }



    @Composable
    fun GetQuestionScreen() {
        val context = LocalContext.current

        LaunchedEffect(Unit) {
            val url = "http://192.168.1.9/quizora/REST/get_all_rows.php" //192.168.100.7:8080 databse URl
            val request = StringRequest(Request.Method.GET, url,
                { response ->
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

}






