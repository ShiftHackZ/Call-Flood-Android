@file:OptIn(ExperimentalMaterial3Api::class)

package com.shifthackz.redialer.app.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun <T : Any> DropdownTextField(
    modifier: Modifier = Modifier,
    label: String = "",
    enabled: Boolean = true,
    value: T,
    items: List<T> = emptyList(),
    onItemSelected: (T) -> Unit = {},
    displayDelegate: (T) -> String = { t -> "$t" },
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { if (enabled) expanded = !expanded },
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            value = displayDelegate(value),
            onValueChange = {},
            enabled = enabled,
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(displayDelegate(item)) },
                    onClick = {
                        expanded = false
                        if (value == item) return@DropdownMenuItem
                        onItemSelected(item)
                    },
                )
            }
        }
    }
}
