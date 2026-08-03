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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CltHistogramVisualizer(
    sampleMeans: List<Double>,
    modifier: Modifier = Modifier,
    barColor: Color = MaterialTheme.colorScheme.primary
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            .padding(16.dp)
    ){
        Canvas(modifier = Modifier.matchParentSize()) {
            if (sampleMeans.isEmpty()) return@Canvas
            val width = size.width
            val height = size.height
            val numBins = 25
            val minVal = sampleMeans.minOrNull() ?: 0.0
            val maxVal = sampleMeans.maxOrNull() ?: 10.0
            val range = if (maxVal-minVal == 0.0) 1.0 else maxVal-minVal
            val binWidthVal = range / numBins
            val bins = IntArray(numBins)
            for (mean in sampleMeans) {
                var idx=((mean-minVal)/binWidthVal).toInt()
                if (idx>= numBins) idx= numBins - 1
                if (idx<0)idx=0
                bins[idx]++
            }
            val maxFreq = (bins.maxOrNull()?: 1).toFloat()
            val barWidthPx = width / numBins
            for (i in 0 until numBins) {
                val freq=bins[i].toFloat()
                val barHeightPx = (freq/maxFreq)*(height-30f)
                val x=i*barWidthPx
                val y=height-20f-barHeightPx
                drawRect(
                    color = barColor.copy(alpha = 0.75f),
                    topLeft = Offset(x+1f,y),
                    size = Size(barWidthPx-2f,barHeightPx)
                )
            }
            drawLine(
                color = Color.Gray,
                start = Offset(0f,height-20f),
                end = Offset(width,height-20f),
                strokeWidth = 2f
            )
        }
    }
}