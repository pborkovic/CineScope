package com.cinescope.cinescope.presentation.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cinescope.cinescope.presentation.components.*
import com.cinescope.cinescope.presentation.theme.Spacing
import com.cinescope.cinescope.presentation.theme.CineScopeTheme
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.foundation.layout.statusBarsPadding

/**
 * Apple-inspired Search Screen with clean design and spacious layout.
 */
@Composable
fun SearchScreen(
    navigateToDetails: (Int) -> Unit,
    navigateBack: () -> Unit,
    viewModel: SearchViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CineScopeTheme.colors.cardBackground)
            .statusBarsPadding()
    ) {
        Spacer(modifier = Modifier.height(Spacing.sm))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = navigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Text(
                text = "Search",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = CineScopeTheme.colors.textPrimary,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(Spacing.md))

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            CineScopeSearchBar(
                query = uiState.searchQuery,
                onQueryChange = viewModel::onSearchQueryChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.md)
            )

            Spacer(modifier = Modifier.height(Spacing.lg))

            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        LoadingIndicator(message = "Searching...")
                    }
                }

                uiState.errorMessage != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(Spacing.xl)
                        ) {
                            Text(
                                text = "⚠️",
                                fontSize = 48.sp
                            )
                            Spacer(modifier = Modifier.height(Spacing.md))
                            Text(
                                text = uiState.errorMessage ?: "An error occurred",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }

                uiState.searchResults.isEmpty() && uiState.searchQuery.isNotEmpty() -> {
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
                                text = "No results found",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = CineScopeTheme.colors.textPrimary
                            )
                            Spacer(modifier = Modifier.height(Spacing.sm))
                            Text(
                                text = "Try searching with different keywords",
                                fontSize = 16.sp,
                                color = CineScopeTheme.colors.textSecondary
                            )
                        }
                    }
                }

                uiState.searchResults.isEmpty() -> {
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
                                text = "Search for movies",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = CineScopeTheme.colors.textPrimary
                            )
                            Spacer(modifier = Modifier.height(Spacing.sm))
                            Text(
                                text = "Find your favorite films and series",
                                fontSize = 16.sp,
                                color = CineScopeTheme.colors.textSecondary
                            )
                        }
                    }
                }

                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(150.dp),
                        contentPadding = PaddingValues(
                            start = Spacing.md,
                            end = Spacing.md,
                            bottom = 100.dp
                        ),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.searchResults) { movie ->
                            MovieCard(
                                movie = movie,
                                onClick = {
                                    viewModel.addToLibrary(movie)
                                    navigateToDetails(movie.tmdbId)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
