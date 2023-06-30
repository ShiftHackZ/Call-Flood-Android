@file:OptIn(ExperimentalMaterial3Api::class)

package com.shifthackz.redialer.app.ui.screen.dialer

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shifthackz.redialer.app.R
import com.shifthackz.redialer.app.core.mvi.EmptyEffect
import com.shifthackz.redialer.app.core.mvi.MviScreen
import com.shifthackz.redialer.app.service.flood.FloodMode
import com.shifthackz.redialer.app.ui.component.DropdownTextField

class DialerScreen(
    private val viewModel: DialerViewModel,
) : MviScreen<DialerState, EmptyEffect>(viewModel) {

    @Composable
    override fun Content() {
        ScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            state = viewModel.state.collectAsStateWithLifecycle().value,
            onPhoneInputChanged = viewModel::onPhoneInputChanged,
            onModeSelected = viewModel::onModeChanged,
            onMaxCallsChanged = viewModel::onMaxCallsChanged,
            onStartDelayChanged = viewModel::onStartDelayChanged,
            onMaxDurationChanged = viewModel::onMaxDurationChanged,
            onToggleFlood = viewModel::toggleFloodState,
        )
    }
}

@Composable
private fun ScreenContent(
    modifier: Modifier = Modifier,
    state: DialerState,
    onPhoneInputChanged: (String) -> Unit = {},
    onModeSelected: (FloodMode) -> Unit = {},
    onMaxCallsChanged: (String) -> Unit = {},
    onStartDelayChanged: (String) -> Unit = {},
    onMaxDurationChanged: (String) -> Unit = {},
    onToggleFlood: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .scrollable(rememberScrollState(), Orientation.Vertical),
    ) {
        val contentModifier = Modifier
            .align(Alignment.CenterHorizontally)
            .fillMaxWidth(0.9f)

        val modeLoop = stringResource(id = R.string.mode_loop)
        val modeCount = stringResource(id = R.string.mode_count)
        val modes = listOf(modeLoop, modeCount)

        TextField(
            modifier = contentModifier.padding(top = 48.dp),
            value = state.phoneNumberInput,
            onValueChange = onPhoneInputChanged,
            enabled = !state.floodRunning,
//            isError = widthValidationError != null,
//            supportingText = { widthValidationError?.let { Text(it.asString()) } },
            label = { Text(stringResource(id = R.string.input_phone)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        )
        DropdownTextField(
            modifier = contentModifier.padding(top = 16.dp),
            label = "Mode",
            value = when (state.mode) {
                is FloodMode.Count -> modeCount
                FloodMode.Loop -> modeLoop
            },
            enabled = !state.floodRunning,
            items = modes,
            onItemSelected = {value ->
                val mode = when (value) {
                    modeLoop -> FloodMode.Loop
                    else -> FloodMode.Count(state.maxCallCountInput.toInt())
                }
                onModeSelected(mode)
            },
        )
        if (state.mode is FloodMode.Count) TextField(
            modifier = contentModifier.padding(top = 16.dp),
            value = state.maxCallCountInput,
            onValueChange = { value ->
                onMaxCallsChanged(value.filter { it.isDigit() })
            },
            enabled = !state.floodRunning,
//            isError = widthValidationError != null,
//            supportingText = { widthValidationError?.let { Text(it.asString()) } },
            label = { Text(stringResource(id = R.string.input_call_count)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )
        Row(
            modifier = contentModifier.padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            val rowModifier = Modifier.weight(1f)
            TextField(
                modifier = rowModifier,
                value = state.startDelaySecondsInput,
                onValueChange = { value ->
                    onStartDelayChanged(value.filter { it.isDigit() })
                },
                enabled = !state.floodRunning,
//            isError = widthValidationError != null,
//            supportingText = { widthValidationError?.let { Text(it.asString()) } },
                label = { Text(stringResource(id = R.string.input_delay_start)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
            TextField(
                modifier = rowModifier,
                value = state.maxDurationSecondsInput,
                onValueChange = { value ->
                    onMaxDurationChanged(value.filter { it.isDigit() })
                },
                enabled = !state.floodRunning,
//            isError = widthValidationError != null,
//            supportingText = { widthValidationError?.let { Text(it.asString()) } },
                label = { Text(stringResource(id = R.string.input_max_duration)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
        }
        Spacer(modifier = Modifier.weight(1f))

        Text(
            modifier = contentModifier,
            text = stringResource(
                R.string.calls_processed,
                "${state.processedCount}",
            ),
//            color = if (state.floodRunning) {
//                Color.Green
//            } else {
//                MaterialTheme.colorScheme.onBackground
//            },
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
        Text(
            modifier = contentModifier.padding(all = 32.dp),
            text = stringResource(
                if (state.floodRunning) R.string.status_running
                else R.string.status_not_running
            ),
            color = if (state.floodRunning) {
                Color.Green
            } else {
                MaterialTheme.colorScheme.onBackground
            },
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
        )
        Button(
            modifier = contentModifier
                .height(70.dp)
                .padding(bottom = 14.dp),
            onClick = onToggleFlood,
        ) {
            Icon(
                modifier = Modifier.size(18.dp),
                imageVector = Icons.Default.Call,
                contentDescription = "Call",
            )
            Text(
                modifier = Modifier.padding(start = 8.dp),
                style = MaterialTheme.typography.bodyLarge,
                text = if (state.floodRunning) "STOP FLOOD" else "START FLOOD"
            )
        }
        Text(
            modifier = contentModifier.padding(bottom = 2.dp),
            text = "ShiftHackZ © 2023 // Version: 0.0.1",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelSmall,
        )
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
private fun PreviewScreenContentEmpty() {
    ScreenContent(state = DialerState());
}
