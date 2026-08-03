package com.kevin.statsdroid.utils.calculator

import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.sqrt

object NormalCalculator{
    private fun erf(x:Double):Double{
        val a1=0.254829592
        val a2=-0.284496736
        val a3=1.421413741
        val a4=-1.453152027
        val a5=1.061405429
        val p=0.3275911
        val sign=if(x<0)-1 else 1
        val absX=abs(x)
        val t=1.0/(1.0+p*absX)
        val y=1.0-((((a5*t+a4)*t+a3)*t+a2)*t+a1)*t*exp(-absX*absX)
        return sign*y
    }
    // P(Z<=z)
    fun cdf(z:Double):Double{
        return 0.5*(1.0+erf(z/sqrt(2.0)))
    }
    // P(Z>=z)
    fun cdfUpper(z:Double):Double{
        return 1.0-cdf(z)
    }
    //P(z1<=Z<=z2)
    fun cdfBetween(z1:Double,z2:Double):Double{
        return cdf(z2)-cdf(z1)
    }
}