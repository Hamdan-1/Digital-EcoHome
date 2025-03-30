package com.trailblazers.digitalecohome.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.trailblazers.digitalecohome.R // Import your R class
import com.trailblazers.digitalecohome.ui.theme.DigitalEcoHomeTheme

@Composable
fun HomeScreen(
    // Pass NavController if needed for navigation from this screen
    // navController: NavController,
    homeViewModel: HomeViewModel = viewModel() // Get ViewModel instance
) {
    // Observe ViewModel state (example with a hypothetical text LiveData/StateFlow)
    val textState by homeViewModel.text.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.menu_home), // Example using string resource
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = textState, // Display data from ViewModel
            style = MaterialTheme.typography.bodyMedium
        )
        // Add your energy monitoring UI elements here:
        // Charts (using libraries like MPAndroidChart-Compose or Compose custom drawing)
        // Data displays (Text, Cards, Lists)
        // Control elements (Buttons, Switches)
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    DigitalEcoHomeTheme {
        // Provide a dummy ViewModel for preview if needed
        val previewViewModel = HomeViewModel() // Assuming constructor is simple
        // You might need to manually set preview data in the ViewModel
        HomeScreen(homeViewModel = previewViewModel)
    }
}