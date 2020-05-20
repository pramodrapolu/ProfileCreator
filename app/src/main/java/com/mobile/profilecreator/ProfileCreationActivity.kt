package com.mobile.profilecreator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class ProfileCreationActivity : AppCompatActivity() {

    private lateinit var profileCreationViewModel: ProfileCreationViewModel

    private lateinit var headerTextView: TextView
    private lateinit var helpTextView: TextView
    private lateinit var tapView: TextView
    private lateinit var avatarView: ImageView

    private lateinit var firstNameField: EditText
    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var websiteField: EditText

    private lateinit var submitButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_creation)

        // Get the View model from the provider.
        profileCreationViewModel = ViewModelProvider(this).get(ProfileCreationViewModel::class.java)
        // Call onCreateView on viewModel so that it can know that view is created.
        profileCreationViewModel.onCreateView(this)

        initViews()

        handleClicks()

        handleDataFromViewModel()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        profileCreationViewModel.onRequestPermissionResult(
            this,
            requestCode,
            grantResults
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        profileCreationViewModel.onActivityResult(requestCode, resultCode)
    }

    /**
     * Initializes all the views
     */
    private fun initViews() {
        headerTextView = findViewById(R.id.header_text)
        helpTextView = findViewById(R.id.help_text)
        tapView = findViewById(R.id.tap_view)
        avatarView = findViewById(R.id.avatar_view)

        firstNameField = findViewById(R.id.first_name_field)
        emailField = findViewById(R.id.email_field)
        passwordField = findViewById(R.id.password_field)
        websiteField = findViewById(R.id.website_field)

        submitButton = findViewById(R.id.submit_button)
    }

    /**
     * Handles the clicks of action buttons/views
     */
    private fun handleClicks() {
        tapView.setOnClickListener {
            profileCreationViewModel.onTapToAddAvatarClicked(this)
        }

        submitButton.setOnClickListener {
            profileCreationViewModel.onSubmitClicked(
                this,
                emailField.text.toString(),
                passwordField.text.toString(),
                firstNameField.text.toString(),
                websiteField.text.toString()
            )
        }
    }

    /**
     * Listen to the Data from the ViewModel and sets it to the Views.
     */
    private fun handleDataFromViewModel() {
        profileCreationViewModel.headerTextLiveData.observe(this, Observer {
            headerTextView.text = it
        })

        profileCreationViewModel.helpTextLiveData.observe(this, Observer {
            helpTextView.text = it
        })

        profileCreationViewModel.avatarImageLiveData.observe(this, Observer {
            avatarView.setImageBitmap(it)
            avatarView.visibility = View.VISIBLE
            tapView.visibility = View.GONE
        })
    }
}
