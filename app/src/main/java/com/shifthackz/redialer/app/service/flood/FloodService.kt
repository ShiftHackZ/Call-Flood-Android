package com.shifthackz.redialer.app.service.flood

import kotlinx.coroutines.flow.StateFlow

interface FloodService {
    fun launch(number: String, mode: FloodMode, startDelay: Int, maxDuration: Int)
    fun stop()
    fun observeCount(): StateFlow<Int>
    fun observeState(): StateFlow<Boolean>
}
