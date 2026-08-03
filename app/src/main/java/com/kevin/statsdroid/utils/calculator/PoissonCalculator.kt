package com.kevin.statsdroid.utils.calculator

import kotlin.math.exp

object PoissonCalculator{
    private fun factorial(k:Int):Double{
        var res=1.0
        for(i in 1..k)res*=i
        return res
    }
    fun pmf(lambda:Double,k:Int):Double{
        if(lambda<=0.0||k<0)return 0.0
        var term=exp(-lambda)
        for(i in 1..k)term*=lambda/i
        return term
    }
    fun cdfLower(lambda:Double,k:Int):Double{
        var sum=0.0
        for(i in 0..k)sum+=pmf(lambda,i)
        return sum
    }
    fun cdfUpper(lambda:Double,k:Int):Double{
        return 1.0-cdfLower(lambda,k-1)
    }
    fun mean(lambda:Double):Double=lambda
    fun variance(lambda:Double):Double=lambda
}