package com.kevin.statsdroid.ui.screens

import com.kevin.statsdroid.utils.calculator.CltCalculator

data class CltUiState(
    val sampleSizeInput:String="30",
    val numSamplesInput:String="500",
    val selectedDistribution:CltCalculator.DistributionType=CltCalculator.DistributionType.UNIFORM,
    val sampleMeans:List<Double>=emptyList()
)
