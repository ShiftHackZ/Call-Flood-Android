package com.shifthackz.redialer.app.ui.screen.dialer

import com.shifthackz.redialer.app.core.mvi.MviState
import com.shifthackz.redialer.app.service.flood.FloodMode

data class DialerState(
    val phoneNumberInput: String = "",
    val mode: FloodMode = FloodMode.Loop,
    val maxCallCountInput: String = "100",
    val startDelaySecondsInput: String = "5",
    val maxDurationSecondsInput: String = "20",
    val processedCount: Int = 0,
    val floodRunning: Boolean = false,
) : MviState
