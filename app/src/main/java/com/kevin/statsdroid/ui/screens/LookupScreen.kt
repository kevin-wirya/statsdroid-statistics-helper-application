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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kevin.statsdroid.ui.components.NormalCurveVisualizer
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LookupScreen(
    viewModel: LookupViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    var expanded by remember { mutableStateOf(false) }
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
            text = "Perhitungan nilai probabilitas dan kumulatif secara presisi.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        // dropdown selection
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            val options = listOf(
                "Binomial Probability Sums",
                "Poisson Probability Sums",
                "Area Under Normal Curve (Standar Z)"
            )
            val currentText = options[uiState.selectedDistribution.ordinal]

            OutlinedTextField(
                value = currentText,
                onValueChange = {},
                readOnly = true,
                label = { Text("Pilih Jenis Distribusi") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = true)
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEachIndexed { index, selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption) },
                        onClick = {
                            viewModel.onDistributionSelected(DistributionType.entries[index])
                            expanded = false
                        }
                    )
                }
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
            value = nVal.coerceIn(1f, 20f),
            onValueChange = { viewModel.onNInputChanged(it.toInt().toString()) },
            valueRange = 1f..20f
        )

        val pVal = uiState.pInput.toFloatOrNull() ?: 0.5f
        Text("Probabilitas Sukses (p): ${String.format(Locale.US, "%.2f", pVal)}")
        Slider(
            value = pVal.coerceIn(0.1f, 0.9f),
            onValueChange = { viewModel.onPInputChanged(String.format(Locale.US, "%.2f", it)) },
            valueRange = 0.1f..0.9f
        )

        val kVal = uiState.kBinomialInput.toFloatOrNull() ?: 5f
        val maxK = (uiState.nInput.toFloatOrNull() ?: 10f).coerceAtLeast(1f)
        Text("Success Threshold (r / k): ${uiState.kBinomialInput}")
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
        Text("Average Rate (μ / λ): ${String.format(Locale.US, "%.1f", lambdaVal)}")
        Slider(
            value = lambdaVal.coerceIn(0.1f, 100f),
            onValueChange = { viewModel.onLambdaInputChanged(String.format(Locale.US, "%.1f", it)) },
            valueRange = 0.1f..100f
        )

        val kVal = uiState.kPoissonInput.toFloatOrNull() ?: 2f
        Text("Success Threshold (r / k): ${uiState.kPoissonInput}")
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
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Informasi: Menggunakan Distribusi Normal Standar Z (Rerata μ = 0.0, Standar Deviasi σ = 1.0)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.padding(12.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        val zVal = uiState.zInput.toFloatOrNull() ?: 1.96f
        Text("Nilai Z-Score: ${String.format(Locale.US, "%.2f", zVal)}")
        Slider(
            value = zVal.coerceIn(-5.0f, 5.0f),
            onValueChange = { viewModel.onZInputChanged(String.format(Locale.US, "%.2f", it)) },
            valueRange = -5.0f..5.0f
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