package com.kevin.statsdroid.ui.screens

import com.kevin.statsdroid.utils.calculator.HypothesisResult
import com.kevin.statsdroid.utils.calculator.HypothesisTestType
import com.kevin.statsdroid.utils.calculator.TailType

data class HypothesisUiState(
    val testType: HypothesisTestType = HypothesisTestType.Z_TEST,
    val sampleMeanInput: String = "105.0",
    val popMeanInput: String = "100.0",
    val popStdDevInput: String = "15.0",
    val sampleStdDevInput: String = "15.0",
    val nInput: String = "30",
    val alphaInput: String = "0.05",
    val tailType: TailType = TailType.TWO_TAILED,
    val result: HypothesisResult? = null
)
