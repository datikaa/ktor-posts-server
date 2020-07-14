package com.datikaa
import com.squareup.moshi.Moshi
import org.koin.dsl.module

val ktorModules = module {
    single<Moshi> {
        Moshi.Builder().build()
    }
}