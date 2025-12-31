package com.cinescope.cinescope.presentation.screens.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.cinescope.cinescope.presentation.components.*
import com.cinescope.cinescope.presentation.theme.Spacing
import com.cinescope.cinescope.presentation.theme.CineScopeTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DetailsScreen(
    tmdbId: Int,
    contentType: String,
    navigateBack: () -> Unit,
    viewModel: DetailsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(tmdbId) {
        viewModel.loadMovieDetails(tmdbId)
    }

    when (val state = uiState) {
        is DetailsUiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(CineScopeTheme.colors.cardBackground),
                contentAlignment = Alignment.Center
            ) {
                LoadingIndicator(message = "Loading details...")
            }
        }

        is DetailsUiState.Error -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(CineScopeTheme.colors.cardBackground),
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
                        text = state.message,
                        fontSize = 17.sp,
                        color = MaterialTheme.colorScheme.error
                    )
                    FilledButton(
                        text = "Go Back",
                        onClick = navigateBack
                    )
                }
            }
        }

        is DetailsUiState.Success -> {
            DetailsContent(
                state = state,
                onRatingChanged = viewModel::updateRating,
                onToggleWatchlist = viewModel::toggleWatchlist,
                navigateBack = navigateBack
            )
        }
    }
}

@Composable
private fun DetailsContent(
    state: DetailsUiState.Success,
    onRatingChanged: (Double) -> Unit,
    onToggleWatchlist: () -> Unit,
    navigateBack: () -> Unit
) {
    val movie = state.movie

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CineScopeTheme.colors.cardBackground)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
        ) {
            AsyncImage(
                model = movie.getBackdropUrl(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.3f),
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.6f)
                            )
                        )
                    )
            )

            IconButton(
                onClick = navigateBack,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = Spacing.lg, start = Spacing.sm)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(Spacing.md)
            ) {
                Text(
                    text = movie.title,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    movie.releaseDate?.let { date ->
                        Text(
                            text = date.take(4),
                            fontSize = 15.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }

                    movie.runtime?.let { runtime ->
                        Text(
                            text = "•",
                            fontSize = 15.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                        Text(
                            text = "${runtime}min",
                            fontSize = 15.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }

                    movie.voteAverage?.let { rating ->
                        Text(
                            text = "•",
                            fontSize = 15.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "${(rating * 10).toInt() / 10.0}",
                            fontSize = 15.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(Spacing.lg))

        CineScopeCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.md),
            onClick = onToggleWatchlist
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing.md),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (state.isInWatchlist) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (state.isInWatchlist) "Remove from Watchlist" else "Add to Watchlist",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(Spacing.lg))

        CineScopeCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.md)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Rate this movie",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CineScopeTheme.colors.textPrimary
                )

                Spacer(modifier = Modifier.height(16.dp))

                RatingStars(
                    rating = state.userRating?.rating ?: 0.0,
                    onRatingChanged = onRatingChanged,
                    starSize = 40.dp
                )

                if (state.userRating != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = state.userRating.ratingDisplay,
                        fontSize = 15.sp,
                        color = CineScopeTheme.colors.textSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(Spacing.lg))

        movie.overview?.let { overview ->
            CineScopeCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.md)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Overview",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CineScopeTheme.colors.textPrimary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = overview,
                        fontSize = 17.sp,
                        lineHeight = 24.sp,
                        color = CineScopeTheme.colors.textSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
}
