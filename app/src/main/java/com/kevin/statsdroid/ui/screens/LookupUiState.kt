package com.kevin.statsdroid.ui.screens

enum class DistributionType{
    BINOMIAL,
    POISSON,
    NORMAL
}

data class LookupUiState(
    val selectedDistribution:DistributionType=DistributionType.BINOMIAL,
    // parameter binomial
    val nInput:String="10",
    val pInput:String="0.5",
    val kBinomialInput:String="5",
    // parameter poisson
    val lambdaInput:String="3.0",
    val kPoissonInput:String="2",
    // parameter normal
    val zInput:String="1.96",
    // hasil
    val resultPmf:Double=0.0,
    val resultCdfLower:Double=0.0,
    val resultCdfUpper:Double=0.0,
    val errorMessage:String?=null
)
