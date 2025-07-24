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
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.quizora.R
import com.example.quizora.data.User


@Composable
fun StudentHomeScreen(navController: NavHostController, viewModel: LoginViewModel) {
    val user = viewModel.currentUser
    val navigation = navController

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF7D5260))
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .background(Color(0xFF6650a4))
                .padding(16.dp)
        ) {
        }

        Column (
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(20.dp)
        ){
            Line1(navController, user)
            Line2(onClick = {
                // leaderboard screen
            })
            Line3(navigation)
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color(0xFF6650a4))
                .padding(20.dp)
        ) {

        }
    }}

    @Composable
    fun Line1(navController: NavHostController, user: User?) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .paddingFromBaseline(top=50.dp)
            ) {
                Text(
                    text = "Welcome,",
                    fontFamily = FontFamily.Serif,
                    fontSize = 40.sp,
                    color = Color.White
                )
                if (user != null){
                    Text(
                    text = "${user?.name}!",
                    fontFamily = FontFamily.Serif,
                    fontSize = 25.sp,
                    color = Color.White
                )}
                else{
                    Text(
                        text = "User!",
                        fontFamily = FontFamily.Serif,
                        fontSize = 25.sp,
                        color = Color.White
                    )

                }
            }

            Button(
                onClick = { navController.navigate("student_profile") }, //navigates to student profile screen
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF625B71)),
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
    fun Line2(onClick: () -> Unit) {
        //placeholder, where the leaderboard will be shown later.

            Card(
                modifier = Modifier
                    .height(250.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .clickable { onClick() },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF625B71))
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
            GameRow(R.drawable.math, "Multiple Choice") { navController.navigate("speed_round") }
            GameRow(R.drawable.chem, "Review?") { navController.navigate("flashcard") }
            GameRow(R.drawable.code, "Game #3") { /* game 3 screen */ }
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
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF625B71)),
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




