package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate


class App : Application() {
    private var darkTheme = false
    override fun onCreate() {
        super.onCreate()
        val sharedPrefs = getSharedPreferences(THEME, MODE_PRIVATE)
        switchTheme(sharedPrefs.getBoolean(COLOR_THEME, darkTheme))
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }

        )
        val sharedPrefs = getSharedPreferences(THEME, MODE_PRIVATE)
        sharedPrefs.edit().putBoolean(COLOR_THEME, darkTheme).apply()
    }

    companion object {
        const val THEME = "theme"
        const val COLOR_THEME = "color_theme"
    }
}