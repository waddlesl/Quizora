package com.example.quizora.data

import kotlinx.serialization.Serializable


@Serializable
data class User(
    val id: Int,
    val name: String,
    val email: String,
    val role: String = "STUDENT",
    val xp: Int = 0,
    val streak: Int = 0,
    val class_code: String? = null
)
