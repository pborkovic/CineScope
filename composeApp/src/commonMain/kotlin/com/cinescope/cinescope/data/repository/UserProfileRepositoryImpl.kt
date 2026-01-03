package com.cinescope.cinescope.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.cinescope.cinescope.data.local.database.CineScopeDatabase
import com.cinescope.cinescope.domain.model.ThemePreference
import com.cinescope.cinescope.domain.model.UserProfile
import com.cinescope.cinescope.domain.repository.UserProfileRepository
import com.cinescope.cinescope.domain.util.NetworkError
import com.cinescope.cinescope.domain.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserProfileRepositoryImpl(
    private val database: CineScopeDatabase
) : UserProfileRepository {

    override suspend fun getUserProfile(): Flow<UserProfile> {
        return try {
            database.cineScopeDatabaseQueries
                .getUserProfile()
                .asFlow()
                .mapToOneOrNull(Dispatchers.Default)
                .map { dbProfile ->
                    dbProfile?.let {
                        UserProfile(
                            id = it.id,
                            name = it.name,
                            profilePicturePath = it.profilePicturePath,
                            themePreference = ThemePreference.fromString(it.themePreference)
                        )
                    } ?: UserProfile()
                }
        } catch (e: Exception) {
            kotlinx.coroutines.flow.flowOf(UserProfile())
        }
    }

    override suspend fun updateUserName(name: String): Result<Unit> {
        return try {
            ensureProfileExists()
            database.cineScopeDatabaseQueries.updateUserName(name)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(NetworkError.Unknown(e.message ?: "Failed to update user name"))
        }
    }

    override suspend fun updateUserProfilePicture(path: String): Result<Unit> {
        return try {
            ensureProfileExists()
            database.cineScopeDatabaseQueries.updateUserProfilePicture(path)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(NetworkError.Unknown(e.message ?: "Failed to update profile picture"))
        }
    }

    override suspend fun updateThemePreference(themePreference: ThemePreference): Result<Unit> {
        return try {
            ensureProfileExists()
            database.cineScopeDatabaseQueries.updateThemePreference(themePreference.name)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(NetworkError.Unknown(e.message ?: "Failed to update theme preference"))
        }
    }

    override suspend fun saveUserProfile(profile: UserProfile): Result<Unit> {
        return try {
            database.cineScopeDatabaseQueries.insertOrUpdateUserProfile(
                name = profile.name,
                profilePicturePath = profile.profilePicturePath,
                themePreference = profile.themePreference.name
            )
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(NetworkError.Unknown(e.message ?: "Failed to save user profile"))
        }
    }

    private fun ensureProfileExists() {
        try {
            val existing = database.cineScopeDatabaseQueries.getUserProfile().executeAsOneOrNull()
            if (existing == null) {
                database.cineScopeDatabaseQueries.insertOrUpdateUserProfile(
                    name = null,
                    profilePicturePath = null,
                    themePreference = ThemePreference.SYSTEM.name
                )
            }
        } catch (e: Exception) {
        }
    }
}
