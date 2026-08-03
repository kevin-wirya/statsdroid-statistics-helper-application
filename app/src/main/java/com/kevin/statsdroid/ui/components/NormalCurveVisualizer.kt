package com.kevin.statsdroid.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.exp

@Composable
fun NormalCurveVisualizer(
    zCutoff:Double?=null,
    isTwoTailed:Boolean=false,
    modifier:Modifier=Modifier,
    curveColor:Color=MaterialTheme.colorScheme.primary,
    shadeColor:Color=MaterialTheme.colorScheme.primary.copy(alpha=0.35f)
){
    Box(
        modifier=modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha=0.3f))
            .padding(16.dp)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val width=size.width
            val height=size.height
            val minZ=-3.5
            val maxZ=3.5
            fun zToX(z:Double):Float{
                return ((z-minZ)/(maxZ-minZ)*width).toFloat()
            }
            fun normalPdf(z:Double):Double{
                return exp(-0.5*z*z)
            }
            val maxPdf=normalPdf(0.0)
            fun pdfToY(pdf:Double):Float{
                val usableHeight=height-40f
                return (height-20f-(pdf/maxPdf*usableHeight)).toFloat()
            }
            // Shaded region
            if(zCutoff!=null){
                val shadePath=Path()
                var firstPoint=true
                for(i in 0..200){
                    val z=minZ+(i/200.0)*(maxZ-minZ)
                    val isShaded=if(isTwoTailed){
                        kotlin.math.abs(z)>=kotlin.math.abs(zCutoff)
                    } else {
                        z<=zCutoff
                    }
                    if(isShaded){
                        val x=zToX(z)
                        val y=pdfToY(normalPdf(z))
                        if(firstPoint){
                            shadePath.moveTo(x,height-20f)
                            shadePath.lineTo(x,y)
                            firstPoint=false
                        }else{
                            shadePath.lineTo(x,y)
                        }
                    }else if(!firstPoint){
                        val x=zToX(z)
                        shadePath.lineTo(x,height-20f)
                        shadePath.close()
                        firstPoint=true
                    }
                }
                drawPath(path=shadePath,color=shadeColor)
            }
            // Bell curve
            val curvePath=Path()
            for(i in 0..200){
                val z=minZ+(i/200.0)*(maxZ-minZ)
                val x=zToX(z)
                val y=pdfToY(normalPdf(z))
                if(i==0)curvePath.moveTo(x,y)else curvePath.lineTo(x,y)
            }
            drawPath(
                path=curvePath,
                color=curveColor,
                style=Stroke(width=4f)
            )
            // X axis line
            drawLine(
                color=Color.Gray,
                start=Offset(0f, height-20f),
                end=Offset(width,height-20f),
                strokeWidth=2f
            )
            // Z cutoff line
            if(zCutoff!=null){
                val cutoffX=zToX(zCutoff)
                drawLine(
                    color=Color.Red,
                    start=Offset(cutoffX,0f),
                    end=Offset(cutoffX,height-20f),
                    strokeWidth=3f
                )
                if(isTwoTailed){
                    val negCutoffX=zToX(-kotlin.math.abs(zCutoff))
                    drawLine(
                        color=Color.Red,
                        start=Offset(negCutoffX,0f),
                        end=Offset(negCutoffX,height-20f),
                        strokeWidth=3f
                    )
                }
            }
        }
    }
}
