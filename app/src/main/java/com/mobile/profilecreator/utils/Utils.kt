package com.mobile.profilecreator.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Patterns
import androidx.exifinterface.media.ExifInterface
import java.io.IOException

object Utils {
    /**
     * Checks if the provided emailAddress is valid or not with pattern matching.
     */
    fun isValidEmailAddress(emailAddress: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()
    }

    /**
     * Returns a spannable string to get the underline text for the provided text
     */
    fun getUnderlinedText(text: String): SpannableString {
        val content = SpannableString(text)
        content.setSpan(UnderlineSpan(), 0, text.length, 0)
        return content
    }
}
