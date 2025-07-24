package com.example.quizora.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizora.auth.LoginViewModel
import androidx.compose.ui.tooling.preview.Preview
import com.example.quizora.data.User

@Composable
fun StudentProfileScreen(viewModel: LoginViewModel) {
    val user = viewModel.currentUser

    Box(
        modifier = Modifier
            .background(Color(0xFF25363E))
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Welcome, ${user?.name ?: "Student"}!", color = Color.White)

            user?.let {
                ProfileField(label = "Email", value = it.email)
                ProfileField(label = "XP", value = it.xp.toString())
                ProfileField(label = "Streak", value = it.streak.toString())
                ProfileField(label = "Class Code", value = it.class_code.toString())
                ProfileField(label = "Role", value = it.role)
            }
        }
    }
}

@Composable
fun ProfileField(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        )
    }
}

