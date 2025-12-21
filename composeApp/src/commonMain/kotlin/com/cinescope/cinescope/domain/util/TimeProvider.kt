package com.cinescope.cinescope.domain.util

import kotlinx.datetime.Instant

/**
 * Multiplatform time provider to work around iOS Native Clock.System issues
 */
@OptIn(kotlin.time.ExperimentalTime::class)
expect object TimeProvider {
    fun now(): Instant
}
