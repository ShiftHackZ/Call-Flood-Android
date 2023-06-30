package com.shifthackz.redialer.app.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.shifthackz.redialer.app.ui.screen.dialer.DialerScreen
import com.shifthackz.redialer.app.ui.theme.CallFloodTheme
import org.koin.androidx.compose.koinViewModel


class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isGranted = permissions.entries.all {
            it.value
        }
        if (isGranted) {
            println("Everything was granted")
        } else {
            println("Call permission was denied")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ANSWER_PHONE_CALLS,
            )
        )
        setContent {
            CallFloodTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DialerScreen(koinViewModel()).Build()
                }
            }
        }
    }
//
//    private fun processIntent(intent: Intent) {
//        when (intent.action) {
//            Intent.ACTION_CALL -> {
//                startActivity(intent)
//                println("WILL FINISH CALL IN 20 sec...")
//                Handler(Looper.getMainLooper()).postDelayed({
//                    println("ATTEMPTING TO FINISH THE CALL...")
//                    val telecomManager = getSystemService(Context.TELECOM_SERVICE)
//                    val endCallMethod = telecomManager.javaClass.getDeclaredMethod("endCall")
//                    endCallMethod.isAccessible = true
//                    endCallMethod.invoke(telecomManager)
//                }, 20000L)
//            }
//        }
//    }
}
