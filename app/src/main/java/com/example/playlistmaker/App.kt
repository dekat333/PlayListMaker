package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate


class App : Application() {
    var darkTheme = false
    lateinit var sharedPrefs: SharedPreferences
    override fun onCreate() {
        super.onCreate()
        sharedPrefs = getSharedPreferences(THEME, MODE_PRIVATE)
        switchTheme(sharedPrefs.getBoolean(COLOR_THEME, darkTheme))
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled

        sharedPrefs = getSharedPreferences(THEME, MODE_PRIVATE)
        sharedPrefs.edit().putBoolean(COLOR_THEME, darkTheme).apply()

        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }

        )

    }

    companion object {
        const val THEME = "theme"
        const val COLOR_THEME = "color_theme"
    }
}