package com.example.quizora.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizora.data.User
import com.example.quizora.network.LoginService
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
}


