package com.cinescope.cinescope.di

import com.cinescope.cinescope.BuildConfig
import com.cinescope.cinescope.data.remote.api.HttpClientFactory
import com.cinescope.cinescope.data.remote.api.TmdbApiClient
import org.koin.dsl.module

val networkModule = module {
    single { HttpClientFactory.create() }

    single {
        TmdbApiClient(
            httpClient = get(),
            apiKey = BuildConfig.TMDB_API_KEY
        )
    }
}
