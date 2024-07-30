package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.TrackMap.toDomainModel
import com.example.playlistmaker.data.TrackMap.toDto
import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.domain.api.EntityTrackRepository
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson

class EntityTrackRepositoryImpl: EntityTrackRepository {
        private val gson = Gson()

        override fun decodeTrackDetails(trackJsonString: String): Track {
            return gson.fromJson(trackJsonString, TrackDto::class.java).toDomainModel()
        }

        override fun encodeTrackDetails(track: Track): String {
            return gson.toJson(track.toDto())
        }
}