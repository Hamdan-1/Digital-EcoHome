// app/src/main/java/com/trailblazers/digital-EcoHome/ui/home/HomeScreen.kt
package com.trailblazers.digitalecohome.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
// import androidx.compose.material3.Scaffold // REMOVE if unused import reported here
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle // **FIX: Add Import**
import androidx.lifecycle.viewmodel.compose.viewModel
import com.trailblazers.digitalecohome.R
import com.trailblazers.digitalecohome.ui.theme.DigitalEcoHomeTheme

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = viewModel()
) {
    val textState by homeViewModel.text.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // **FIX: Simplify Text call**
        Text(
            text = stringResource(R.string.menu_home),
            modifier = Modifier.padding(bottom = 8.dp), // Keep relevant modifiers
            style = MaterialTheme.typography.titleLarge // Use style primarily
        )
        // **FIX: Simplify Text call**
        Text(
            text = textState,
            style = MaterialTheme.typography.bodyMedium // Use style primarily
            // Add modifier, color etc. if needed and unambiguous
        )
        // Add your energy monitoring UI elements here
    }
}

// Preview code remains the same...
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    DigitalEcoHomeTheme {
        val previewViewModel = HomeViewModel()
        HomeScreen(homeViewModel = previewViewModel)
    }
}