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
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        val buttonBack = findViewById<ImageButton>(R.id.icon)

        buttonBack.setOnClickListener {
            finish()
        }

        val buttonShare = findViewById<FrameLayout>(R.id.share)

        buttonShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = Uri.parse("mailto:")
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.mail)))
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.web_practicum))
            startActivity(shareIntent)
        }

        val buttonSupport = findViewById<FrameLayout>(R.id.help)

        buttonSupport.setOnClickListener {
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.mail)))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject))
            supportIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.message))
            startActivity(supportIntent)
        }

        val buttonUser = findViewById<FrameLayout>(R.id.user_doc)

        buttonUser.setOnClickListener {
            val userIntent = Intent(Intent.ACTION_VIEW)
            userIntent.data = Uri.parse(getString(R.string.offer))
            startActivity(userIntent)
        }

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        themeSwitcher.isChecked = (applicationContext as App).darkTheme
        themeSwitcher.setOnCheckedChangeListener {switcher, checked ->
            (applicationContext as App).switchTheme(checked)
        }
    }

}