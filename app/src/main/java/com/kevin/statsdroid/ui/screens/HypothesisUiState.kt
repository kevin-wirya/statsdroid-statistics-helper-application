package com.kevin.statsdroid.ui.screens

import com.kevin.statsdroid.utils.calculator.HypothesisResult

data class HypothesisUiState(
    val sampleMeanInput: String="105.0",
    val popMeanInput:String="100.0",
    val popStdDevInput:String="15.0",
    val nInput:String="30",
    val alphaInput:String="0.05",
    val isTwoTailed:Boolean=true,
    val result:HypothesisResult?=null
)
