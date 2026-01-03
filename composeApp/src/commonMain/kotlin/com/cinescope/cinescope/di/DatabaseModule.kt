package com.cinescope.cinescope.di

import app.cash.sqldelight.db.SqlDriver
import com.cinescope.cinescope.data.local.database.CineScopeDatabase
import com.cinescope.cinescope.data.local.database.DatabaseMigrations
import org.koin.dsl.module

expect val platformDatabaseModule: org.koin.core.module.Module

val databaseModule = module {
    includes(platformDatabaseModule)

    single {
        val driver = get<SqlDriver>()

        DatabaseMigrations.applyMigrations(driver)

        CineScopeDatabase(driver = driver)
    }
}
