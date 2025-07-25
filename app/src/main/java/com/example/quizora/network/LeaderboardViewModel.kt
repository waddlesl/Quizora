package com.example.quizora.network

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizora.data.User
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.collections.filter


@Serializable
data class LeaderboardEntry(
    val name: String,
    val xp: Int,
    val current_streak: Int? = 0 // nullable in case streak doesn't exist yet
)



class LeaderboardViewModel : ViewModel() {

    private val _leaderboard = MutableStateFlow<List<LeaderboardEntry>>(emptyList())
    val leaderboard: StateFlow<List<LeaderboardEntry>> = _leaderboard

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    fun fetchLeaderboard() {
        viewModelScope.launch {
            try {
                val response: List<LeaderboardEntry> = client.get("http://192.168.1.9/quizora/REST/leaderboard.php") {
                    accept(ContentType.Application.Json)
                }.body()

                _leaderboard.value = response
                    .filter { it.xp > 0 }
                    .sortedByDescending { it.xp }
            } catch (e: Exception) {
                Log.e("LeaderboardViewModel", "Failed to fetch leaderboard", e)
            }
        }
    }
}