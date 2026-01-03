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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
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
import com.cinescope.cinescope.domain.model.ThemePreference
import com.cinescope.cinescope.presentation.components.CineScopeCard
import com.cinescope.cinescope.presentation.components.rememberImagePickerLauncher
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

    LaunchedEffect(uiState.userProfile.name) {
        nameInput = uiState.userProfile.name ?: ""
    }

    val imagePickerLauncher = rememberImagePickerLauncher { imagePath ->
        imagePath?.let { viewModel.updateProfilePicture(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CineScopeTheme.colors.cardBackground)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(Spacing.md))

        Text(
            text = "Settings",
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            color = CineScopeTheme.colors.textPrimary,
            modifier = Modifier.padding(horizontal = Spacing.md)
        )

        Spacer(modifier = Modifier.height(Spacing.xl))

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
                if (uiState.userProfile.profilePicturePath != null) {
                    AsyncImage(
                        model = uiState.userProfile.profilePicturePath,
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
                            .clickable {
                                imagePickerLauncher.launch()
                            }
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                            .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
                            .clickable {
                                imagePickerLauncher.launch()
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

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .align(Alignment.BottomEnd)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable {
                            imagePickerLauncher.launch()
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

        Text(
            text = "Profile Information",
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

        Text(
            text = "Appearance",
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
                Text(
                    text = "Theme",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = CineScopeTheme.colors.textSecondary
                )

                Spacer(modifier = Modifier.height(8.dp))

                ThemeDropdown(
                    selectedTheme = uiState.userProfile.themePreference,
                    onThemeSelected = { theme ->
                        viewModel.updateThemePreference(theme)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(Spacing.xl))

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

@Composable
private fun ThemeDropdown(
    selectedTheme: ThemePreference,
    onThemeSelected: (ThemePreference) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val themes = listOf(
        ThemePreference.SYSTEM,
        ThemePreference.LIGHT,
        ThemePreference.DARK
    )

    Box {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .background(
                    color = Color.Transparent,
                    shape = RoundedCornerShape(12.dp)
                )
                .border(
                    width = 1.dp,
                    color = CineScopeTheme.colors.textSecondary.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedTheme.toDisplayName(),
                fontSize = 17.sp,
                fontWeight = FontWeight.Normal,
                color = CineScopeTheme.colors.textPrimary
            )

            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Select theme",
                tint = CineScopeTheme.colors.textSecondary,
                modifier = Modifier.size(24.dp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(CineScopeTheme.colors.cardBackground)
                .border(
                    width = 1.dp,
                    color = CineScopeTheme.colors.textSecondary.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp)
                )
        ) {
            themes.forEach { theme ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = theme.toDisplayName(),
                            fontSize = 17.sp,
                            fontWeight = if (theme == selectedTheme) FontWeight.SemiBold else FontWeight.Normal,
                            color = if (theme == selectedTheme)
                                MaterialTheme.colorScheme.primary
                            else
                                CineScopeTheme.colors.textPrimary
                        )
                    },
                    onClick = {
                        onThemeSelected(theme)
                        expanded = false
                    }
                )
            }
        }
    }
}
