package com.datikaa
import com.datikaa.repositry.UserRegistrationRepository
import com.datikaa.repositry.UserRegistrationRepositoryImpl
import com.datikaa.repositry.UserRepository
import com.datikaa.repositry.UserRepositoryImpl
import org.koin.dsl.module

val ktorModules = module {
    single<UserRepository> { UserRepositoryImpl() }
    single<UserRegistrationRepository> { UserRegistrationRepositoryImpl() }
}