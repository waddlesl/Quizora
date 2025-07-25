package com.example.quizora.auth

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizora.data.User
import com.example.quizora.network.LoginService
import com.example.quizora.network.LoginService.ScoreService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel : ViewModel() {
    var currentUser: User? = null

    fun login(email: String, password: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    LoginService.login(email, password)
                }
                if (response.success && response.user != null) {
                    currentUser = response.user
                    onResult(true, "Login successful")
                } else {
                    onResult(false, response.message)
                }
            } catch (e: Exception) {
                onResult(false, "Error: ${e.message}")
            }
        }
    }

    //hgehehe morestudff
    fun submitUserScore(score: Int, context: Context, onResult: (Boolean) -> Unit) {
        val userId = currentUser?.id
        if (userId == null) {
            Log.e("SubmitDebug", "User ID is null")
            onResult(false)
            return
        }

        viewModelScope.launch {
            try {
                val result = ScoreService.submitScore(userId, score, context)
                Log.d("SubmitDebug", "Score submission result: $result")
                onResult(result)
            } catch (e: Exception) {
                Log.e("SubmitDebug", "Submit error: ${e.message}", e)
                onResult(false)
            }
        }
    }
    fun updateStreak(userId: Int?, score: Int, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val result = ScoreService.updateStreak(userId, score)
                val success = result != null
                onResult(success)
            } catch (e: Exception) {
                Log.e("UpdateStreakError", "Exception: ${e.message}")
                onResult(false)
            }
        }
    }}


