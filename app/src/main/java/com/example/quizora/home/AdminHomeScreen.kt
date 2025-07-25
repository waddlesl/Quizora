package com.example.quizora.home

import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.quizora.R

@Composable
fun AdminHomeLayout(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {}

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Top1(navController)
            Spacer(modifier = Modifier.height(24.dp))
            Top2()
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(20.dp)
        ) {}

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 100.dp)
        ) {
            AddButton(navController)
        }
    }
}

@Composable
fun Top1(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .paddingFromBaseline(top = 50.dp)
        ) {
            Text(
                text = "Welcome,",
                fontFamily = FontFamily.Serif,
                fontSize = 40.sp,
                color = Color.White
            )
            Text(
                text = "Admin!",
                fontFamily = FontFamily.Serif,
                fontSize = 25.sp,
                color = Color.White
            )
        }

        Button(
            onClick = {  navController.navigate("login")},
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9ECEC0)),
            contentPadding = PaddingValues(0.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.user),
                contentDescription = "User Profile",
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@Composable
fun Top2() {
    Column {
        Text(
            text = "Published Quizzes:",
            fontFamily = FontFamily.Serif,
            fontSize = 20.sp,
            color = Color.White
        )
    }

    Spacer(modifier = Modifier.height(30.dp))

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        QuizRow(R.drawable.math, "Q#1") {  }
        QuizRow(R.drawable.chem, "Q#2") {  }
    }
}

@Composable
fun QuizRow(imageRes: Int, label: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .width(200.dp)
                .height(80.dp),
            shape = RoundedCornerShape(9.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9ECEC0)),
            contentPadding = PaddingValues(0.dp)
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "Quiz Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.height(80.dp)
        ) {
            Text(
                text = label,
                fontFamily = FontFamily.Serif,
                fontSize = 20.sp,
                color = Color.White
            )
            Text(
                text = "edit",
                fontFamily = FontFamily.Serif,
                fontSize = 14.sp,
                color = Color.White
            )
        }
    }
}

@Composable
fun AddButton(navController: NavController) {
    Button(
        onClick = {
            navController.navigate("add_quiz")
        },
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9ECEC0)),
        contentPadding = PaddingValues(0.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.add),
            contentDescription = "Add Button",
            modifier = Modifier.size(30.dp)
        )
    }
}
