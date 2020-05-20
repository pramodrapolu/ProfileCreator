package com.mobile.profilecreator.profileconfirmation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.mobile.profilecreator.R
import com.mobile.profilecreator.utils.Utils

/**
 * Activity for the Confirmation Screen.
 *
 * Didn't create a view model for this as not much is gonna be in that.
 * But the idea of using View model is showed for the first screen.
 */
class ProfileConfirmationActivity : AppCompatActivity() {

    companion object {
        private const val AVATAR_PATH = "avatar_path"
        private const val FIRST_NAME = "first_name"
        private const val EMAIL_ADDRESS = "email_address"
        private const val WEBSITE = "website"

        fun createStartIntent(
            context: Context,
            avatarPath: String?,
            firstName: String?,
            emailAddress: String,
            website: String?
        ): Intent {
            val intent = Intent(context, ProfileConfirmationActivity::class.java)
            avatarPath?.let { intent.putExtra(AVATAR_PATH, avatarPath) }
            firstName?.let { intent.putExtra(FIRST_NAME, firstName) }
            intent.putExtra(EMAIL_ADDRESS, emailAddress)
            website?.let { intent.putExtra(WEBSITE, website) }

            return intent
        }
    }

    private lateinit var headerTextView: TextView
    private lateinit var helpTextView: TextView
    private lateinit var avatarView: ImageView
    private lateinit var tapView: Button
    private lateinit var websiteTextView: TextView
    private lateinit var firsNameTextView: TextView
    private lateinit var emailTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_confirmation)

        val avatarPath = intent.extras?.getString(AVATAR_PATH)
        val firstName = intent.extras?.getString(FIRST_NAME)
        val emailAddress = intent.extras?.getString(EMAIL_ADDRESS)
        val website = intent.extras?.getString(WEBSITE)

        overridePendingTransition(R.anim.slide_in_left, R.anim.no_transition_change)

        initViews()

        populateData(avatarPath, firstName, emailAddress, website)
    }

    /**
     * Initializes the views
     */
    private fun initViews() {
        headerTextView = findViewById(R.id.header_text)
        helpTextView = findViewById(R.id.help_text)
        avatarView = findViewById(R.id.avatar_view)
        tapView = findViewById(R.id.tap_view)
        websiteTextView = findViewById(R.id.website_confirmation_text)
        firsNameTextView = findViewById(R.id.first_name_confirmation_text)
        emailTextView = findViewById(R.id.email_confirmation_text)
    }

    /**
     * Populates the UI views with the provided data.
     */
    private fun populateData(
        avatarPath: String?,
        firstName: String?,
        emailAddress: String?,
        website: String?
    ) {
        helpTextView.text = getString(R.string.confirmation_help_text)
        tapView.visibility = View.GONE
        avatarView.visibility = View.VISIBLE

        if (firstName?.isNotEmpty() == true) {
            headerTextView.text = getString(R.string.hello_text_with_name, firstName)
            firsNameTextView.text = firstName
            firsNameTextView.visibility = View.VISIBLE
        } else {
            headerTextView.text = getString(R.string.hello_text)
            firsNameTextView.visibility = View.GONE
        }

        avatarPath?.let {
            Utils.getDecodedFile(it)?.let { avatarBitmap ->
                avatarView.setImageBitmap(avatarBitmap)
            }
        }

        emailAddress?.let {
            emailTextView.text = it
        }

        if (website?.isNotEmpty() == true) {
            websiteTextView.text = Utils.getUnderlinedText(website)
            websiteTextView.visibility = View.VISIBLE
        } else {
            websiteTextView.visibility = View.GONE
        }
    }
}
