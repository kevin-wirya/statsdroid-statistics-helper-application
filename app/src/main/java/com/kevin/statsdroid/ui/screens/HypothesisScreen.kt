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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kevin.statsdroid.ui.components.NormalCurveVisualizer
import com.kevin.statsdroid.utils.calculator.HypothesisTestType
import com.kevin.statsdroid.utils.calculator.TailType
import java.util.Locale

@Composable
fun HypothesisScreen(
    viewModel: HypothesisViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
){
    val uiState by viewModel.uiState.collectAsState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Visual Hypothesis Tester",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Pengujian hipotesis (Z-Test & t-Test) beserta visualisasi Rejection Region.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        // pilihan jenis uji statistik
        Text("Pilih Jenis Uji Statistik:", fontWeight = FontWeight.SemiBold)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                RadioButton(
                    selected = uiState.testType == HypothesisTestType.Z_TEST,
                    onClick = {viewModel.onTestTypeChanged(HypothesisTestType.Z_TEST)}
                )
                Text("Z-Test (σ)")
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ){
                RadioButton(
                    selected = uiState.testType == HypothesisTestType.T_TEST,
                    onClick = {viewModel.onTestTypeChanged(HypothesisTestType.T_TEST)}
                )
                Text("t-Test (s)")
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        // pilihan tail type
        Text("Tail Type (Hₐ):", fontWeight = FontWeight.SemiBold)
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ){
            FilterChip(
                selected = uiState.tailType == TailType.TWO_TAILED,
                onClick = {viewModel.onTailTypeChanged(TailType.TWO_TAILED)},
                label = {Text("Two-tailed (≠)")}
            )
            FilterChip(
                selected = uiState.tailType == TailType.RIGHT_TAILED,
                onClick = {viewModel.onTailTypeChanged(TailType.RIGHT_TAILED)},
                label = {Text("Right (>)")}
            )
            FilterChip(
                selected = uiState.tailType == TailType.LEFT_TAILED,
                onClick = {viewModel.onTailTypeChanged(TailType.LEFT_TAILED)},
                label = {Text("Left (<)")}
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        // input slider
        val sampleMeanVal = uiState.sampleMeanInput.toFloatOrNull() ?: 105f
        Text("Sample Mean (x̄): ${String.format(Locale.US, "%.1f", sampleMeanVal)}")
        Slider(
            value = sampleMeanVal.coerceIn(-100f, 100f),
            onValueChange = {viewModel.onSampleMeanChanged(String.format(Locale.US, "%.1f", it))},
            valueRange = -100f..100f
        )
        val popMeanVal = uiState.popMeanInput.toFloatOrNull() ?: 100f
        Text("Hypothesized Mean (μ₀): ${String.format(Locale.US, "%.1f", popMeanVal)}")
        Slider(
            value = popMeanVal.coerceIn(-100f, 100f),
            onValueChange = {viewModel.onPopMeanChanged(String.format(Locale.US, "%.1f", it))},
            valueRange = -100f..100f
        )
        if (uiState.testType == HypothesisTestType.Z_TEST) {
            val popStdDevVal = uiState.popStdDevInput.toFloatOrNull() ?: 15f
            Text("Population Std Dev (σ): ${String.format(Locale.US, "%.1f", popStdDevVal)}")
            Slider(
                value = popStdDevVal.coerceIn(0.1f, 50f),
                onValueChange = {viewModel.onPopStdDevChanged(String.format(Locale.US, "%.1f", it))},
                valueRange = 0.1f..50f
            )
        } else {
            val sampleStdDevVal = uiState.sampleStdDevInput.toFloatOrNull() ?: 15f
            Text("Sample Std Dev (s): ${String.format(Locale.US, "%.1f", sampleStdDevVal)}")
            Slider(
                value = sampleStdDevVal.coerceIn(0.1f, 50f),
                onValueChange = {viewModel.onSampleStdDevChanged(String.format(Locale.US, "%.1f", it))},
                valueRange = 0.1f..50f
            )
        }
        val minN = if (uiState.testType == HypothesisTestType.Z_TEST) 1f else 2f
        val nVal = uiState.nInput.toFloatOrNull() ?: 30f
        Text("Sample Size (n): ${nVal.toInt()}${if (uiState.testType == HypothesisTestType.T_TEST) " (df = ${nVal.toInt() - 1})" else ""}")
        Slider(
            value = nVal.coerceIn(minN, 500f),
            onValueChange = {viewModel.onNChanged(it.toInt().toString())},
            valueRange = minN..500f
        )
        val alphaVal = uiState.alphaInput.toFloatOrNull() ?: 0.05f
        Text("Significance Level (α): ${String.format(Locale.US, "%.2f", alphaVal)}")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("0.01", "0.05", "0.10").forEach { valAlpha ->
                FilterChip(
                    selected = uiState.alphaInput == valAlpha,
                    onClick = {viewModel.onAlphaChanged(valAlpha)},
                    label = {Text("α = $valAlpha")}
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        // visualizer bell curve & rejection region
        uiState.result?.let { res ->
            NormalCurveVisualizer(zCutoff = res.testStatistic)
        }
        Spacer(modifier = Modifier.height(16.dp))
        // result card
        uiState.result?.let { res ->
            val isRejected = res.isRejected
            val cardColor = if (isRejected) {
                MaterialTheme.colorScheme.errorContainer
            } else {
                MaterialTheme.colorScheme.primaryContainer
            }
            val textColor = if (isRejected) {
                MaterialTheme.colorScheme.onErrorContainer
            } else {
                MaterialTheme.colorScheme.onPrimaryContainer
            }
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = cardColor)
            ){
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Keputusan Uji Hipotesis",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Statistik hitung (${if (uiState.testType == HypothesisTestType.Z_TEST) "Z" else "t"}) = ${String.format(Locale.US, "%.4f", res.testStatistic)}",
                        color = textColor
                    )
                    res.degreesOfFreedom?.let { df ->
                        Text(text = "Degrees of freedom (df) = $df", color = textColor)
                    }
                    Text(
                        text = "p-value = ${String.format(Locale.US, "%.4f", res.pValue)}",
                        color = textColor
                    )
                    Text(
                        text = "Kesimpulan: ${res.rejectText}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                }
            }
        }
    }
}
