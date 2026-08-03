package com.kevin.statsdroid.ui.screens

enum class DistributionType{
    BINOMIAL,
    POISSON,
    NORMAL
}

data class LookupUiState(
    val selectedDistribution:DistributionType=DistributionType.BINOMIAL,
    // Parameter Binomial
    val nInput:String="10",
    val pInput:String="0.5",
    val kBinomialInput:String="5",
    // Parameter Poisson
    val lambdaInput:String="3.0",
    val kPoissonInput:String="2",
    // Parameter Normal
    val zInput:String="1.96",
    // Hasil Perhitungan
    val resultPmf:Double=0.0,
    val resultCdfLower:Double=0.0,
    val resultCdfUpper:Double=0.0,
    val errorMessage:String?=null
)
