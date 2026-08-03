package com.kevin.statsdroid.utils.calculator

import kotlin.math.sqrt
import kotlin.math.ln
import kotlin.random.Random

object CltCalculator {
    enum class DistributionType {
        UNIFORM,
        EXPONENTIAL,
        BIMODAL
    }

    data class CltStats(
        val populationData: List<Double>,
        val sampleMeans: List<Double>,
        val theoreticalMean: Double,
        val theoreticalStdDev: Double,
        val empiricalMean: Double,
        val standardError: Double
    )

    fun calculateCltSimulation(
        distribution: DistributionType,
        sampleSize: Int,
        numSamples: Int
    ): CltStats {
        val popData = generatePopulationData(distribution, 1000)

        val means = mutableListOf<Double>()
        for (i in 0 until numSamples) {
            var sum = 0.0
            for (j in 0 until sampleSize) {
                sum += drawSingleSample(distribution)
            }
            means.add(sum / sampleSize)
        }

        val (mu, sigma) = when (distribution) {
            DistributionType.UNIFORM -> Pair(5.0, 10.0 / sqrt(12.0))
            DistributionType.EXPONENTIAL -> Pair(1.0, 1.0)
            DistributionType.BIMODAL -> Pair(5.0, 3.041)
        }

        val empMean = if (means.isNotEmpty()) means.average() else mu
        val se = sigma / sqrt(sampleSize.toDouble())

        return CltStats(
            populationData = popData,
            sampleMeans = means,
            theoreticalMean = mu,
            theoreticalStdDev = sigma,
            empiricalMean = empMean,
            standardError = se
        )
    }

    fun generatePopulationData(distribution: DistributionType, count: Int): List<Double> {
        return List(count) { drawSingleSample(distribution) }
    }

    private fun drawSingleSample(distribution: DistributionType): Double {
        return when (distribution) {
            DistributionType.UNIFORM -> Random.nextDouble(0.0, 10.0)
            DistributionType.EXPONENTIAL -> -ln(1.0 - Random.nextDouble(0.0001, 0.9999))
            DistributionType.BIMODAL -> {
                if (Random.nextBoolean()) {
                    Random.nextDouble(1.0, 3.5)
                } else {
                    Random.nextDouble(6.5, 9.0)
                }
            }
        }
    }

    fun generateSampleMeans(
        distribution: DistributionType,
        sampleSize: Int,
        numSamples: Int
    ): List<Double> {
        return calculateCltSimulation(distribution, sampleSize, numSamples).sampleMeans
    }
}