package com.datikaa.model

import com.squareup.moshi.JsonClass
import io.ktor.auth.Principal

@JsonClass(generateAdapter = true)
data class User(
    val id: Int,
    val email: String,
    val displayName: String,
    val registrationDate: String
) : Principal

@JsonClass(generateAdapter = true)
data class UserRegistration(
    val id: Int,
    val email: String,
    val displayName: String,
    val passwordHash: String
)

@JsonClass(generateAdapter = true)
data class Post(
    val id: Int,
    val userId: Int,
    val creationDate: String,
    val message: String
)

@JsonClass(generateAdapter = true)
data class Comment(
    val id: Int,
    val userId: Int,
    val postId: Int,
    val creationDate: String,
    val message: String
)