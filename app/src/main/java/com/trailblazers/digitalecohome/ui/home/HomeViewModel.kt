package com.trailblazers.digitalecohome.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.math.roundToInt // Import for rounding

class HomeViewModel : ViewModel() {

    // --- Existing Text LiveData ---
    private val _text = MutableLiveData<String>().apply {
        value = "Digital EcoHome Dashboard" // Updated text
    }
    val text: LiveData<String> = _text

    // --- Constants for Calculations ---
    private val totalAppliances = 10 // Example: Total appliances tracked
    // --- UAE Cost Simulation Constants (FIX: Removed 'const') ---
    private val AVG_WATTS_PER_APPLIANCE = 200.0 // Average watts per active device (Simulated)
    private val AED_PER_KWH = 0.23         // Simplified avg rate in AED (Simulated)
    private val MAX_HOURLY_RATE_AED = 3.00 // Rate in AED/hr representing 100% on the bar

    // --- LiveData for Active Appliance Count & Percentage ---
    private val _activeAppliancesCount = MutableLiveData<Int>()
    val activeAppliancesCount: LiveData<Int> = _activeAppliancesCount

    private val _activePercentage = MutableLiveData<Int>()
    val activePercentage: LiveData<Int> = _activePercentage

    // --- LiveData for Cost Rate Percentage ---
    private val _costRatePercentage = MutableLiveData<Int>()
    val costRatePercentage: LiveData<Int> = _costRatePercentage

    // Initialize values
    init {
        _activeAppliancesCount.value = 0 // Start with 0 appliances ON
        updateCalculations()            // Calculate initial percentages (Active % and Cost %)
    }

    // --- Combined Calculation Function ---
    private fun updateCalculations() {
        val count = _activeAppliancesCount.value ?: 0

        // 1. Calculate Active Percentage
        val activePercent = if (totalAppliances > 0) {
            (count.toFloat() / totalAppliances.toFloat() * 100).roundToInt()
        } else {
            0
        }
        _activePercentage.value = activePercent.coerceIn(0, 100)

        // 2. Calculate Cost Rate Percentage (UAE Simulation)
        val currentTotalWatts = count * AVG_WATTS_PER_APPLIANCE
        val currentTotalKw = currentTotalWatts / 1000.0
        val currentCostAedPerHour = currentTotalKw * AED_PER_KWH

        // Calculate percentage relative to the defined maximum rate
        val costPercent = if (MAX_HOURLY_RATE_AED > 0) {
            (currentCostAedPerHour / MAX_HOURLY_RATE_AED * 100.0).roundToInt()
        } else {
            0
        }
        _costRatePercentage.value = costPercent.coerceIn(0, 100) // Update LiveData
    }


    // --- Simulation Functions ---
    fun simulateApplianceTurnedOn() {
        val currentCount = _activeAppliancesCount.value ?: 0
        if (currentCount < totalAppliances) {
            _activeAppliancesCount.value = currentCount + 1
            updateCalculations() // Recalculate Active % AND Cost %
        }
    }

    fun simulateApplianceTurnedOff() {
        val currentCount = _activeAppliancesCount.value ?: 0
        if (currentCount > 0) {
            _activeAppliancesCount.value = currentCount - 1
            updateCalculations() // Recalculate Active % AND Cost %
        }
    }
}