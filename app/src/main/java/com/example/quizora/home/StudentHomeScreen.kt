package com.example.quizora.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.quizora.auth.LoginViewModel

@Composable
fun StudentHomeScreen(viewModel: LoginViewModel) {
    val user = viewModel.currentUser

    Column(
        Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Welcome ${user?.name ?: "Student"}!")
        user?.let {
            Text("Email: ${it.email}")
            Text("XP: ${it.xp}")
            Text("Streak: ${it.streak}")
            Text("Class Code: ${it.class_code}")
            Text("Role: ${it.role}")
        }
    }
}
