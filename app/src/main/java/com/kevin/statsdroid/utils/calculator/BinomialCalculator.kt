package com.kevin.statsdroid.utils.calculator

import kotlin.math.pow

object BinomialCalculator{
    fun combination(n:Int,k:Int):Double{
        if(k<0||k>n)return 0.0;
        if(k==0||k==n)return 1.0;
        var res=1.0
        val minK=if(k<n-k)k else n-k
        for(i in 1..minK){
            res=res*(n-i+1)/i
        }
        return res
    }
    fun pmf(n:Int,p:Double,k:Int):Double{
        if(p<0.0.||p>1.0||k<0||k>n)return 0.0
        return combination(n,k)*p.pow(k)*(1-p).pow(n-k)
    }
    fun cdfLower(n:Int,p:Double,k:Int):Double{
        var sum=0.0
        for(i in 0..k){
            sum+=pmf(n,p,i)
        }
        return sum
    }
    fun cdfUpper(n:Int,p:Double,k:Int):Double{
        var sum=0.0
        for(i in k..n){
            sum+=pmf(n,p,i)
        }
        return sum
    }
    fun mean(n:Int,p:Double):Double=n*p
    fun variance(n:Int,p:Double):Double=n*p*(1-p)
}