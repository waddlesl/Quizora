package com.example.quizora.data

import kotlinx.serialization.Serializable

@Serializable
data class FlashCard(
    val word: String,
    val define: String
)