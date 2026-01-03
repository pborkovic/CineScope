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
import platform.UIKit.UIApplication
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UIImagePickerControllerOriginalImage
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun rememberImagePickerLauncher(
    onImageSelected: (String?) -> Unit
): ImagePickerLauncher {
    return remember {
        object : ImagePickerLauncher {
            override fun launch() {
                val picker = UIImagePickerController()
                picker.sourceType = UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary
                picker.allowsEditing = false

                picker.delegate = object : NSObject(),
                    UIImagePickerControllerDelegateProtocol,
                    UINavigationControllerDelegateProtocol {

                    override fun imagePickerController(
                        picker: UIImagePickerController,
                        didFinishPickingMediaWithInfo: Map<Any?, *>
                    ) {
                        picker.dismissViewControllerAnimated(true, null)

                        val image = didFinishPickingMediaWithInfo[UIImagePickerControllerOriginalImage] as? UIImage
                        val savedPath = image?.let { saveImageToDocuments(it) }
                        onImageSelected(savedPath)
                    }

                    override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
                        picker.dismissViewControllerAnimated(true, null)
                        onImageSelected(null)
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
