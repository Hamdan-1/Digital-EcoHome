package com.trailblazers.digitalecohome.ui.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {
    // **FIX: Use StateFlow**
    private val _text = MutableStateFlow("This is home screen (from StateFlow)") // Initial value
    val text: StateFlow<String> = _text.asStateFlow() // Expose as immutable StateFlow
}