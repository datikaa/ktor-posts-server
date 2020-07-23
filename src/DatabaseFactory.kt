package com.datikaa

import com.datikaa.db.Comments
import com.datikaa.db.Posts
import com.datikaa.db.Users
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    fun init() {
        Database.connect(hikari())

        transaction {
            SchemaUtils.create(Users)
            SchemaUtils.create(Posts)
            SchemaUtils.create(Comments)

            Users.insert {
                it[email] = "teszt@test.te"
                it[displayName] = "Teszt Name"
                it[passwordHash] = "pwHash"
            }

            Users.insert {
                it[email] = "teszt2@test.te"
                it[displayName] = "Teszt Name 2"
                it[passwordHash] = "pwHash"
            }
        }
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = "org.h2.Driver"
        config.jdbcUrl = "jdbc:h2:mem:test"
        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()
        return HikariDataSource(config)
    }
}

suspend fun <T> dbTransaction(block: suspend () -> T): T =
    newSuspendedTransaction(context = Dispatchers.IO) {
        block()
    }