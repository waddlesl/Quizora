package com.example.quizora.network

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizora.data.User
import io.ktor.client.HttpClient
import io.ktor.client.call.body
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
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json



@Serializable
data class Course(
    val courseCode: String
)

class CourseViewModel : ViewModel() {
    private val _courses = mutableStateOf<List<Course>>(emptyList())
    val courses: androidx.compose.runtime.State<List<Course>> = _courses

    init {
        fetchCourses()
    }

    private fun fetchCourses() {
        viewModelScope.launch {
            val client = HttpClient(CIO) {
                install(ContentNegotiation) {
                    json(Json {
                        ignoreUnknownKeys = true
                    })
                }
            }

            try {
                val response: List<Course> =
                    client.get("http://192.168.1.9/quizora/REST/get_courses.php").body()

                _courses.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                client.close()
            }
        }
    }
}