package com.datikaa.routes

import com.datikaa.db.Users
import com.datikaa.db.rowToUser
import com.datikaa.dbTransaction
import com.datikaa.model.UserRegistration
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.locations.*
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

@KtorExperimentalLocationsAPI
@Location("v1/users")
class UserListRoute

@KtorExperimentalLocationsAPI
@Location("v1/user/create")
class UserCreateRoute

@KtorExperimentalLocationsAPI
@Location("v1/user/delete")
data class UserDeleteRoute(val id: Int)

@KtorExperimentalLocationsAPI
fun Route.user() {
    get<UserListRoute> {
        val users = dbTransaction {
            return@dbTransaction Users.selectAll().map { dbUser -> dbUser.rowToUser() }
        }
        call.respond(users)
    }

    post<UserCreateRoute> {
        val user = call.receive<UserRegistration>()
        dbTransaction {
            val newUserId = Users.insert {
                it[email] = user.email
                it[displayName] = user.displayName
                it[passwordHash] = user.passwordHash
            } get Users.id

            val newUser = Users.select { Users.id eq newUserId }.first().rowToUser()
            call.respond(newUser)
        }
    }

    delete<UserDeleteRoute> {
        dbTransaction {
            Users.deleteWhere { Users.id eq it.id }
        }
        call.respond(HttpStatusCode.OK)
    }
}