package com.cinescope.cinescope.data.local.database

import app.cash.sqldelight.db.SqlDriver

/**
 * Centralized database migration handler.
 *
 * This class manages all database schema migrations in a clean, modular way.
 * Each migration is isolated and version-controlled.
 */
object DatabaseMigrations {

    /**
     * Applies all necessary migrations to bring the database up to date.
     * This method is idempotent and safe to call multiple times.
     */
    fun applyMigrations(driver: SqlDriver) {
        migrateToVersion2(driver)
    }

    /**
     * Migration to version 2: Adds themePreference column to UserProfile table.
     *
     * This migration adds support for user theme preferences (System/Light/Dark).
     * Safe to run on databases that already have the column.
     */
    private fun migrateToVersion2(driver: SqlDriver) {
        try {
            driver.executeQuery(
                identifier = null,
                sql = "SELECT themePreference FROM UserProfile LIMIT 1",
                mapper = { app.cash.sqldelight.db.QueryResult.Value(Unit) },
                parameters = 0
            )
        } catch (e: Exception) {
            driver.execute(
                identifier = null,
                sql = "ALTER TABLE UserProfile ADD COLUMN themePreference TEXT DEFAULT 'SYSTEM'",
                parameters = 0,
                binders = null
            )
        }
    }
}
