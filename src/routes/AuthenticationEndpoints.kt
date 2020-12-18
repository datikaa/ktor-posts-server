package com.datikaa.routes

import com.datikaa.helpers.JwtConfig
import com.datikaa.model.UserAuthentication
import com.datikaa.repositry.UserRegistrationRepository
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.locations.*
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import org.koin.core.component.KoinApiExtension
import org.koin.ktor.ext.inject

@KtorExperimentalLocationsAPI
@Location("v1/authentication")
class AuthenticationRoute

@KtorExperimentalLocationsAPI
fun Route.authentication() {

    val registrationRepository: UserRegistrationRepository by inject()

    get<AuthenticationRoute> {
        val authentication = call.receive<UserAuthentication>()
        val credentials = registrationRepository.getUserCredentials(authentication.email, authentication.passwordHash)
        call.respond(
            if (credentials == null) {
                HttpStatusCode.NotFound
            } else {
                JwtConfig.generateToken(credentials)
            }
        )
    }
}