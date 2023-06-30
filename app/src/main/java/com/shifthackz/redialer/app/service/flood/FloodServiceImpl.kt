package com.shifthackz.redialer.app.service.flood

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.os.PowerManager
import android.os.PowerManager.WakeLock
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FloodServiceImpl(private val context: Context) : FloodService {

    private val powerManager: PowerManager
        get() = context.getSystemService(Context.POWER_SERVICE) as PowerManager

    private val processedCountStateFlow = MutableStateFlow(0)

    private var wakeLock: WakeLock? = null

    private var phoneNumber = ""
    private var floodMode: FloodMode = FloodMode.Loop

    private var processedCount = 0
        set(value) {
            processedCountStateFlow.tryEmit(value)
            field = value
        }

    private var targetCount = -1

    private var startDelay: Long = 5_000
    private var maxDuration: Long = 20_000

    private var isRunning = false

    override fun launch(number: String, mode: FloodMode, startDelay: Int, maxDuration: Int) {
        log("Launching flood with parameters:\nPhone: $number\nMode: $mode")

        phoneNumber = number
        floodMode = mode

        processedCount = 0
        if (mode is FloodMode.Count) targetCount = mode.count

        this.startDelay = startDelay * 1000L
        this.maxDuration = maxDuration * 1000L

        isRunning = true
        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "floodapp:CallFloodWl");
        wakeLock?.acquire(Long.MAX_VALUE)
        startCall()
    }

    override fun stop() {
        log("Flood stopped.")
        isRunning = false
        wakeLock?.release()
    }

    override fun observeCount(): StateFlow<Int> {
        return processedCountStateFlow.asStateFlow()
    }

    private fun startCall() {
        try {
            log("Attempting to start call...")
            val intent = Intent(Intent.ACTION_CALL).apply {
                data = Uri.parse("tel:${phoneNumber}")
                addFlags(FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
            log("Call started!")
        } finally {
            onCallStarted()
        }
    }

    private fun finishCall() {
        try {
            log("Attempting to finish call...")
            val telecomManager = context.getSystemService(Context.TELECOM_SERVICE)
            val endCallMethod = telecomManager.javaClass.getDeclaredMethod("endCall")
            endCallMethod.isAccessible = true
            endCallMethod.invoke(telecomManager)
            log("Call finished!")
        } finally {
            onCallFinished()
        }
    }

    private fun onCallStarted() {
        processedCount++
        log("Call was started, finishing it in 20 seconds.")
        CoroutineScope(Dispatchers.Main).launch {
            delay(maxDuration)
            finishCall()
        }
    }

    private fun onCallFinished() {
        val action: () -> Unit = {
            if (isRunning) when (floodMode) {
                is FloodMode.Count -> {
                    if (processedCount <= targetCount) {
                        startCall()
                    } else {
                        isRunning = false
                    }
                }
                FloodMode.Loop -> startCall()
            }
        }
        CoroutineScope(Dispatchers.Main).launch {
            delay(startDelay)
            action()
        }
    }

    private fun log(message: String) = Log.d(FloodService::class.simpleName, message)
}
