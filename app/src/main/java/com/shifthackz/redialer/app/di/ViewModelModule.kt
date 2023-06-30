package com.shifthackz.redialer.app.di

import com.shifthackz.redialer.app.ui.screen.dialer.DialerViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::DialerViewModel)
}
