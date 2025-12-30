package com.cinescope.cinescope.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.cinescope.cinescope.domain.model.Movie
import com.cinescope.cinescope.presentation.model.MovieUi
import com.cinescope.cinescope.presentation.theme.Gradients
import com.cinescope.cinescope.presentation.theme.Shadows.subtle
import com.cinescope.cinescope.presentation.theme.CineScopeTheme

/**
 * Apple-style movie card with gradient background and poster overlay.
 *
 * Design characteristics:
 * - Vibrant gradient background (varies by movie ID)
 * - Poster image with subtle dark gradient overlay
 * - 20dp corner radius
 * - 4dp shadow elevation
 * - Circular white rating badge (if rated)
 * - Clean white card container
 *
 * This component now uses MovieUi presentation model for optimized UI rendering.
 */
@Composable
fun MovieCard(
    movie: MovieUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gradient = Gradients.getGradient(movie.tmdbId % 6)

    CineScopeCard(
        modifier = modifier.width(150.dp),
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        elevation = 4.dp
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(225.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(gradient)
                )

                AsyncImage(
                    model = movie.posterUrl,
                    contentDescription = movie.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.3f)
                                )
                            )
                        )
                )

                if (movie.isHighlyRated) {
                    val ratingValue = movie.ratingDisplay.substringBefore("/").toDoubleOrNull()
                    ratingValue?.let { rating ->
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(8.dp)
                                .size(36.dp)
                                .subtle(CircleShape)
                                .clip(CircleShape)
                                .background(Color.White)
                                .padding(6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = CineScopeTheme.glassColors.rating,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = rating.toString(),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = CineScopeTheme.colors.textPrimary
                                )
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = CineScopeTheme.colors.textPrimary
                )
            }
        }
    }
}

/**
 * Legacy MovieCard that accepts domain Movie model.
 * Kept for backward compatibility with screens not yet migrated to MovieUi.
 * @deprecated Use MovieCard(movieUi: MovieUi) instead
 */
@Deprecated("Use MovieCard with MovieUi instead", ReplaceWith("MovieCard(movieUi, onClick, modifier)"))
@Composable
fun MovieCard(
    movie: Movie,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Assign gradient based on movie ID for consistency
    val gradient = Gradients.getGradient(movie.tmdbId % 6)

    CineScopeCard(
        modifier = modifier.width(150.dp),
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        elevation = 4.dp
    ) {
        Column {
            // Poster with gradient background
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(225.dp)
            ) {
                // Gradient background layer
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(gradient)
                )

                // Poster image
                AsyncImage(
                    model = movie.getPosterUrl(),
                    contentDescription = movie.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Dark gradient overlay for depth
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.3f)
                                )
                            )
                        )
                )

                // Rating badge (bottom-right corner)
                movie.voteAverage?.let { rating ->
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                            .size(36.dp)
                            .subtle(CircleShape)
                            .clip(CircleShape)
                            .background(Color.White)
                            .padding(6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = CineScopeTheme.glassColors.rating,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "${(rating * 10).toInt() / 10.0}",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Medium,
                                color = CineScopeTheme.colors.textPrimary
                            )
                        }
                    }
                }
            }

            // Movie title
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = CineScopeTheme.colors.textPrimary
                )
            }
        }
    }
}
