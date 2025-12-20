package com.cinescope.cinescope.di

import org.koin.dsl.module

val appModule = module {
    includes(
        networkModule,
        databaseModule,
        repositoryModule,
        viewModelModule
    )
}
