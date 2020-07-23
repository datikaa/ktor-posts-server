package com.datikaa.routes

import com.datikaa.model.UserRegistration
import com.datikaa.repositry.UserRepository
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.locations.*
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import org.koin.ktor.ext.inject

@KtorExperimentalLocationsAPI
@Location("v1/users")
class UserListRoute

@KtorExperimentalLocationsAPI
@Location("v1/user")
data class UserGetRoute(val id: Int)

@KtorExperimentalLocationsAPI
@Location("v1/user/create")
class UserCreateRoute

@KtorExperimentalLocationsAPI
@Location("v1/user/delete")
data class UserDeleteRoute(val id: Int)

@KtorExperimentalLocationsAPI
fun Route.user() {

    val userRepository: UserRepository by inject()

    get<UserListRoute> {
        val users = userRepository.getUsers()
        call.respond(users)
    }

    get<UserGetRoute> {
        val user = userRepository.getUser(it.id)
        call.respond(user ?: HttpStatusCode.NotFound)
    }

    post<UserCreateRoute> {
        val user = call.receive<UserRegistration>()
        userRepository.putUser(user)
    }

    delete<UserDeleteRoute> {
        userRepository.deleteUser(it.id)
        call.respond(HttpStatusCode.OK)
    }
}