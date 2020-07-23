package com.datikaa.helpers

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.datikaa.model.UserRegistration
import org.koin.core.KoinComponent
import java.util.*

object JwtConfig : KoinComponent {

    private const val secret = "secretOfLifeIs42"
    private const val issuer = "com.datikaa"
    private const val validityInMs = 60_000 // 10 mins
    private val algorithm = Algorithm.HMAC512(secret)

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()

    fun generateToken(userRegistration: UserRegistration): String {
        return JWT.create()
            .withSubject("Authentication")
            .withIssuer(issuer)
            .withClaim("email", userRegistration.email)
            .withClaim("password", userRegistration.passwordHash)
            .withExpiresAt(getExpiration())
            .sign(algorithm)
    }

    private fun getExpiration() = Date(System.currentTimeMillis() + validityInMs)
}

