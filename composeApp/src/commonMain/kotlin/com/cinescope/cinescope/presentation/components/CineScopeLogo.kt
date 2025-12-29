package com.cinescope.cinescope.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CineScopeLogo(
    modifier: Modifier = Modifier,
    showText: Boolean = true,
    size: Float = 200f
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Canvas(
            modifier = Modifier.size(size.dp)
        ) {
            val canvasWidth = this.size.width
            val canvasHeight = this.size.height
            val scale = canvasWidth / 512f
            val filmGradient = Brush.linearGradient(
                colors = listOf(Color(0xFF6A5CFF), Color(0xFFC77DFF)),
                start = Offset(0f, 0f),
                end = Offset(canvasWidth, canvasHeight)
            )
            val lensGradient = Brush.linearGradient(
                colors = listOf(Color(0xFFFF7A18), Color(0xFFFFB347)),
                start = Offset(0f, 0f),
                end = Offset(canvasWidth, canvasHeight)
            )
            val reelCenterX = 240f * scale
            val reelCenterY = 220f * scale
            val reelRadius = 90f * scale

            drawCircle(
                brush = filmGradient,
                radius = reelRadius,
                center = Offset(reelCenterX, reelCenterY)
            )
            drawCircle(
                color = Color.White,
                radius = 12f * scale,
                center = Offset(reelCenterX, reelCenterY)
            )
            val holeRadius = 10f * scale

            drawCircle(
                color = Color.White,
                radius = holeRadius,
                center = Offset(reelCenterX, reelCenterY - 40f * scale)
            )
            drawCircle(
                color = Color.White,
                radius = holeRadius,
                center = Offset(reelCenterX, reelCenterY + 40f * scale)
            )
            drawCircle(
                color = Color.White,
                radius = holeRadius,
                center = Offset(reelCenterX - 40f * scale, reelCenterY)
            )
            drawCircle(
                color = Color.White,
                radius = holeRadius,
                center = Offset(reelCenterX + 40f * scale, reelCenterY)
            )

            val stripX = 150f * scale
            val stripY = 280f * scale
            val stripWidth = 180f * scale
            val stripHeight = 40f * scale

            drawRoundRect(
                color = Color(0xFF4CC9F0),
                topLeft = Offset(stripX, stripY),
                size = androidx.compose.ui.geometry.Size(stripWidth, stripHeight),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(20f * scale, 20f * scale)
            )

            val perfY = 292f * scale
            val perfHeight = 16f * scale
            val perfWidth = 20f * scale
            val perfSpacing = 40f * scale

            for (i in 0..3) {
                drawRoundRect(
                    color = Color(0xFF1B132A),
                    topLeft = Offset(stripX + 15f * scale + i * perfSpacing, perfY),
                    size = androidx.compose.ui.geometry.Size(perfWidth, perfHeight),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f * scale, 4f * scale)
                )
            }

            val glassCenterX = 310f * scale
            val glassCenterY = 250f * scale
            val glassRadius = 38f * scale

            drawCircle(
                color = Color(0xFF2A1E3F),
                radius = glassRadius,
                center = Offset(glassCenterX, glassCenterY)
            )
            drawCircle(
                brush = lensGradient,
                radius = glassRadius,
                center = Offset(glassCenterX, glassCenterY),
                style = Stroke(width = 8f * scale)
            )

            val handleLength = 52f * scale
            val handleWidth = 14f * scale
            val handleStartX = glassCenterX + glassRadius * 0.7f
            val handleStartY = glassCenterY + glassRadius * 0.7f

            rotate(45f, pivot = Offset(handleStartX, handleStartY)) {
                drawRoundRect(
                    brush = lensGradient,
                    topLeft = Offset(handleStartX, handleStartY),
                    size = androidx.compose.ui.geometry.Size(handleWidth, handleLength),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(7f * scale, 7f * scale)
                )
            }
        }

        if (showText) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "CineScope",
                fontSize = (size / 4).sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFFF9F43)
            )
        }
    }
}
