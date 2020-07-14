package com.datikaa.routes

import com.datikaa.db.Users
import com.datikaa.db.rowToUser
import com.datikaa.dbTransaction
import com.datikaa.model.User
import com.datikaa.model.UserRegistration
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.locations.*
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.koin.ktor.ext.inject
import java.lang.reflect.Type

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
    val moshi: Moshi by inject()

    get<UserListRoute> {
        val users = dbTransaction {
            return@dbTransaction Users.selectAll().map { dbUser -> dbUser.rowToUser() }
        }
        val type: Type = Types.newParameterizedType(MutableList::class.java, User::class.java)
        val adapter: JsonAdapter<List<User>> = moshi.adapter(type)
        call.respond(adapter.toJson(users))
    }

    post<UserCreateRoute> {
        val user = call.receive<UserRegistration>()
        dbTransaction {
            val id = Users.insert {
                it[email] = user.email
                it[displayName] = user.displayName
            } get Users.id

        }
    }

    delete<UserDeleteRoute> {
        dbTransaction {
            Users.deleteWhere { Users.id eq it.id }
        }
        call.respond(HttpStatusCode.OK)
    }
}