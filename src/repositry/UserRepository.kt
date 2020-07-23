package com.datikaa.repositry

import com.datikaa.db.Users
import com.datikaa.dbTransaction
import com.datikaa.model.User
import com.datikaa.model.UserRegistration
import org.jetbrains.exposed.sql.*

interface UserRepository {
    suspend fun getUsers(): List<User>
    suspend fun getUser(id: Int): User?
    suspend fun putUser(user: UserRegistration): User
    suspend fun deleteUser(id: Int)
}

class UserRepositoryImpl(): UserRepository {

    private fun ResultRow.rowToUser() = User(
        id = this[Users.id],
        email = this[Users.email],
        displayName = this[Users.displayName],
        registrationDate = this[Users.registrationDate].toString()
    )

    override suspend fun getUser(id: Int): User? {
        return dbTransaction {
            return@dbTransaction Users.select { Users.id eq id }.firstOrNull()?.rowToUser()
        }
    }

    override suspend fun getUsers(): List<User> {
        return dbTransaction {
            return@dbTransaction Users.selectAll().map { dbUser -> dbUser.rowToUser() }
        }
    }

    override suspend fun putUser(user: UserRegistration): User {
        return dbTransaction {
            val newUserId = Users.insert {
                it[email] = user.email
                it[displayName] = user.displayName
                it[passwordHash] = user.passwordHash
            } get Users.id

           return@dbTransaction Users.select { Users.id eq newUserId }.first().rowToUser()
        }
    }

    override suspend fun deleteUser(id: Int) {
        dbTransaction {
            Users.deleteWhere { Users.id eq id }
        }
    }
}