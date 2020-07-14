package com.datikaa

import com.ryanharter.ktor.moshi.moshi
import com.squareup.moshi.Moshi
import io.ktor.application.*
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.locations.Locations
import io.ktor.request.path
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.get
import org.slf4j.event.Level

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(StatusPages)
    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

    install(Koin) {
        modules(ktorModules)
    }

    install(ContentNegotiation) {
        val moshi: Moshi = get()
        moshi(moshi)
    }

    install(Locations)
}