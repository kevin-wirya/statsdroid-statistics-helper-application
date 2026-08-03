package com.kevin.statsdroid.data.repository

import com.kevin.statsdroid.domain.repository.StatsRepository
import com.kevin.statsdroid.utils.calculator.BinomialCalculator
import com.kevin.statsdroid.utils.calculator.CltCalculator
import com.kevin.statsdroid.utils.calculator.HypothesisCalculator
import com.kevin.statsdroid.utils.calculator.HypothesisResult
import com.kevin.statsdroid.utils.calculator.NormalCalculator
import com.kevin.statsdroid.utils.calculator.PoissonCalculator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StatsRepositoryImpl @Inject constructor() : StatsRepository {
    override fun calculateBinomialPmf(n: Int,p:Double, k:Int): Double =
        BinomialCalculator.pmf(n, p, k)
    override fun calculateBinomialCdfLower(n: Int,p:Double,k:Int): Double =
        BinomialCalculator.cdfLower(n, p, k)
    override fun calculateBinomialCdfUpper(n: Int,p:Double,k:Int): Double =
        BinomialCalculator.cdfUpper(n, p, k)
    override fun calculatePoissonPmf(lambda:Double,k:Int): Double =
        PoissonCalculator.pmf(lambda, k)
    override fun calculatePoissonCdfLower(lambda:Double,k:Int): Double =
        PoissonCalculator.cdfLower(lambda, k)
    override fun calculatePoissonCdfUpper(lambda:Double,k:Int): Double =
        PoissonCalculator.cdfUpper(lambda, k)
    override fun calculateNormalCdf(z:Double): Double =
        NormalCalculator.cdf(z)
    override fun performZTestOneSample(
        sampleMean:Double,
        popMean:Double,
        popStdDev:Double,
        n:Int,
        alpha:Double,
        isTwoTailed:Boolean
    ):HypothesisResult = HypothesisCalculator.zTestOneSample(
        sampleMean,popMean,popStdDev,n,alpha,isTwoTailed
    )
    override fun generateCltSampleMeans(
        distribution:CltCalculator.DistributionType,
        sampleSize:Int,
        numSamples:Int
    ): List<Double> = CltCalculator.generateSampleMeans(distribution,sampleSize,numSamples)
}
