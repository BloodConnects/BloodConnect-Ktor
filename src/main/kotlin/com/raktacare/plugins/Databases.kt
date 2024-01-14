package com.raktacare.plugins

import com.raktacare.module.user.Users
import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases() {

    Class.forName("org.postgresql.Driver")
    val driverClassName = "org.postgresql.Driver"
    val jdbcURL = environment.config.property("ktor.postgres.url").getString()
    val user = environment.config.property("ktor.postgres.user").getString()
    val password = environment.config.property("ktor.postgres.password").getString()
    val database = Database.connect(jdbcURL, driverClassName, user, password)
    transaction(database) {
        SchemaUtils.create(Users)
    }
}

suspend fun <T> dbQuery(block: suspend () -> T): T = newSuspendedTransaction(Dispatchers.IO) { block() }