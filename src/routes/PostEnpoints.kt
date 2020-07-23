package com.datikaa.routes

import com.datikaa.db.Posts
import com.datikaa.db.rowToPost
import com.datikaa.dbTransaction
import com.datikaa.model.NewPost
import com.datikaa.model.User
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.auth.principal
import io.ktor.http.HttpStatusCode
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll

@KtorExperimentalLocationsAPI
@Location("v1/post")
class SubmitPostRoute()

@KtorExperimentalLocationsAPI
@Location("v1/posts")
class PostListRoute

@KtorExperimentalLocationsAPI
fun Route.posts() {

    get<PostListRoute> {
        val posts = dbTransaction {
            return@dbTransaction Posts.selectAll().map { dbUser -> dbUser.rowToPost() }
        }
        call.respond(posts)
    }

    authenticate {
        post<SubmitPostRoute> {
            val user = call.principal<User>()
            if(user == null) {
                call.respond(HttpStatusCode.Forbidden)
                return@post
            }
            val newPost = call.receive<NewPost>()
            dbTransaction {
                Posts.insert {
                    it[userId] = user.id
                    it[message] = newPost.message
                }
            }
            call.respond(HttpStatusCode.OK)
        }
    }
}