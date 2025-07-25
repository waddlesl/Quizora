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
import androidx.compose.material3.Button
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.quizora.R
import com.example.quizora.data.User


@Composable
fun StudentHomeScreen(navController: NavHostController, viewModel: LoginViewModel) {
    val user = viewModel.currentUser
    val navigation = navController


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
        ) {
        }

        Column (
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(20.dp)
        ){
            Line1(navController, user?.name)
            Line2(navController)
            Line3(navController)
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(20.dp)
        ) {

        }
    }}

@Composable
fun Line1(navController: NavHostController, username: String?) {
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
                text = "${username ?: "Student"}!",
                fontFamily = FontFamily.Serif,
                fontSize = 25.sp,
                color = Color.White
            )
        }

        Button(
            onClick = { navController.navigate("student_profile") },
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
    fun Line2(navController: NavController) {
        //placeholder, where the leaderboard will be shown later.

            Card(
                modifier = Modifier
                    .height(250.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .clickable {navController.navigate("leaderboard")},
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF9ECEC0))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🏆 Leaderboard", fontSize = 20.sp, fontFamily = FontFamily.Serif)
                    Text("Tap to view full rankings")
                }
            }
        }



    @Composable
    fun Line3(navController: NavHostController) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 24.dp)
        ) {//placeholder pics. change pics for each game
            GameRow(R.drawable.math, "Multiple Choice") { navController.navigate("ListOfCourse/${"speed"}") }
            GameRow(R.drawable.chem, "Fill in The Blanks") { navController.navigate("ListOfCourse/${"fill"}") }
            GameRow(R.drawable.code, "Comprehensive Review") { navController.navigate("flashcard") }
        }
    }

    @Composable
    fun GameRow(imageRes: Int, label: String, onClick: () -> Unit) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { onClick() },
                modifier = Modifier
                    .width(200.dp)
                    .height(80.dp),
                shape = RoundedCornerShape(9.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9ECEC0)),
                contentPadding = PaddingValues(0.dp)
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = "Game Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Text(
                text = label,
                fontFamily = FontFamily.Serif,
                fontSize = 20.sp,
                color = Color.White
            )
        }
    }




