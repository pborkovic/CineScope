package com.cinescope.cinescope.presentation.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.cinescope.cinescope.presentation.components.CineScopeCard
import com.cinescope.cinescope.presentation.theme.Spacing
import com.cinescope.cinescope.presentation.theme.CineScopeTheme
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.foundation.layout.statusBarsPadding

@Composable
fun SettingsScreen(
    navigateBack: () -> Unit = {},
    viewModel: SettingsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var nameInput by remember { mutableStateOf("") }

    // Initialize name input when profile loads
    LaunchedEffect(uiState.userProfile.name) {
        nameInput = uiState.userProfile.name ?: ""
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CineScopeTheme.colors.cardBackground)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {
        // Top spacing
        Spacer(modifier = Modifier.height(Spacing.md))

        // Header
        Text(
            text = "Settings",
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            color = CineScopeTheme.colors.textPrimary,
            modifier = Modifier.padding(horizontal = Spacing.md)
        )

        Spacer(modifier = Modifier.height(Spacing.xl))

        // Profile Picture Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.md),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(120.dp),
                contentAlignment = Alignment.Center
            ) {
                // Profile picture or placeholder
                if (uiState.userProfile.profilePicturePath != null) {
                    AsyncImage(
                        model = uiState.userProfile.profilePicturePath,
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
                            .clickable {
                                // TODO: Implement image picker
                            }
                    )
                } else {
                    // Placeholder
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                            .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
                            .clickable {
                                // TODO: Implement image picker
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(60.dp)
                        )
                    }
                }

                // Camera icon overlay
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .align(Alignment.BottomEnd)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable {
                            // TODO: Implement image picker
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Change Picture",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Tap to change profile picture",
                fontSize = 14.sp,
                color = CineScopeTheme.colors.textSecondary
            )
        }

        Spacer(modifier = Modifier.height(Spacing.xl))

        // Profile Information Section
        Text(
            text = "Profile Information",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = CineScopeTheme.colors.textPrimary,
            modifier = Modifier.padding(horizontal = Spacing.md)
        )

        Spacer(modifier = Modifier.height(Spacing.md))

        // Name Input Card
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
                    text = "Name",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = CineScopeTheme.colors.textSecondary
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = nameInput,
                    onValueChange = { newValue ->
                        nameInput = newValue
                        viewModel.updateUserName(newValue)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = "Enter your name",
                            color = CineScopeTheme.colors.textSecondary
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = CineScopeTheme.colors.textPrimary,
                        unfocusedTextColor = CineScopeTheme.colors.textPrimary
                    ),
                    singleLine = true,
                    textStyle = androidx.compose.ui.text.TextStyle(
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Normal
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(Spacing.xl))

        // App Information Section
        Text(
            text = "About",
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
                    .padding(20.dp)
            ) {
                SettingRow(label = "App Name", value = "CineScope")
                Spacer(modifier = Modifier.height(16.dp))
                SettingRow(label = "Version", value = "1.0.0")
                Spacer(modifier = Modifier.height(16.dp))
                SettingRow(label = "Platform", value = "Kotlin Multiplatform")
            }
        }

        // Bottom padding for floating tab bar
        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
private fun SettingRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 17.sp,
            fontWeight = FontWeight.Normal,
            color = CineScopeTheme.colors.textPrimary
        )

        Text(
            text = value,
            fontSize = 17.sp,
            fontWeight = FontWeight.Normal,
            color = CineScopeTheme.colors.textSecondary
        )
    }
}
