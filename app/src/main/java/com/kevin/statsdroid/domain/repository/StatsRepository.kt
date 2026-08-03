package com.kevin.statsdroid.domain.repository

import com.kevin.statsdroid.utils.calculator.CltCalculator
import com.kevin.statsdroid.utils.calculator.HypothesisResult

interface StatsRepository{
    fun calculateBinomialPmf(n:Int,p:Double,k:Int):Double
    fun calculateBinomialCdfLower(n:Int,p:Double,k:Int):Double
    fun calculateBinomialCdfUpper(n:Int,p:Double,k:Int):Double
    
    fun calculatePoissonPmf(lambda:Double,k:Int):Double
    fun calculatePoissonCdfLower(lambda:Double,k:Int):Double
    fun calculatePoissonCdfUpper(lambda:Double,k:Int):Double
    
    fun calculateNormalCdf(z:Double):Double
   
    fun performZTestOneSample(
        sampleMean:Double,
        popMean:Double,
        popStdDev:Double,
        n:Int,
        alpha:Double,
        isTwoTailed:Boolean
    ):HypothesisResult

    fun generateCltSampleMeans(
        distribution:CltCalculator.DistributionType,
        sampleSize:Int,
        numSamples:Int
    ):List<Double>
}