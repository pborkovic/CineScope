package com.cinescope.cinescope.domain.model

/**
 * Represents the user's profile information.
 *
 * @property id Database identifier (always 1 for single user)
 * @property name User's display name
 * @property profilePicturePath Path to the user's profile picture
 */
data class UserProfile(
    val id: Long = 1,
    val name: String? = null,
    val profilePicturePath: String? = null
)
