package com.datikaa

import com.datikaa.helpers.JwtConfig
import com.datikaa.repositry.UserRepository
import com.datikaa.routes.authentication
import com.datikaa.routes.user
import io.ktor.application.*
import io.ktor.auth.Authentication
import io.ktor.auth.jwt.jwt
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.locations.Locations
import io.ktor.request.path
import io.ktor.response.respondText
import io.ktor.routing.routing
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject
import org.slf4j.event.Level

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(StatusPages) {
        exception<Throwable> { cause ->
            call.respondText(cause.localizedMessage, ContentType.Text.Plain, HttpStatusCode.InternalServerError)
            throw cause
        }
    }
    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

    install(Authentication) {
        jwt {
            verifier(JwtConfig.verifier)
            realm = "com.datikaa"
            validate {
                val userRepository: UserRepository by inject()
                val email = it.payload.getClaim("email").asString()
                val password = it.payload.getClaim("password").asString()
                return@validate userRepository.getUser(email, password)
            }
        }
    }

    install(Koin) {
        modules(ktorModules)
    }

    install(ContentNegotiation) {
        gson()
    }

    install(Locations)

    routing {
        authentication()
        user()
    }

    DatabaseFactory.init()
}