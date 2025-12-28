package com.cinescope.cinescope.presentation.screens.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tv
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cinescope.cinescope.presentation.components.CineScopeCard
import com.cinescope.cinescope.presentation.components.GradientCard
import com.cinescope.cinescope.presentation.components.LoadingIndicator
import com.cinescope.cinescope.presentation.components.StatsItem
import com.cinescope.cinescope.presentation.theme.Gradients
import com.cinescope.cinescope.presentation.theme.Spacing
import com.cinescope.cinescope.presentation.theme.CineScopeTheme
import org.koin.compose.viewmodel.koinViewModel

/**
 * Apple-inspired Statistics Screen with vibrant gradient cards.
 */
@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(CineScopeTheme.colors.cardBackground),
            contentAlignment = Alignment.Center
        ) {
            LoadingIndicator(message = "Loading statistics...")
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CineScopeTheme.colors.cardBackground)
                .verticalScroll(rememberScrollState())
        ) {
            // Top spacing
            Spacer(modifier = Modifier.height(Spacing.xl))

            // Large title
            Text(
                text = "Statistics",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = CineScopeTheme.colors.textPrimary,
                modifier = Modifier.padding(horizontal = Spacing.md)
            )

            Spacer(modifier = Modifier.height(Spacing.lg))
            // Overview Stats with gradient cards
            Column(
                modifier = Modifier.padding(horizontal = Spacing.md),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    GradientCard(
                        modifier = Modifier.weight(1f),
                        gradient = Gradients.Ocean
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Movie,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = uiState.totalMovies.toString(),
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Movies",
                                fontSize = 15.sp,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }

                    GradientCard(
                        modifier = Modifier.weight(1f),
                        gradient = Gradients.Berry
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Tv,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = uiState.totalTVSeries.toString(),
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "TV Series",
                                fontSize = 15.sp,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    GradientCard(
                        modifier = Modifier.weight(1f),
                        gradient = Gradients.Sunset
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "${(uiState.averageRating * 10).toInt() / 10.0}",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Avg Rating",
                                fontSize = 15.sp,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }

                    GradientCard(
                        modifier = Modifier.weight(1f),
                        gradient = Gradients.Lavender
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bookmark,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = uiState.watchlistCount.toString(),
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Watchlist",
                                fontSize = 15.sp,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(Spacing.xl))

            // Rating Distribution
            if (uiState.ratingDistribution.isNotEmpty()) {
                Text(
                    text = "Rating Distribution",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CineScopeTheme.colors.textPrimary,
                    modifier = Modifier.padding(horizontal = Spacing.md)
                )

                Spacer(modifier = Modifier.height(Spacing.md))

                CineScopeCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Spacing.md)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        (5 downTo 1).forEach { rating ->
                            val count = uiState.ratingDistribution[rating] ?: 0
                            val maxCount = uiState.ratingDistribution.values.maxOrNull() ?: 1
                            val percentage = if (maxCount > 0) count.toFloat() / maxCount else 0f

                            RatingBar(
                                rating = rating,
                                count = count,
                                percentage = percentage
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(Spacing.xl))
            }

            // Top Genres
            if (uiState.topGenres.isNotEmpty()) {
                Text(
                    text = "Top Genres",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CineScopeTheme.colors.textPrimary,
                    modifier = Modifier.padding(horizontal = Spacing.md)
                )

                Spacer(modifier = Modifier.height(Spacing.md))

                CineScopeCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Spacing.md)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        uiState.topGenres.forEach { (genre, count) ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = genre,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = CineScopeTheme.colors.textPrimary
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(MaterialTheme.colorScheme.primary)
                                        .padding(horizontal = 12.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = count.toString(),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(Spacing.xl))
            }

            // Insights with gradient card
            GradientCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.md),
                gradient = Gradients.Forest
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "💡 Insights",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    InsightItem(
                        text = "You've watched ${uiState.totalMovies + uiState.totalTVSeries} titles total!"
                    )

                    if (uiState.averageRating >= 4.0) {
                        Spacer(modifier = Modifier.height(8.dp))
                        InsightItem(
                            text = "You're a tough critic with an average rating of ${(uiState.averageRating * 10).toInt() / 10.0}⭐"
                        )
                    }

                    if (uiState.totalRatings >= 10) {
                        Spacer(modifier = Modifier.height(8.dp))
                        InsightItem(
                            text = "You've rated ${uiState.totalRatings} titles - great for personalized recommendations!"
                        )
                    }

                    if (uiState.watchlistCount > 0) {
                        Spacer(modifier = Modifier.height(8.dp))
                        InsightItem(
                            text = "You have ${uiState.watchlistCount} titles in your watchlist"
                        )
                    }
                }
            }

            // Bottom padding for floating tab bar
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
private fun RatingBar(
    rating: Int,
    count: Int,
    percentage: Float
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "$rating⭐",
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = CineScopeTheme.colors.textPrimary,
            modifier = Modifier.width(50.dp)
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color(0xFFE5E5E5))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(percentage)
                    .background(MaterialTheme.colorScheme.primary)
            )
        }

        Text(
            text = count.toString(),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = CineScopeTheme.colors.textSecondary,
            modifier = Modifier.width(35.dp),
            textAlign = TextAlign.End
        )
    }
}

@Composable
private fun InsightItem(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "• ",
            fontSize = 16.sp,
            color = Color.White,
            fontWeight = FontWeight.Normal
        )
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White.copy(alpha = 0.9f),
            lineHeight = 22.sp
        )
    }
}
