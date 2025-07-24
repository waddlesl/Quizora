package com.example.quizora.nav

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quizora.auth.LoginScreen
import com.example.quizora.auth.LoginViewModel
import com.example.quizora.auth.RegisterScreen
import com.example.quizora.home.StudentHomeScreen
import com.example.quizora.home.StudentProfileScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.quizora.quizzes.FlashcardSwipeScreen
import com.example.quizora.quizzes.FlashcardViewModel
import androidx.compose.runtime.getValue
import androidx.navigation.NavController

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val sharedViewModel: LoginViewModel = viewModel()

    //flashiescard
    val viewModel: FlashcardViewModel = viewModel()
    val flashcards by viewModel.flashcards.collectAsState()

    NavHost(navController, startDestination = "login") {
        composable("login") { LoginScreen(navController, sharedViewModel) }
        composable("register") { RegisterScreen(navController) }
        composable("flashcard") { FlashcardSwipeScreen (navController = navController)
        }
        composable("student_profile") {
            StudentProfileScreen(sharedViewModel)
        }
        composable("student_home") { StudentHomeScreen(navController = navController, sharedViewModel) }}

        }


