package com.kevin.statsdroid.utils.calculator

import kotlin.math.abs
import kotlin.math.sqrt

data class HypothesisResult(
    val testStatistic:Double,
    val pValue:Double,
    val isRejected:Boolean,
    val rejectText:String
)

object HypothesisCalculator{
    fun zTestOneSample(
        sampleMean:Double,
        popMean:Double,
        popStdDev:Double,
        n:Int,
        alpha:Double,
        isTwoTailed:Boolean
    ):HypothesisResult{
        val z=(sampleMean-popMean)/(popStdDev/sqrt(n.toDouble()))
        val pValue=if(isTwoTailed){
            2.0*(1.0-NormalCalculator.cdf(abs(z)))
        }else{
            1.0-NormalCalculator.cdf(z)
        }
        val isRejected=pValue<alpha
        val decision=if(isRejected) "Tolak H0" else "Gagal Tolak H0"
        return HypothesisResult(z,pValue,isRejected,decision)
    }   
}