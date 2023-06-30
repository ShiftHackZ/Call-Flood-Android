package com.shifthackz.redialer.app.ui.screen.dialer

import androidx.lifecycle.viewModelScope
import com.shifthackz.redialer.app.core.mvi.EmptyEffect
import com.shifthackz.redialer.app.core.mvi.MviViewModel
import com.shifthackz.redialer.app.service.flood.FloodMode
import com.shifthackz.redialer.app.service.flood.FloodService
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DialerViewModel(
    private val floodService: FloodService,
) : MviViewModel<DialerState, EmptyEffect>() {

    override val emptyState = DialerState()

    init {
        floodService
            .observeCount()
            .onEach {
                currentState.copy(processedCount = it).let(::setState)
            }
            .launchIn(viewModelScope)
    }

    fun onPhoneInputChanged(value: String) = currentState
        .copy(phoneNumberInput = value)
        .let(::setState)

    fun onModeChanged(value: FloodMode) = currentState
        .copy(mode = value)
        .let(::setState)

    fun onMaxCallsChanged(value: String) = currentState
        .copy(maxCallCountInput = value)
        .let(::setState)

    fun onStartDelayChanged(value: String) = currentState
        .copy(startDelaySecondsInput = value)
        .let(::setState)

    fun onMaxDurationChanged(value: String) = currentState
        .copy(maxDurationSecondsInput = value)
        .let(::setState)

    fun toggleFloodState() {
        if (!currentState.floodRunning) {
            floodService.launch(
                currentState.phoneNumberInput,
                currentState.mode,
                currentState.startDelaySecondsInput.toInt(),
                currentState.maxDurationSecondsInput.toInt(),
            )
        }
        else floodService.stop()
        currentState.copy(floodRunning = !currentState.floodRunning).let(::setState)
    }
}
