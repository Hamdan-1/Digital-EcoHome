package com.trailblazers.digitalecohome.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen() {
    Column {
        Text("Current Consumption: 3.45 kWh")
        Text("Daily Average: 4.12 kWh")
        Text("Monthly Usage: 125.67 kWh")
        Text("Estimated Savings: 15.20 AED")
        Text("High energy consumption detected in the Living Room")
        Text("Bedroom AC is offline")
    }
}
