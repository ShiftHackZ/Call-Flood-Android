package com.shifthackz.redialer.app.core.mvi

import androidx.compose.runtime.Composable

abstract class Screen {

    @Composable
    open fun Build() {
        Content()
    }

    @Composable
    protected abstract fun Content()
}
