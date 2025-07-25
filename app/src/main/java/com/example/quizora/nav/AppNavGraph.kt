package com.example.quizora.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.quizora.quizzes.FlashcardSwipeScreen
import com.example.quizora.quizzes.FlashcardViewModel
import androidx.compose.runtime.getValue
import com.example.quizora.quizzes.SpeedRoundScreen
import com.example.quizora.home.AdminHomeLayout
import com.example.quizora.home.LeaderboardScreen
import com.example.quizora.network.CourseViewModel
import com.example.quizora.network.LeaderboardViewModel
import com.example.quizora.quizzes.CourseListScreen
import com.example.quizora.quizzes.FillinScreen
import com.example.quizora.quizzes.chooseQuiz

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
        composable("flashcard") {
            val courseCode = null
            val viewModel: FlashcardViewModel = viewModel()
            LaunchedEffect(courseCode) {
                viewModel.fetchFlashcards(courseCode)
            }
            FlashcardSwipeScreen (navController = navController)
        }

        composable("leaderboard") { LeaderboardScreen() }
        //to run flashcard with different kinds of courses or subj
        composable("flashcard/{courseCode}") { backStackEntry ->
            val courseCode = backStackEntry.arguments?.getString("courseCode") ?: ""
            val viewModel: FlashcardViewModel = viewModel()

            // Fetch the flashcards when this screen is opened
            LaunchedEffect(courseCode) {
                viewModel.fetchFlashcards(courseCode)

            }

            FlashcardSwipeScreen(
                viewModel = viewModel,
                navController = navController
            )
        }


        composable("speed_round/{courseCode}") {
                backStackEntry ->
            val courseCode = backStackEntry.arguments?.getString("courseCode") ?: ""
            val loginViewModel: LoginViewModel  = viewModel()
            SpeedRoundScreen(
                courseCode = courseCode,
                navController = navController,
                viewModel = loginViewModel
            )
        }

        composable("fill/{courseCode}") {
                backStackEntry ->
            val courseCode = backStackEntry.arguments?.getString("courseCode") ?: ""
            val loginViewModel: LoginViewModel  = viewModel()
            FillinScreen(
                courseCode = courseCode,
                navController = navController,
                viewModel = loginViewModel
            )
        }
        /*
        composable("fill_in") { FillinScreen(navController, sharedViewModel)
        }*/

        composable("ListOfCourse/{speed}") { backStackEntry ->
            val type = backStackEntry.arguments?.getString("speed") ?: ""
            val viewModel: CourseViewModel = viewModel()
            CourseListScreen(
                type = type,
                navController = navController,
                viewModel = viewModel
            )
        }

        composable("ListOfCourse/{fill}") { backStackEntry ->
            val type = backStackEntry.arguments?.getString("fill") ?: ""
            val viewModel: CourseViewModel = viewModel()
            CourseListScreen(
                type = type,
                navController = navController,
                viewModel = viewModel
            )
        }


        composable("student_profile") {
            StudentProfileScreen(sharedViewModel)
        }
        composable("student_home") { StudentHomeScreen(navController = navController, sharedViewModel) }
        composable("admin_home") {
            AdminHomeLayout(navController)
        }
        composable("choose_quiz") {
            chooseQuiz()
        }}
        }





