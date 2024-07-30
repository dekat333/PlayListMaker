package com.example.playlistmaker

import com.example.playlistmaker.data.handler.AudioPlayerHandler
import com.example.playlistmaker.data.handler.AudioPlayerHandlerImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.network.TracksRepositoryImpl
import com.example.playlistmaker.data.repository.EntityTrackRepositoryImpl
import com.example.playlistmaker.domain.api.AudioPlayerInteractor
import com.example.playlistmaker.domain.api.EntityTrackRepository
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.impl.AudioPlayerInteractorImpl
import com.example.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    fun getEntityTrackRepository(): EntityTrackRepository {
        return EntityTrackRepositoryImpl()
    }

    fun provideAudioPlayerInteractor(): AudioPlayerInteractor {
        return AudioPlayerInteractorImpl(getPlayerHandler(), getEntityTrackRepository())
    }

    private fun getPlayerHandler(): AudioPlayerHandler {
        return AudioPlayerHandlerImpl()
    }
}