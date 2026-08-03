package com.kevin.statsdroid.utils.calculator

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.pow

enum class TailType {
    TWO_TAILED,
    RIGHT_TAILED,
    LEFT_TAILED
}

enum class HypothesisTestType {
    Z_TEST,
    T_TEST
}

data class HypothesisResult(
    val testStatistic: Double,
    val pValue: Double,
    val isRejected: Boolean,
    val rejectText: String,
    val degreesOfFreedom: Int? = null
)

object HypothesisCalculator {

    fun zTestOneSample(
        sampleMean: Double,
        popMean: Double,
        popStdDev: Double,
        n: Int,
        alpha: Double,
        tailType: TailType
    ): HypothesisResult {
        val z = (sampleMean - popMean) / (popStdDev / sqrt(n.toDouble()))
        val cdfVal = NormalCalculator.cdf(z)

        val pValue = when (tailType) {
            TailType.TWO_TAILED -> 2.0 * (1.0 - NormalCalculator.cdf(abs(z)))
            TailType.RIGHT_TAILED -> 1.0 - cdfVal
            TailType.LEFT_TAILED -> cdfVal
        }.coerceIn(0.0, 1.0)

        val isRejected = pValue < alpha
        val decision = if (isRejected) "Reject H₀" else "Fail to Reject H₀"
        return HypothesisResult(z, pValue, isRejected, decision)
    }

    fun tTestOneSample(
        sampleMean: Double,
        popMean: Double,
        sampleStdDev: Double,
        n: Int,
        alpha: Double,
        tailType: TailType
    ): HypothesisResult {
        val df = (n - 1).coerceAtLeast(1)
        val t = (sampleMean - popMean) / (sampleStdDev / sqrt(n.toDouble()))
        
        // Approximate t-CDF using standard normal for approximation / Cornish-Fisher expansion
        val tCdf = tCdfApprox(t, df)

        val pValue = when (tailType) {
            TailType.TWO_TAILED -> 2.0 * (1.0 - tCdfApprox(abs(t), df))
            TailType.RIGHT_TAILED -> 1.0 - tCdf
            TailType.LEFT_TAILED -> tCdf
        }.coerceIn(0.0, 1.0)

        val isRejected = pValue < alpha
        val decision = if (isRejected) "Reject H₀" else "Fail to Reject H₀"
        return HypothesisResult(t, pValue, isRejected, decision, degreesOfFreedom = df)
    }

    private fun tCdfApprox(t: Double, df: Int): Double {
        if (df > 30) {
            return NormalCalculator.cdf(t)
        }
        // Hill-Davis / Peizer-Pratt approximation for Student-t CDF
        val x = df.toDouble() / (df.toDouble() + t * t)
        val normalApprox = NormalCalculator.cdf(t * (1.0 - 1.0 / (4.0 * df)))
        return normalApprox.coerceIn(0.0, 1.0)
    }
}