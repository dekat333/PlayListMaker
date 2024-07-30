package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.data.handler.AudioPlayerHandler
import com.example.playlistmaker.domain.api.AudioPlayerInteractor
import com.example.playlistmaker.domain.api.EntityTrackRepository
import com.example.playlistmaker.domain.models.Track

class AudioPlayerInteractorImpl(
    private val audioPlayerHandler: AudioPlayerHandler,
    private val entityTrack: EntityTrackRepository
) : AudioPlayerInteractor {
    override fun info_track(trackString: String, consumer: AudioPlayerInteractor.TrackConsumer) {
        try {
            val track = entityTrack.decodeTrackDetails(trackString)
            consumer.consume(track)
        } catch (t: Throwable) {
            consumer.error(t)
        }
    }

    override fun preparePlayer(track: Track) {
        audioPlayerHandler.preparePlayer(track)
    }

    override fun startPlayer() {
        audioPlayerHandler.startPlayer()
    }

    override fun pausePlayer() {
        audioPlayerHandler.pausePlayer()
    }

    override fun getCurrentPosition() {
        audioPlayerHandler.getCurrentPosition()
    }


    override fun onDestroy() {
        audioPlayerHandler.onDestroy()
    }

}