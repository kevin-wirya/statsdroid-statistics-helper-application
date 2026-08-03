package com.kevin.statsdroid.ui.screens

import androidx.lifecycle.ViewModel
import com.kevin.statsdroid.domain.repository.StatsRepository
import com.kevin.statsdroid.utils.calculator.CltCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CltViewModel @Inject constructor(
    private val repository: StatsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CltUiState())
    val uiState: StateFlow<CltUiState> = _uiState.asStateFlow()

    init {
        generateSimulation()
    }

    fun onDistributionChanged(distribution: CltCalculator.DistributionType) {
        _uiState.update { it.copy(selectedDistribution = distribution) }
        generateSimulation()
    }

    fun onSampleSizeChanged(input: String) {
        _uiState.update { it.copy(sampleSizeInput = input) }
        generateSimulation()
    }

    fun onNumSamplesChanged(input: String) {
        _uiState.update { it.copy(numSamplesInput = input) }
        generateSimulation()
    }

    fun generateSimulation() {
        val state = _uiState.value
        val sampleSize = state.sampleSizeInput.toIntOrNull() ?: 30
        val numSamples = state.numSamplesInput.toIntOrNull() ?: 1000

        val cltStats = CltCalculator.calculateCltSimulation(
            state.selectedDistribution, sampleSize, numSamples
        )

        _uiState.update {
            it.copy(
                populationData = cltStats.populationData,
                sampleMeans = cltStats.sampleMeans,
                theoreticalMean = cltStats.theoreticalMean,
                theoreticalStdDev = cltStats.theoreticalStdDev,
                empiricalMean = cltStats.empiricalMean,
                standardError = cltStats.standardError
            )
        }
    }
}
