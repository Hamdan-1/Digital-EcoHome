// app/src/main/java/com/trailblazers/digitalecohome/ui/dashboard/HomeViewModel.kt
package com.trailblazers.digitalecohome.ui.dashboard

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {
    // Simulated energy usage in kWh (you can later replace this with real sensor data)
    private val _energyUsage = MutableStateFlow(12.5)
    val energyUsage: StateFlow<Double> = _energyUsage.asStateFlow()

    // Simulated count of devices that are on
    private val _devicesOn = MutableStateFlow(2)
    val devicesOn: StateFlow<Int> = _devicesOn.asStateFlow()
}
