package com.trailblazers.digitalecohome.ui.report

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class EnergyReportViewModel : ViewModel() {
    // Simulated energy report data; update this with real calculations later.
    private val _reportText = MutableStateFlow(
        "Simulated Energy Report Data:\nYour energy consumption is stable with a slight dip on weekends."
    )
    val reportText: StateFlow<String> = _reportText.asStateFlow()
}
