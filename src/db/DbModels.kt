package com.datikaa.db

import com.datikaa.model.Comment
import com.datikaa.model.Post
import com.datikaa.model.User
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.CurrentDateTime
import org.jetbrains.exposed.sql.`java-time`.datetime

object Users : Table() {
    val id = integer("id").autoIncrement()
    val email = varchar("email", 255).uniqueIndex()
    val displayName = varchar("display_name", 255)
    val passwordHash = text("password_hash")
    val registrationDate = datetime("registration_date").defaultExpression(CurrentDateTime())

    override val primaryKey = PrimaryKey(id)
}

object Posts : Table() {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id") references Users.id
    val creationDate = datetime("creation_date").defaultExpression(CurrentDateTime())
    val message = text("message")

    override val primaryKey = PrimaryKey(id)
}

object Comments : Table() {
    val id = integer("id").autoIncrement()
    val userId = integer("userId") references Users.id
    val postId = integer("postId") references Posts.id
    val creationDate = datetime("creation_date").defaultExpression(CurrentDateTime())
    val message = text("message")

    override val primaryKey = PrimaryKey(id)
}

object Tokens : Table() {
    val token = text("token")
    val userId = integer("userId") references Users.id

    override val primaryKey = PrimaryKey(token)
}


fun ResultRow.rowToPost() = Post(
    id = this[Posts.id],
    userId = this[Posts.userId],
    creationDate = this[Posts.creationDate].toString(),
    message = this[Posts.message]
)

fun ResultRow.rowToComment() = Comment(
    id = this[Comments.id],
    userId = this[Comments.userId],
    postId = this[Comments.postId],
    creationDate = this[Comments.creationDate].toString(),
    message = this[Comments.message]
)
