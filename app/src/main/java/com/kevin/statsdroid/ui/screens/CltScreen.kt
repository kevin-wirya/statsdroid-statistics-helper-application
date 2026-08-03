package com.kevin.statsdroid.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.kevin.statsdroid.utils.calculator.CltCalculator
import com.kevin.statsdroid.ui.components.CltHistogramVisualizer

@Composable
fun CltScreen(
    viewModel: CltViewModel = hiltViewModel(),
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
            text="Simulasi Central Limit Theorem",
            style=MaterialTheme.typography.headlineMedium,
            fontWeight=FontWeight.Bold
        )
        Text(
            text="Buktikan bahwa distribusi rata-rata sampel mendekati normal seiring bertambahnya n.",
            style=MaterialTheme.typography.bodyMedium,
            color=MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier=Modifier.height(16.dp))
        // pilihan bentuk populasi
        val dists=listOf("Uniform", "Exponential")
        val selectedIndex=uiState.selectedDistribution.ordinal
        PrimaryTabRow(selectedTabIndex=selectedIndex) {
            dists.forEachIndexed { idx, title ->
                Tab(
                    selected = selectedIndex == idx,
                    onClick = {
                        val newDist = CltCalculator.DistributionType.entries[idx]
                        viewModel.onDistributionChanged(newDist)
                    },
                    text={Text(title)}
                )
            }
        }
        Spacer(modifier=Modifier.height(16.dp))
        // slider ukuran sampel (n)
        val nVal=uiState.sampleSizeInput.toFloatOrNull()?:30f
        Text("Ukuran sampel (n): ${nVal.toInt()}")
        Slider(
            value=nVal.coerceIn(2f,100f),
            onValueChange={viewModel.onSampleSizeChanged(it.toInt().toString())},
            valueRange=2f..100f
        )
        // slider jumlah iterasi sampel (M)
        val mVal=uiState.numSamplesInput.toFloatOrNull()?:500f
        Text("Jumlah iterasi sampel (M): ${mVal.toInt()}")
        Slider(
            value=mVal.coerceIn(100f,2000f),
            onValueChange={viewModel.onNumSamplesChanged(it.toInt().toString())},
            valueRange=100f..2000f
        )
        Spacer(modifier=Modifier.height(16.dp))
        Text(
            text="Histogram Distribusi Rata-rata Sampel",
            style=MaterialTheme.typography.titleMedium,
            fontWeight=FontWeight.Bold
        )
        Spacer(modifier=Modifier.height(8.dp))
        CltHistogramVisualizer(sampleMeans=uiState.sampleMeans)
    }
}
