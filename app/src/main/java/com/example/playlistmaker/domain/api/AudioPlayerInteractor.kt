package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface AudioPlayerInteractor {
    fun info_track(track: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(track: Track)
        fun error(t: Throwable)
    }

    fun preparePlayer(track: Track)
    fun startPlayer()
    fun pausePlayer()
    fun getCurrentPosition()
    fun onDestroy()

}