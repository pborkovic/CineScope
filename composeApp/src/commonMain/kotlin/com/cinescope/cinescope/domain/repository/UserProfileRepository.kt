package com.cinescope.cinescope.domain.repository

import com.cinescope.cinescope.domain.model.ThemePreference
import com.cinescope.cinescope.domain.model.UserProfile
import com.cinescope.cinescope.domain.util.Result
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for user profile data access and management.
 *
 * Manages the persistence and retrieval of user profile information including
 * name, profile picture, and theme preference. Supports a single user profile per application.
 *
 * @see UserProfile
 */
interface UserProfileRepository {
    /**
     * Retrieves the user profile as a reactive stream.
     *
     * Returns a Flow that emits the current user profile and updates whenever
     * the profile changes. If no profile exists, emits a default profile.
     *
     * @return Flow emitting the current UserProfile
     */
    suspend fun getUserProfile(): Flow<UserProfile>

    /**
     * Updates the user's display name.
     *
     * @param name The new display name
     * @return Result.Success on successful update, or Result.Error on failure
     */
    suspend fun updateUserName(name: String): Result<Unit>

    /**
     * Updates the user's profile picture path.
     *
     * @param path Path to the profile picture file
     * @return Result.Success on successful update, or Result.Error on failure
     */
    suspend fun updateUserProfilePicture(path: String): Result<Unit>

    /**
     * Updates the user's theme preference.
     *
     * @param themePreference The new theme preference
     * @return Result.Success on successful update, or Result.Error on failure
     */
    suspend fun updateThemePreference(themePreference: ThemePreference): Result<Unit>

    /**
     * Saves or updates the complete user profile.
     *
     * @param profile The UserProfile to save
     * @return Result.Success on successful save, or Result.Error on failure
     */
    suspend fun saveUserProfile(profile: UserProfile): Result<Unit>
}
