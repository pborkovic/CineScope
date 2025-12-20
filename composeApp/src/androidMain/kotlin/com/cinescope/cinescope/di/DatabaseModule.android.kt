package com.cinescope.cinescope.di

import app.cash.sqldelight.db.SqlDriver
import com.cinescope.cinescope.data.local.database.DatabaseDriverFactory
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformDatabaseModule: Module = module {
    single<SqlDriver> {
        DatabaseDriverFactory(context = get()).createDriver()
    }
}
