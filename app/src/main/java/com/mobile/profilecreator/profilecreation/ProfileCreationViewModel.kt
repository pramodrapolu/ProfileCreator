package com.mobile.profilecreator.profilecreation

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobile.profilecreator.R
import com.mobile.profilecreator.profileconfirmation.ProfileConfirmationActivity
import com.mobile.profilecreator.utils.Utils
import java.io.File
import java.io.IOException

/**
 * View Model for the [ProfileCreationActivity] which handles the business logic.
 */
class ProfileCreationViewModel : ViewModel() {

    companion object {
        const val REQUEST_CAMERA_PERMISSION = 1
        const val REQUEST_TAKE_PHOTO = 2

        private const val PROFILE_PIC_NAME = "ProfilePicture"
        private const val PROFILE_PIC_FORMAT = ".jpg"
        private const val FILE_PROVIDER_AUTHORITY = "com.mobile.profilecreator.fileProvider"
    }

    private var currentPhotoPath: String? = null

    private var headerText = MutableLiveData<String>()
    var headerTextLiveData: LiveData<String> = headerText

    private var helpText = MutableLiveData<String>()
    var helpTextLiveData: LiveData<String> = helpText

    private var avatarImage = MutableLiveData<String>()
    var avatarImageLiveData: LiveData<String> = avatarImage

    /**
     * Can be used to set the static data to the View or do anything during the start of the Activity
     */
    fun onCreateView(context: Context) {
        headerText.value = context.getString(R.string.profile_creation)
        helpText.value = context.getString(R.string.creation_help_text)
    }

    /**
     * This can be called as soon as the tap to add avatar button is clicked
     */
    fun onTapToAddAvatarClicked(context: Activity) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionForCamera(context)
        } else {
            startCamera(context)
        }
    }

    /**
     * This can be called as soon as the Submit button is clicked
     */
    fun onSubmitClicked(
        context: Activity,
        emailText: String,
        passwordText: String,
        firstNameText: String,
        websiteText: String
    ) {
        if (emailText.isNotEmpty() && passwordText.isNotEmpty()) {
            if (Utils.isValidEmailAddress(emailText)) {
                // Start the ProfileConfirmationActivity
                context.startActivity(
                    ProfileConfirmationActivity.createStartIntent(
                        context,
                        currentPhotoPath,
                        firstNameText,
                        emailText,
                        websiteText
                    )
                )
            } else {
                Toast.makeText(
                    context,
                    "Please check the entered Email Address",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(
                context,
                "Email Address and password are mandatory fields",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * This can be called as soon as the request permission result is back.
     */
    fun onRequestPermissionResult(
        context: Activity,
        requestCode: Int,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CAMERA_PERMISSION -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // if success Start camera
                    startCamera(context)
                } else {
                    // Permission denied! Do nothing. If the user wants to take picture again we will
                    // be requesting the permission again.
                    Toast.makeText(
                        context,
                        "Permission is Denied. If ever you want to add a " +
                                "picture go to settings and grant the permission first.",
                        Toast.LENGTH_LONG
                    ).show()
                }
                return
            }
            else -> {
                // Ignore all other request codes.
            }
        }
    }

    /**
     * As soon as the activity result is back from the Camera app.
     */
    fun onActivityResult(requestCode: Int, resultCode: Int) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == AppCompatActivity.RESULT_OK) {
            handlePhoto()
        }
    }

    /**
     * Requests the Camera Permission
     */
    private fun requestPermissionForCamera(context: Activity) {
        ActivityCompat.requestPermissions(
            context,
            arrayOf(Manifest.permission.CAMERA),
            REQUEST_CAMERA_PERMISSION
        )
    }

    private fun startCamera(context: Activity) {
        // Send the the intent to start the camera to capture a image.
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { captureImageIntent ->
            // Ensure that there's a camera activity to handle the intent
            captureImageIntent.resolveActivity(context.packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createEmptyFile(context)
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also { file ->
                    val photoURI: Uri = FileProvider.getUriForFile(
                        context,
                        FILE_PROVIDER_AUTHORITY,
                        file
                    )
                    captureImageIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    context.startActivityForResult(
                        captureImageIntent,
                        REQUEST_TAKE_PHOTO
                    )
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createEmptyFile(context: Activity): File {
        // Create an image file name
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            PROFILE_PIC_NAME,
            PROFILE_PIC_FORMAT,
            storageDir
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun handlePhoto() {
        avatarImage.value = currentPhotoPath
    }
}