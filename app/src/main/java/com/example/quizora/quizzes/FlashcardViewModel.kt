package com.example.quizora.quizzes

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizora.data.FlashCard
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import io.ktor.serialization.kotlinx.json.*

class FlashcardViewModel : ViewModel() {

    private val _flashcards = MutableStateFlow<List<FlashCard>>(emptyList())
    val flashcards: StateFlow<List<FlashCard>> = _flashcards

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    init {
        fetchFlashcards()
    }

    fun fetchFlashcards() {
        viewModelScope.launch {
            try {
                val response: List<FlashCard> = client.get("http://192.168.1.9/quizora/REST/flashcard.php") {
                    accept(ContentType.Application.Json)
                }.body()
                Log.d("FlashcardViewModel", "Fetched flashcards: $response") // 👈 Add this
                _flashcards.value = response
            } catch (e: Exception) {
                Log.e("FlashcardViewModel", "Error fetching flashcards", e)
            }
        }
    }
}