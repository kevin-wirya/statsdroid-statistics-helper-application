package com.kevin.statsdroid.ui.screens

import com.kevin.statsdroid.utils.calculator.CltCalculator

data class CltUiState(
    val sampleSizeInput: String = "30",
    val numSamplesInput: String = "1000",
    val selectedDistribution: CltCalculator.DistributionType = CltCalculator.DistributionType.UNIFORM,
    val populationData: List<Double> = emptyList(),
    val sampleMeans: List<Double> = emptyList(),
    val theoreticalMean: Double = 5.0,
    val theoreticalStdDev: Double = 2.8867,
    val empiricalMean: Double = 5.0,
    val standardError: Double = 0.527
)
