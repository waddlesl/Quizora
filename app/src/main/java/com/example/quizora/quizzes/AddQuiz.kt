package com.example.quizora.quizzes

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.serialization.json.addJsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.IOException

@Preview(showBackground = true)
@Composable
fun AddQuizScreen() {
    val context = LocalContext.current

    var courseCode by remember { mutableStateOf("") }
    var quizNumber by remember { mutableStateOf("") }
    val questions = remember { mutableStateListOf<QuestionInputState>() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF25363E))
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .padding(20.dp)
        ) {


            Text(text = "Course Code:", color = Color.White)
            OutlinedTextField(
                value = courseCode,
                onValueChange = { courseCode = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter course code") }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = "Quiz Number:", color = Color.White)
            OutlinedTextField(
                value = quizNumber,
                onValueChange = { quizNumber = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter quiz number") }
            )

            Spacer(modifier = Modifier.height(24.dp))


            questions.forEachIndexed { index, question ->
                Text("Question ${index + 1}", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = question.questionNumber.value,
                    onValueChange = { question.questionNumber.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Question Number") }
                )
                OutlinedTextField(
                    value = question.questionText.value,
                    onValueChange = { question.questionText.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Question") }
                )
                OutlinedTextField(
                    value = question.choiceA.value,
                    onValueChange = { question.choiceA.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Choice A") }
                )
                OutlinedTextField(
                    value = question.choiceB.value,
                    onValueChange = { question.choiceB.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Choice B") }
                )
                OutlinedTextField(
                    value = question.choiceC.value,
                    onValueChange = { question.choiceC.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Choice C") }
                )
                OutlinedTextField(
                    value = question.choiceD.value,
                    onValueChange = { question.choiceD.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Choice D") }
                )
                OutlinedTextField(
                    value = question.correctAnswer.value,
                    onValueChange = { question.correctAnswer.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Correct Answer (A/B/C/D)") }
                )

                Spacer(modifier = Modifier.height(16.dp))
            }


            Button(
                onClick = { questions.add(QuestionInputState()) },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Add Question")
            }

            Spacer(modifier = Modifier.height(24.dp))


            Button(
                onClick = {
                    if (validateInputs(context, courseCode, quizNumber, questions)) {
                        submitQuizToBackend(
                            context = context,
                            courseCode = courseCode,
                            quizNumber = quizNumber,
                            questions = questions
                        )
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Submit")
            }
        }
    }
}

data class QuestionInputState(
    var questionNumber: MutableState<String> = mutableStateOf(""),
    var questionText: MutableState<String> = mutableStateOf(""),
    var choiceA: MutableState<String> = mutableStateOf(""),
    var choiceB: MutableState<String> = mutableStateOf(""),
    var choiceC: MutableState<String> = mutableStateOf(""),
    var choiceD: MutableState<String> = mutableStateOf(""),
    var correctAnswer: MutableState<String> = mutableStateOf("")
)

private fun validateInputs(
    context: Context,
    courseCode: String,
    quizNumber: String,
    questions: List<QuestionInputState>
): Boolean {
    if (courseCode.isBlank() || quizNumber.isBlank()) {
        toastOnMain(context, "Course Code and Quiz Number are required.")
        return false
    }
    if (questions.isEmpty()) {
        toastOnMain(context, "Please add at least one question.")
        return false
    }

    return true
}

private fun submitQuizToBackend(
    context: Context,
    courseCode: String,
    quizNumber: String,
    questions: List<QuestionInputState>
) {
    val client = OkHttpClient()

    val jsonBody = buildJsonObject {
        put("course_code", courseCode.toInt())
        put("quiz_number", quizNumber.toInt())
        putJsonArray("questions") {
            questions.forEach { q ->
                addJsonObject {
                    put("question", q.questionText.value)
                    put("choiceA", q.choiceA.value)
                    put("choiceB", q.choiceB.value)
                    put("choiceC", q.choiceC.value)
                    put("choiceD", q.choiceD.value)
                    put("correctAnswer", q.correctAnswer.value)
                }
            }
        }
    }.toString()

    val mediaType = "application/json; charset=utf-8".toMediaType()


    val requestBody: RequestBody = RequestBody.create(mediaType, jsonBody)

    val request = Request.Builder()
        .url("http://192.168.1.9/quizora/REST/submit_quiz.php")
        .post(requestBody)
        .build()

    client.newCall(request).enqueue(object : okhttp3.Callback {
        override fun onFailure(call: okhttp3.Call, e: IOException) {
            toastOnMain(context, "Failed to submit quiz: ${e.message}")
        }

        override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
            if (response.isSuccessful) {
                toastOnMain(context, "Quiz submitted successfully!")
            } else {
                toastOnMain(context, "Failed to submit quiz: ${response.message}")
            }
        }
    })
}

private fun toastOnMain(context: Context, msg: String) {
    Handler(Looper.getMainLooper()).post {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }
}
