package com.example.quizora.data

import kotlinx.serialization.Serializable

@Serializable
data class FlashCard(
    val answer: String,
    val question: String
)