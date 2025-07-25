package com.example.quizora.network

import android.content.Context
import android.util.Log
import com.example.quizora.data.User
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.json.JSONObject


// "http://192.168.18.16:8080/quizora/REST/add_record.php?"
@Serializable
data class LoginRequest(val email: String, val password: String)

@Serializable
data class LoginResponse(val success: Boolean, val message: String, val user: User? = null)

object LoginService {
    // Configure HttpClient with JSON serialization
    internal val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    suspend fun login(email: String, password: String): LoginResponse {
        val rawResponse = client.post("http://192.168.1.9/quizora/REST/login.php") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(email, password))
        }.bodyAsText()

        // Debug print: log the raw server response
        println("DEBUG: Raw server response -> $rawResponse")

        // Parse the JSON into LoginResponse
        return Json { ignoreUnknownKeys = true }.decodeFromString(LoginResponse.serializer(), rawResponse)
    }




    //hehe hello lemme add this for scores
    object ScoreService {
        suspend fun submitScore(userId: Int?, score: Int, context: Context): Boolean {
            return try {
                /*val response = client.get("http://192.168.1.9/quizora/REST/submit_score.php") {
                    contentType(ContentType.Application.FormUrlEncoded)
                    setBody("user_id=$userId&score=$score")
                }*/
                val url = "http://192.168.1.9/quizora/REST/submit_score.php?user_id=$userId&score=$score"
                Log.d("SubmitDebug", "URL: $url")
                val response = client.get(url)
                val body = response.bodyAsText()
                Log.d("SubmitDebug", "Server Response: $body")
                return response.status.isSuccess()
                response.status.isSuccess()
            } catch (e: Exception) {
                Log.e("SubmitDebug", "Exception occurred: ${e::class.simpleName} - ${e.message}", e)
                false
            }
        }
        suspend fun updateStreak(userId1: Int?, userId: Int): Pair<Int, Int>? {
            return try {
                val response = client.get("http://192.168.1.9/quizora/REST/update_streak.php") {
                    parameter("user_id", userId)
                }
                val jsonString = response.bodyAsText()
                Log.d("StreakDebug", "Raw response: $jsonString") // for checking of what went wrong
                val json = JSONObject(jsonString)
                val current = json.getInt("current_streak")
                val longest = json.getInt("longest_streak")
                Pair(current, longest)
            } catch (e: Exception) {
                Log.e("StreakDebug", "Failed to update streak: ${e.message}")
                null
            }
        }
    }
}

