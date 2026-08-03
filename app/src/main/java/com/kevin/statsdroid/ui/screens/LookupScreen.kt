package com.kevin.statsdroid.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kevin.statsdroid.ui.components.NormalCurveVisualizer
import java.util.Locale

@Composable
fun LookupScreen(
    viewModel: LookupViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Tabel Distribusi Probabilitas",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Hitung nilai probabilitas & kumulatif secara presisi.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tab Pilihan Distribusi
        val tabs = listOf("Binomial", "Poisson", "Normal Standar (Z)")
        val selectedTabIndex = uiState.selectedDistribution.ordinal

        PrimaryTabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = {
                        viewModel.onDistributionSelected(DistributionType.entries[index])
                    },
                    text = { Text(title) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Form Input Sesuai Mode
        when (uiState.selectedDistribution) {
            DistributionType.BINOMIAL -> {
                BinomialInputForm(uiState = uiState, viewModel = viewModel)
            }
            DistributionType.POISSON -> {
                PoissonInputForm(uiState = uiState, viewModel = viewModel)
            }
            DistributionType.NORMAL -> {
                NormalInputForm(uiState = uiState, viewModel = viewModel)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Kartu Hasil
        ResultCard(uiState = uiState)
    }
}

@Composable
private fun BinomialInputForm(uiState: LookupUiState, viewModel: LookupViewModel) {
    Column {
        val nVal = uiState.nInput.toFloatOrNull() ?: 10f
        Text("Jumlah Percobaan (n): ${uiState.nInput}")
        Slider(
            value = nVal.coerceIn(1f, 100f),
            onValueChange = { viewModel.onNInputChanged(it.toInt().toString()) },
            valueRange = 1f..100f
        )

        val pVal = uiState.pInput.toFloatOrNull() ?: 0.5f
        Text("Probabilitas Sukses (p): ${String.format(Locale.US, "%.2f", pVal)}")
        Slider(
            value = pVal.coerceIn(0.01f, 0.99f),
            onValueChange = { viewModel.onPInputChanged(String.format(Locale.US, "%.2f", it)) },
            valueRange = 0.01f..0.99f
        )

        val kVal = uiState.kBinomialInput.toFloatOrNull() ?: 5f
        val maxK = (uiState.nInput.toFloatOrNull() ?: 10f).coerceAtLeast(1f)
        Text("Jumlah Sukses (k): ${uiState.kBinomialInput}")
        Slider(
            value = kVal.coerceIn(0f, maxK),
            onValueChange = { viewModel.onKBinomialInputChanged(it.toInt().toString()) },
            valueRange = 0f..maxK
        )
    }
}

@Composable
private fun PoissonInputForm(uiState: LookupUiState, viewModel: LookupViewModel) {
    Column {
        val lambdaVal = uiState.lambdaInput.toFloatOrNull() ?: 3.0f
        Text("Rata-rata Kejadian (λ): ${String.format(Locale.US, "%.1f", lambdaVal)}")
        Slider(
            value = lambdaVal.coerceIn(0.1f, 20f),
            onValueChange = { viewModel.onLambdaInputChanged(String.format(Locale.US, "%.1f", it)) },
            valueRange = 0.1f..20f
        )

        val kVal = uiState.kPoissonInput.toFloatOrNull() ?: 2f
        Text("Jumlah Kejadian (k): ${uiState.kPoissonInput}")
        Slider(
            value = kVal.coerceIn(0f, 30f),
            onValueChange = { viewModel.onKPoissonInputChanged(it.toInt().toString()) },
            valueRange = 0f..30f
        )
    }
}

@Composable
private fun NormalInputForm(uiState: LookupUiState, viewModel: LookupViewModel) {
    Column {
        val zVal = uiState.zInput.toFloatOrNull() ?: 1.96f
        Text("Nilai Z-Score: ${String.format(Locale.US, "%.2f", zVal)}")
        Slider(
            value = zVal.coerceIn(-3.5f, 3.5f),
            onValueChange = { viewModel.onZInputChanged(String.format(Locale.US, "%.2f", it)) },
            valueRange = -3.5f..3.5f
        )

        Spacer(modifier = Modifier.height(12.dp))
        NormalCurveVisualizer(zCutoff = zVal.toDouble())
    }
}

@Composable
private fun ResultCard(uiState: LookupUiState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Hasil Perhitungan",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (uiState.selectedDistribution != DistributionType.NORMAL) {
                Text(
                    text = "P(X = k) = ${String.format(Locale.US, "%.4f", uiState.resultPmf)}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Text(
                text = "P(X <= k / Z <= z) = ${String.format(Locale.US, "%.4f", uiState.resultCdfLower)}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "P(X >= k / Z >= z) = ${String.format(Locale.US, "%.4f", uiState.resultCdfUpper)}",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}