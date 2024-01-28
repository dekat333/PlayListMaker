package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        val button_back = findViewById<ImageButton>(R.id.icon)

        button_back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val button_share = findViewById<FrameLayout>(R.id.share)

        button_share.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = Uri.parse("mailto:")
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.mail)))
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.web_practicum))
            startActivity(shareIntent)
        }

        val button_support = findViewById<FrameLayout>(R.id.help)

        button_support.setOnClickListener {
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.mail)))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject))
            supportIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.message))
            startActivity(supportIntent)
        }

        val button_user = findViewById<FrameLayout>(R.id.user_doc)

        button_user.setOnClickListener {
            val userIntent = Intent(Intent.ACTION_VIEW)
            userIntent.data = Uri.parse(getString(R.string.offer))
            startActivity(userIntent)
        }
    }

}