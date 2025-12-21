package com.cinescope.cinescope.domain.util

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@OptIn(kotlin.time.ExperimentalTime::class)
actual object TimeProvider {
    actual fun now(): Instant = Clock.System.now()
}
