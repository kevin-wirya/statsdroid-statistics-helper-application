package com.kevin.statsdroid.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
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
import com.kevin.statsdroid.ui.components.CltHistogramVisualizer
import com.kevin.statsdroid.utils.calculator.CltCalculator
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CltScreen(
    viewModel: CltViewModel = hiltViewModel(),
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
            text = "Central Limit Theorem Visualizer",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Pembuktian distribusi rata-rata sampel mendekati normal seiring bertambahnya n.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))
        // dropdown
        Text("Population Shape (Bentuk Populasi Awal):", fontWeight = FontWeight.SemiBold)
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            val distNames = listOf(
                "Uniform",
                "Exponential/Skewed",
                "Bimodal"
            )
            val currentText = distNames[uiState.selectedDistribution.ordinal]
            OutlinedTextField(
                value = currentText,
                onValueChange = {},
                readOnly = true,
                label = { Text("Bentuk Populasi") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = true)
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                distNames.forEachIndexed { idx, name ->
                    DropdownMenuItem(
                        text = { Text(name) },
                        onClick = {
                            viewModel.onDistributionChanged(CltCalculator.DistributionType.entries[idx])
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        // slider sample size
        val nVal = uiState.sampleSizeInput.toFloatOrNull() ?: 30f
        Text("Sample Size (n): ${nVal.toInt()}")
        Slider(
            value = nVal.coerceIn(1f, 100f),
            onValueChange = { viewModel.onSampleSizeChanged(it.toInt().toString()) },
            valueRange = 1f..100f
        )
        // number of samples (M) filter chips
        Text("Number of Samples (M): ${uiState.numSamplesInput}")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("100", "500", "1000", "5000").forEach { mOption ->
                FilterChip(
                    selected = uiState.numSamplesInput == mOption,
                    onClick = { viewModel.onNumSamplesChanged(mOption) },
                    label = { Text("M = $mOption") }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        // action button
        Button(
            onClick = { viewModel.generateSimulation() },
            modifier = Modifier.fillMaxWidth()
        ){
            Text("Simulate samples")
        }
        Spacer(modifier = Modifier.height(20.dp))
        // live summary metrics card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Live Summary Metrics",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text("Theoretical Mean (μ): ${String.format(Locale.US, "%.4f", uiState.theoreticalMean)}")
                Text("Empirical Mean (x̄_avg): ${String.format(Locale.US, "%.4f", uiState.empiricalMean)}")
                Text("Population Std Dev (σ): ${String.format(Locale.US, "%.4f", uiState.theoreticalStdDev)}")
                Text("Standard Error (SE = σ / √n): ${String.format(Locale.US, "%.4f", uiState.standardError)}")
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        // grafik 1: original population distribution
        Text(
            text = "Grafik 1: Original Population Distribution",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        CltHistogramVisualizer(
            sampleMeans = uiState.populationData,
            barColor = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(20.dp))
        // grafik 2: sampling distribution of sample means
        Text(
            text = "Grafik 2: Sampling Distribution of Sample Means",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Overlaid Theoretical Normal Bell Curve",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        CltHistogramVisualizer(
            sampleMeans = uiState.sampleMeans,
            barColor = MaterialTheme.colorScheme.primary
        )
    }
}
