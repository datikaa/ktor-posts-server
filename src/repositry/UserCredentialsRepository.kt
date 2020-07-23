package com.datikaa.repositry

import com.datikaa.db.Users
import com.datikaa.dbTransaction
import com.datikaa.model.UserRegistration
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select

interface UserRegistrationRepository {
    suspend fun getUserCredentials(email: String, passwordHash: String): UserRegistration?
}

class UserRegistrationRepositoryImpl : UserRegistrationRepository {

    private fun ResultRow.rowToUserRegistration() = UserRegistration(
        email = this[Users.email],
        displayName = this[Users.displayName],
        passwordHash = this[Users.passwordHash].toString()
    )

    override suspend fun getUserCredentials(email: String, passwordHash: String): UserRegistration? {
        return dbTransaction {
            return@dbTransaction Users.select { Users.email.eq(email) and Users.passwordHash.eq(passwordHash) }.firstOrNull()?.rowToUserRegistration()
        }
    }
}