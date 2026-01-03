package com.cinescope.cinescope.data.local.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = CineScopeDatabase.Schema,
            context = context,
            name = "cinescope.db",

            callback = AndroidSqliteDriver.Callback(CineScopeDatabase.Schema)
        )
    }
}
