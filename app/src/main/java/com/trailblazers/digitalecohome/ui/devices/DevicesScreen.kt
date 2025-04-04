// app/src/main/java/com/trailblazers/digitalecohome/ui/devices/DevicesScreen.kt
package com.trailblazers.digitalecohome.ui.devices

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DevicesScreen() {
    // Simulated list of devices
    val devices = listOf("Air Conditioner", "Washing Machine", "Vacuum Cleaner")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(devices) { device ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Text(
                    text = device,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                Button(onClick = { /* Simulate device toggle action */ }) {
                    Text("Toggle")
                }
            }
        }
    }
}

