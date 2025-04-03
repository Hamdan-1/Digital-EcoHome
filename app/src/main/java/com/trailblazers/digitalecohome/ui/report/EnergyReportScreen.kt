package com.trailblazers.digitalecohome.ui.report
import androidx.compose.foundation.layout.*
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
fun EnergyReportScreen(
    energyReportViewModel: EnergyReportViewModel = viewModel()
) {
    val reportText by energyReportViewModel.reportText.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.menu_report),
            modifier = Modifier.padding(bottom = 8.dp),
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = reportText,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EnergyReportScreenPreview() {
    DigitalEcoHomeTheme {
        EnergyReportScreen()
    }
}
