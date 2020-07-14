package com.datikaa.model

import io.ktor.auth.Principal

data class User(
    val id: Int,
    val email: String,
    val displayName: String,
    val registrationDate: String
) : Principal

data class UserRegistration(
    val email: String,
    val displayName: String,
    val passwordHash: String
)

data class Post(
    val id: Int,
    val userId: Int,
    val creationDate: String,
    val message: String
)

data class Comment(
    val id: Int,
    val userId: Int,
    val postId: Int,
    val creationDate: String,
    val message: String
)