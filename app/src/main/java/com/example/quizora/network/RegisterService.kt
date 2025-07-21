package com.example.quizora.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class RegisterRequest(val name: String, val email: String, val password: String)

@Serializable
data class RegisterResponse(val success: Boolean, val message: String)

object RegisterService {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return client.post("http://192.168.18.16:8080/quizora/REST/register.php") {
            contentType(ContentType.Application.Json)
            setBody(RegisterRequest(name, email, password))
        }.body()
    }
}