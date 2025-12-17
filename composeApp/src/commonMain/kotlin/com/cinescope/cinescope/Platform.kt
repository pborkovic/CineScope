package com.cinescope.cinescope

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform