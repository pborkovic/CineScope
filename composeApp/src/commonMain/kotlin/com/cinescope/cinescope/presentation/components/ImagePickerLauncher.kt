package com.cinescope.cinescope.presentation.components

import androidx.compose.runtime.Composable

/**
 * Image picker launcher that provides a platform-specific image selection functionality.
 */
interface ImagePickerLauncher {
    /**
     * Launches the image picker.
     */
    fun launch()
}

/**
 * Remembers an image picker launcher that can be used to select images from the device.
 *
 * @param onImageSelected Callback invoked when an image is selected, providing the file path.
 *                        Returns null if selection was cancelled.
 * @return An [ImagePickerLauncher] that can be used to launch the image picker.
 */
@Composable
expect fun rememberImagePickerLauncher(
    onImageSelected: (String?) -> Unit
): ImagePickerLauncher
