package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.domain.models.Track

object TrackMap {
    fun TrackDto.toDomainModel(): Track {
        return Track(
            trackId ?: -1,
            number?: -1,
            trackName?: "string",
            artistName?: "string",
            trackTimeMillis?: "string" ,
            artworkUrl100?: "string",
            collectionName?: "string",
            releaseDate?: "string",
            primaryGenreName?: "string",
            country?: "string",
            previewUrl?: "string"
        )

    }

    fun Track.toDto(): TrackDto {
        return TrackDto(
            trackId,
            number,
            trackName,
            artistName,
            trackTimeMillis,
            artworkUrl100,
            collectionName,
            releaseDate,
            primaryGenreName,
            country,
            previewUrl
        )
    }
}