package com.trailblazers.digitalecohome.ui.slideshow

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
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
import com.trailblazers.digitalecohome.R
import com.trailblazers.digitalecohome.ui.theme.DigitalEcoHomeTheme

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
        Text(
            text = stringResource(R.string.menu_slideshow),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
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
        SlideshowScreen() // Assuming default ViewModel works for preview
    }
}