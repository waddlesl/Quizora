package com.example.quizora.quizzes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizora.R

@Preview
@Composable
fun chooseQuiz() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF25363E))
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .background(Color(0xFFECFB77))
                .padding(20.dp)
        ) {

        }
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Choose your quiz type:",
                fontSize = 24.sp,
                fontFamily = FontFamily.Serif,
                color = Color.White
            )

            Button(
                onClick = {  },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9ECEC0))
            ) {
                Text(
                    text = "Multiple Choice",
                    fontSize = 18.sp,
                    fontFamily = FontFamily.Serif,
                    color = Color.Black
                )
            }

            Button(
                onClick = {  },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9ECEC0))
            ) {
                Text(
                    text = "Flashcards",
                    fontSize = 18.sp,
                    fontFamily = FontFamily.Serif,
                    color = Color.Black
                )
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color(0xFFECFB77))
                .padding(20.dp)
        ) {

        }
    }
}
