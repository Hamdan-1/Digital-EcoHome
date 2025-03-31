// app/src/main/java/com/trailblazers/digital-EcoHome/ui/slideshow/SlideshowScreen.kt
package com.trailblazers.digitalecohome.ui.slideshow

// ... other imports
import androidx.compose.material3.Text // Ensure Text is imported
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
import androidx.compose.material3.MaterialTheme // Ensure MaterialTheme is imported
import androidx.compose.foundation.layout.* // For Column, padding, etc.

@Composable
fun SlideshowScreen(
    slideshowViewModel: SlideshowViewModel = viewModel()
) {
    val textState by slideshowViewModel.text.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // **FIX: Simplify Text call**
        Text(
            text = stringResource(R.string.menu_slideshow),
            modifier = Modifier.padding(bottom = 8.dp),
            style = MaterialTheme.typography.titleLarge
        )
        // **FIX: Simplify Text call**
        Text(
            text = textState,
            style = MaterialTheme.typography.bodyMedium
        )
        // Add Slideshow specific UI here
    }
}

@Preview(showBackground = true)
@Composable
fun SlideshowScreenPreview() {
    DigitalEcoHomeTheme {
        SlideshowScreen()
    }
}