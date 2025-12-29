package com.cinescope.cinescope.di

import org.koin.dsl.module

/**
 * Koin module for validator dependencies.
 *
 * All validators are registered as factories to ensure stateless validation.
 * Validators should not maintain state between invocations.
 */
val validationModule = module {
    factory { com.cinescope.cinescope.domain.validation.MovieSearchValidator() }
    factory { com.cinescope.cinescope.domain.validation.RatingValidator() }
}
