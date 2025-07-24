package com.example.quizora.network

import android.content.Context
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


// "http://192.168.18.16:8080/quizora/REST/add_record.php?"
@Serializable
data class LoginRequest(val email: String, val password: String)

@Serializable
data class LoginResponse(val success: Boolean, val message: String, val user: User? = null)

object LoginService {
    // Configure HttpClient with JSON serialization
    private val client = HttpClient(CIO) {
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
        suspend fun submitScore(userId: Int, score: Int, context: Context): Boolean {
            return try {
                val response = client.get("http://192.168.1.9/quizora/REST/submit_score.php") {
                    parameter("user_id", userId)
                    parameter("score", score)
                }
                response.status.isSuccess()
            } catch (e: Exception) {
                false
            }
        }
    }
}

