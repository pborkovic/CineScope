package com.cinescope.cinescope.domain.model

/**
 * Represents the user's theme preference for the application.
 *
 * @property SYSTEM Follow system theme (light/dark)
 * @property LIGHT Always use light theme
 * @property DARK Always use dark theme
 */
enum class ThemePreference {
    SYSTEM,
    LIGHT,
    DARK;

    companion object {
        /**
         * Converts a string representation to ThemePreference.
         * Returns SYSTEM as default if the string is invalid.
         */
        fun fromString(value: String?): ThemePreference {
            return when (value?.uppercase()) {
                "LIGHT" -> LIGHT
                "DARK" -> DARK
                "SYSTEM" -> SYSTEM
                else -> SYSTEM
            }
        }
    }

    /**
     * Returns a user-friendly display name for the theme preference.
     */
    fun toDisplayName(): String {
        return when (this) {
            SYSTEM -> "System"
            LIGHT -> "Light"
            DARK -> "Dark"
        }
    }
}
