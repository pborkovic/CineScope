package com.cinescope.cinescope.data.local.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(
            schema = CineScopeDatabase.Schema,
            name = "cinescope.db"
        )
    }
}
