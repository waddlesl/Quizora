package com.example.quizora.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.example.quizora.network.LeaderboardViewModel

@Composable
fun LeaderboardScreen(viewModel: LeaderboardViewModel = LeaderboardViewModel()) {
    val leaderboardState = viewModel.leaderboard.collectAsState()
    val leaderboard = leaderboardState.value
        .filter { it.xp > 0 } // Exclude 0 XP
        .sortedByDescending { it.xp } // Sort highest XP first

    LaunchedEffect(Unit) {
        viewModel.fetchLeaderboard()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()

            .background(MaterialTheme.colorScheme.background),

    ) {
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center

        ) {
            Text(
                text = "🏆 Leaderboard",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp), color = Color.White

            )
        }

        leaderboard.forEachIndexed { index, user ->
            val bgColor = when (index) {
                0 -> Color(0xFFFFD700) // Gold
                1 -> Color(0xFFC0C0C0) // Silver
                2 -> Color(0xFFCD7F32) // Bronze
                else -> Color.LightGray
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .background(bgColor)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("${index + 1}. ${user.name}", fontSize = 18.sp)
                Text("${user.xp} XP", fontSize = 16.sp)
            }
        }
    }
}