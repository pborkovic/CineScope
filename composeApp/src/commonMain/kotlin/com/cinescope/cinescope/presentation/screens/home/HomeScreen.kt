package com.cinescope.cinescope.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cinescope.cinescope.presentation.components.*
import com.cinescope.cinescope.presentation.theme.Gradients
import com.cinescope.cinescope.presentation.theme.Spacing
import com.cinescope.cinescope.presentation.theme.CineScopeTheme
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.foundation.layout.statusBarsPadding

/**
 * Apple-inspired Home Screen with clean design and vibrant gradients.
 *
 * Design principles:
 * - Clean white background
 * - Generous whitespace (32dp sections)
 * - Vibrant gradient hero card
 * - iOS-style shadows and cards
 * - Large, bold typography
 * - Minimal visual clutter
 */
@Composable
fun HomeScreen(
    navigateToDetails: (Int) -> Unit,
    navigateToSearch: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val recentMovies = uiState.recentMovies
    val totalMoviesWatched = uiState.totalMoviesWatched
    val isLoading = uiState.isLoading
    val userName = uiState.userName

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CineScopeTheme.colors.cardBackground)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {
            // Top padding
            Spacer(modifier = Modifier.height(Spacing.md))

            // Greeting header
            Column(
                modifier = Modifier.padding(horizontal = Spacing.md)
            ) {
                Text(
                    text = "Welcome Back${if (userName != null) "," else ""}",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    color = CineScopeTheme.colors.textPrimary
                )

                if (userName != null) {
                    Text(
                        text = userName,
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(Spacing.lg))

            // Hero section (shown when no movies watched)
            if (totalMoviesWatched == 0L) {
                GradientCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Spacing.md),
                    gradient = Gradients.Ocean,
                    onClick = navigateToSearch
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CineScopeIcon(
                            size = 100.dp,
                            showBackground = false
                        )

                        Spacer(modifier = Modifier.height(Spacing.md))

                        Text(
                            text = "Welcome to CineScope",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(Spacing.sm))

                        Text(
                            text = "Track • Rate • Discover",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.White.copy(alpha = 0.9f)
                        )

                        Spacer(modifier = Modifier.height(Spacing.lg))

                        FilledButton(
                            text = "Search Movies",
                            onClick = navigateToSearch,
                            icon = Icons.Default.Search,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(Spacing.xl))
            }

            // Stats section (shown when has watched movies)
            if (totalMoviesWatched > 0L) {
                CineScopeCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Spacing.md)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Spacing.lg),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StatsItem(
                            value = totalMoviesWatched.toString(),
                            label = "Movies",
                            modifier = Modifier.weight(1f)
                        )

                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(40.dp)
                                .background(CineScopeTheme.colors.divider)
                        )

                        StatsItem(
                            value = recentMovies.size.toString(),
                            label = "Recent",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(Spacing.xl))
            }

            // Recently Added section
            if (recentMovies.isNotEmpty()) {
                SectionHeader(
                    title = "Recently Added",
                    actionText = "See All",
                    onActionClick = { /* TODO: Navigate to full list */ }
                )

                Spacer(modifier = Modifier.height(Spacing.md))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = Spacing.md)
                ) {
                    items(recentMovies) { movie ->
                        MovieCard(
                            movie = movie,
                            onClick = { navigateToDetails(movie.tmdbId) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(Spacing.xl))
            }

            // Getting Started guide (shown when no movies)
            if (totalMoviesWatched == 0L) {
                CineScopeCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Spacing.md)
                ) {
                    Column(
                        modifier = Modifier.padding(Spacing.lg)
                    ) {
                        Text(
                            text = "Get Started",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = CineScopeTheme.colors.textPrimary
                        )

                        Spacer(modifier = Modifier.height(Spacing.md))

                        GetStartedItem(
                            icon = Icons.Default.Search,
                            title = "Search for movies",
                            description = "Find your favorite films in our extensive catalog"
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        GetStartedItem(
                            icon = Icons.Default.Star,
                            title = "Rate what you've watched",
                            description = "Keep track of your viewing history and ratings"
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        GetStartedItem(
                            icon = Icons.Default.Movie,
                            title = "Get recommendations",
                            description = "Discover new movies based on your taste"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(Spacing.xl))
            }

            // Bottom padding for floating tab bar
            Spacer(modifier = Modifier.height(100.dp))
        }
}

@Composable
private fun GetStartedItem(
    icon: ImageVector,
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = CineScopeTheme.colors.textPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = description,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = CineScopeTheme.colors.textSecondary,
                lineHeight = 18.sp
            )
        }
    }
}
