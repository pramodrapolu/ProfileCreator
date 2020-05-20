package com.mobile.profilecreator.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Patterns
import androidx.exifinterface.media.ExifInterface
import java.io.IOException

object Utils {

    /**
     * Gets the decoded bitmap from the path provided
     */
    fun getDecodedFile (currentPhotoPath: String?): Bitmap? {
        if (currentPhotoPath != null) {
            val decodedFile = BitmapFactory.decodeFile(currentPhotoPath)
            if (decodedFile != null) {
                // For Some reason the picture was rotated when the pic is taken so below workaround
                // to see if the pic is rotated, if yes we will rotate.
                return rotateImageIfRequired(decodedFile, currentPhotoPath)
            }
        }
        return null
    }

    /**
     * Checks if the provided emailAddress is valid or not with pattern matching.
     */
    fun isValidEmailAddress(emailAddress: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()
    }

    /**
     * Rotate an image if required
     *
     * @param bitmap        The image bitmap
     * @param selectedImage Image URI
     * @return The resulted Bitmap after manipulation
     */
    @Throws(IOException::class)
    private fun rotateImageIfRequired(bitmap: Bitmap, selectedImage: String): Bitmap? {

        try {
            val exifInterface = ExifInterface(selectedImage)

            return when (exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90)
                ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180)
                ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270)
                else -> bitmap
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    private fun rotateImage(bitmap: Bitmap, degree: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val rotatedImg = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        bitmap.recycle()
        return rotatedImg
    }
}
