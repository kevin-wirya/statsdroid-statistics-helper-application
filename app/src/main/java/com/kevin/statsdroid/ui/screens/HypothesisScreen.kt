package com.kevin.statsdroid.ui.screens

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kevin.statsdroid.ui.components.NormalCurveVisualizer
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
    ){
        Text(
            text = "Pengujian Hipotesis (Z-Test)",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Uji Signifikansi Rata-rata Sampel vs Rata-rata Populasi.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        // switch two-tailed vs one-tailed
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ){
            Text(
                text = if (uiState.isTwoTailed) "Uji 2 Arah (Two-Tailed)" else "Uji 1 Arah (One-Tailed)",
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.SemiBold
            )
            Switch(
                checked = uiState.isTwoTailed,
                onCheckedChange = { viewModel.onTwoTailedChanged(it) }
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        // mean sampel
        val sampleMeanVal = uiState.sampleMeanInput.toFloatOrNull() ?: 105f
        Text("Rata-rata Sampel (x̄): ${String.format(Locale.US, "%.1f", sampleMeanVal)}")
        Slider(
            value = sampleMeanVal.coerceIn(50f, 150f),
            onValueChange = { viewModel.onSampleMeanChanged(String.format(Locale.US, "%.1f", it)) },
            valueRange = 50f..150f
        )
        // mean populasi
        val popMeanVal = uiState.popMeanInput.toFloatOrNull() ?: 100f
        Text("Rata-rata Populasi (μ₀): ${String.format(Locale.US, "%.1f", popMeanVal)}")
        Slider(
            value = popMeanVal.coerceIn(50f, 150f),
            onValueChange = { viewModel.onPopMeanChanged(String.format(Locale.US, "%.1f", it)) },
            valueRange = 50f..150f
        )
        // sdev populasi
        val popStdDevVal = uiState.popStdDevInput.toFloatOrNull() ?: 15f
        Text("Standar Deviasi (σ): ${String.format(Locale.US, "%.1f", popStdDevVal)}")
        Slider(
            value = popStdDevVal.coerceIn(1f, 50f),
            onValueChange = { viewModel.onPopStdDevChanged(String.format(Locale.US, "%.1f", it)) },
            valueRange = 1f..50f
        )
        // ukuran sampel
        val nVal = uiState.nInput.toFloatOrNull() ?: 30f
        Text("Ukuran Sampel (n): ${nVal.toInt()}")
        Slider(
            value = nVal.coerceIn(5f, 200f),
            onValueChange = { viewModel.onNChanged(it.toInt().toString()) },
            valueRange = 5f..200f
        )
        // signifikansi / alpha
        val alphaVal = uiState.alphaInput.toFloatOrNull() ?: 0.05f
        Text("Level Signifikansi (α): ${String.format(Locale.US, "%.2f", alphaVal)}")
        Slider(
            value = alphaVal.coerceIn(0.01f, 0.10f),
            onValueChange = { viewModel.onAlphaChanged(String.format(Locale.US, "%.2f", it)) },
            valueRange = 0.01f..0.10f
        )
        Spacer(modifier=Modifier.height(16.dp))
        // visualizer kurva tolak h0
        val zCutoff = uiState.result?.testStatistic ?: 1.96
        NormalCurveVisualizer(
            zCutoff = zCutoff,
            isTwoTailed = uiState.isTwoTailed,
            shadeColor = Color.Red.copy(alpha = 0.4f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        // card hasil
        uiState.result?.let { res ->
            val cardBg = if (res.isRejected) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.primaryContainer
            val textColor = if (res.isRejected) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onPrimaryContainer
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = cardBg)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Keputusan: ${res.rejectText}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Nilai Z hitung: ${String.format(Locale.US, "%.4f", res.testStatistic)}", color = textColor)
                    Text("P-value: ${String.format(Locale.US, "%.4f", res.pValue)}", color = textColor)
                }
            }
        }
    }
}
