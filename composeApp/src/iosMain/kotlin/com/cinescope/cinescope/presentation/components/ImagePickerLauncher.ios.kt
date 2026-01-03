package com.cinescope.cinescope.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSData
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUUID
import platform.Foundation.NSUserDomainMask
import platform.Foundation.writeToFile
import platform.PhotosUI.PHPickerConfiguration
import platform.PhotosUI.PHPickerResult
import platform.PhotosUI.PHPickerViewController
import platform.PhotosUI.PHPickerViewControllerDelegateProtocol
import platform.UIKit.UIApplication
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.darwin.NSObject
import platform.UniformTypeIdentifiers.UTTypeImage

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun rememberImagePickerLauncher(
    onImageSelected: (String?) -> Unit
): ImagePickerLauncher {
    return remember {
        object : ImagePickerLauncher {
            override fun launch() {
                val configuration = PHPickerConfiguration()
                configuration.selectionLimit = 1

                val picker = PHPickerViewController(configuration)
                picker.delegate = object : NSObject(), PHPickerViewControllerDelegateProtocol {
                    override fun picker(
                        picker: PHPickerViewController,
                        didFinishPicking: List<*>
                    ) {
                        picker.dismissViewControllerAnimated(true, null)

                        @Suppress("UNCHECKED_CAST")
                        val results = didFinishPicking as? List<PHPickerResult>

                        if (results.isNullOrEmpty()) {
                            onImageSelected(null)
                            return
                        }

                        val result = results.first()
                        val provider = result.itemProvider

                        provider.loadDataRepresentationForTypeIdentifier(
                            typeIdentifier = UTTypeImage.identifier,
                            completionHandler = { data, error ->
                                if (error != null || data == null) {
                                    onImageSelected(null)
                                    return@loadDataRepresentationForTypeIdentifier
                                }

                                val image = UIImage.imageWithData(data)
                                val savedPath = image?.let { saveImageToDocuments(it) }
                                onImageSelected(savedPath)
                            }
                        )
                    }
                }

                UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
                    picker,
                    animated = true,
                    completion = null
                )
            }
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun saveImageToDocuments(image: UIImage): String? {
    return try {
        val imageData = UIImageJPEGRepresentation(image, 0.8) ?: return null

        val documentsPaths = NSSearchPathForDirectoriesInDomains(
            NSDocumentDirectory,
            NSUserDomainMask,
            true
        )
        val documentsPath = documentsPaths.firstOrNull() as? String ?: return null

        val profileDir = "$documentsPath/profile_images"
        val fileManager = NSFileManager.defaultManager

        if (!fileManager.fileExistsAtPath(profileDir)) {
            fileManager.createDirectoryAtPath(
                profileDir,
                withIntermediateDirectories = true,
                attributes = null,
                error = null
            )
        }

        val filename = "profile_${NSUUID().UUIDString}.jpg"
        val filePath = "$profileDir/$filename"

        (imageData as NSData).writeToFile(
            filePath,
            atomically = true
        )

        filePath
    } catch (e: Exception) {
        e.printStackTrace()

        null
    }
}
