package com.example.quizora.quizzes

import androidx.compose.foundation.background
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.quizora.network.CourseViewModel


@Composable
fun CourseListScreen(
    type: String,
    navController: NavHostController, courseViewModel: CourseViewModel = viewModel(), viewModel: Any

) {
    val courses by courseViewModel.courses

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).verticalScroll(rememberScrollState()) ) {
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center

        ) {
            Text("Available Courses", style = MaterialTheme.typography.headlineMedium, color = Color.White)
            }



        Spacer(Modifier.height(16.dp))

        courses.forEach { course ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Course: ${course.courseCode}")

                    Spacer(Modifier.height(8.dp))

                    if(type == "speed"){
                    Button(onClick = {
                        navController.navigate("speed_round/${course.courseCode}")
                    }) {
                        Text("Take Quiz")
                    }}
                    else{
                        Button(onClick = {
                            navController.navigate("fill/${course.courseCode}")
                        }) {
                            Text("Take Quiz")
                        }


                    }

                    Spacer(Modifier.height(4.dp))

                    OutlinedButton(onClick = {
                        navController.navigate("flashcard/${course.courseCode}")
                    }) {
                        Text("Review")
                    }
                }
            }
        }
    }}
