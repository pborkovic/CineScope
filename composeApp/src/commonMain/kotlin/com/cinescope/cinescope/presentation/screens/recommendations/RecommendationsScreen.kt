package com.cinescope.cinescope.presentation.screens.recommendations

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.cinescope.cinescope.presentation.model.RecommendationUi
import com.cinescope.cinescope.presentation.components.*
import com.cinescope.cinescope.presentation.theme.Gradients
import com.cinescope.cinescope.presentation.theme.Spacing
import com.cinescope.cinescope.presentation.theme.CineScopeTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RecommendationsScreen(
    navigateToDetails: (Int) -> Unit,
    viewModel: RecommendationsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CineScopeTheme.colors.cardBackground)
    ) {
        Spacer(modifier = Modifier.height(Spacing.xl))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Discover",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = CineScopeTheme.colors.textPrimary
            )

            IconButton(onClick = viewModel::refresh) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(Spacing.lg))

        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingIndicator(message = "Generating recommendations...")
                }
            }

            uiState.errorMessage != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(Spacing.xl)
                    ) {
                        Text(
                            text = "⚠️",
                            fontSize = 48.sp
                        )
                        Text(
                            text = uiState.errorMessage ?: "An error occurred",
                            fontSize = 17.sp,
                            color = MaterialTheme.colorScheme.error
                        )
                        FilledButton(
                            text = "Try Again",
                            onClick = viewModel::refresh
                        )
                    }
                }
            }

            uiState.recommendations.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(Spacing.xl)
                    ) {
                        CineScopeIcon(
                            size = 120.dp,
                            showBackground = false
                        )
                        Spacer(modifier = Modifier.height(Spacing.md))
                        Text(
                            text = "Start rating movies",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = CineScopeTheme.colors.textPrimary
                        )
                        Spacer(modifier = Modifier.height(Spacing.sm))
                        Text(
                            text = "Rate at least 3 movies to get personalized recommendations",
                            fontSize = 16.sp,
                            color = CineScopeTheme.colors.textSecondary
                        )
                    }
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = Spacing.md,
                        end = Spacing.md,
                        bottom = 100.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.recommendations) { recommendation ->
                        RecommendationCard(
                            recommendation = recommendation,
                            onClick = { navigateToDetails(recommendation.movie.tmdbId) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RecommendationCard(
    recommendation: RecommendationUi,
    onClick: () -> Unit
) {
    val movie = recommendation.movie

    CineScopeCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            AsyncImage(
                model = movie.posterUrl,
                contentDescription = null,
                modifier = Modifier
                    .width(90.dp)
                    .height(135.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = movie.title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CineScopeTheme.colors.textPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            when (recommendation.matchQuality) {
                                com.cinescope.cinescope.presentation.model.MatchQuality.HIGH -> Gradients.Sunset
                                com.cinescope.cinescope.presentation.model.MatchQuality.MEDIUM -> Gradients.Ocean
                                com.cinescope.cinescope.presentation.model.MatchQuality.LOW -> Gradients.Lavender
                            }
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = recommendation.matchDisplay,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = recommendation.reason,
                    fontSize = 14.sp,
                    color = CineScopeTheme.colors.textSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (movie.ratingDisplay.isNotBlank()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = movie.ratingDisplay.substringBefore("/10"),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(start = 4.dp),
                                color = CineScopeTheme.colors.textPrimary
                            )
                        }
                    }

                    if (movie.releaseYear.isNotBlank()) {
                        Text(
                            text = "•",
                            fontSize = 14.sp,
                            color = CineScopeTheme.colors.textSecondary
                        )
                        Text(
                            text = movie.releaseYear,
                            fontSize = 14.sp,
                            color = CineScopeTheme.colors.textSecondary
                        )
                    }
                }
            }
        }
    }
}
