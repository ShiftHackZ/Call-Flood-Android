package com.shifthackz.redialer.app

import android.app.Application
import com.shifthackz.redialer.app.di.serviceModule
import com.shifthackz.redialer.app.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CallSpammerApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CallSpammerApp)
            modules(
                serviceModule,
                viewModelModule,
            )
        }
    }
}
