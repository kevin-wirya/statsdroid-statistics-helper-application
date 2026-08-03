package com.kevin.statsdroid.utils.calculator

import kotlin.random.Random

object CltCalculator{
    enum class DistributionType{
        UNIFORM, EXPONENTIAL
    }

    fun generateSampleMeans(
        distribution:DistributionType,
        sampleSize:Int,
        numSamples:Int
    ):List<Double>{
        val means=mutableListOf<Double>()
        for(i in 0 until numSamples){
            var sum=0.0
            for(j in 0 until sampleSize){
                sum+=when(distribution){
                    DistributionType.UNIFORM-> Random.nextDouble(0.0,10.0)
                    DistributionType.EXPONENTIAL-> -kotlin.math.ln(1.0-Random.nextDouble(0.0001,0.9999))
                }
            }
            means.add(sum/sampleSize)
        }
        return means
    }
}