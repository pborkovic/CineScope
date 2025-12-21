package com.cinescope.cinescope.domain.util

import kotlinx.datetime.Instant
import platform.Foundation.NSDate
import platform.Foundation.timeIntervalSince1970

@OptIn(kotlin.time.ExperimentalTime::class)
actual object TimeProvider {
    actual fun now(): Instant {
        val timestamp = NSDate().timeIntervalSince1970()
        return Instant.fromEpochSeconds((timestamp).toLong(), ((timestamp % 1.0) * 1_000_000_000).toInt())
    }
}
