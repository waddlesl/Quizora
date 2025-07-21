package com.example.quizora.nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quizora.auth.LoginScreen
import com.example.quizora.auth.LoginViewModel
import com.example.quizora.auth.RegisterScreen
import com.example.quizora.home.StudentHomeScreen
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val sharedViewModel: LoginViewModel = viewModel()

    NavHost(navController, startDestination = "login") {
        composable("login") { LoginScreen(navController, sharedViewModel) }
        composable("register") { RegisterScreen(navController) }
        composable("student_home") { StudentHomeScreen(sharedViewModel) }
    }
}