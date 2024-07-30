package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface SearchHistoryRepository {
    fun read(): ArrayList<Track>
    fun write(trackEntity: Track)
    fun clear()
}