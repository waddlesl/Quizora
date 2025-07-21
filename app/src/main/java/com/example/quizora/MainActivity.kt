package com.example.quizora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.quizora.nav.AppNavGraph
import com.example.quizora.ui.theme.QuizoraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizoraTheme {
                AppNavGraph()
            }
        }
    }
}
