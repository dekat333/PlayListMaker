package com.example.playlistmaker.data.handler

import com.example.playlistmaker.domain.models.Track

interface AudioPlayerHandler {
    fun preparePlayer(track: Track)
    fun startPlayer()
    fun pausePlayer()
    fun onDestroy()
    fun getCurrentPosition(): String

}