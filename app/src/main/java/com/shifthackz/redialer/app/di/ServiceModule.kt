package com.shifthackz.redialer.app.di

import com.shifthackz.redialer.app.service.flood.FloodService
import com.shifthackz.redialer.app.service.flood.FloodServiceImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val serviceModule = module {
    single<FloodService> {
        FloodServiceImpl(androidApplication())
    }
}
