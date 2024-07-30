package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface EntityTrackRepository {
        fun decodeTrackDetails(trackJsonString: String): Track
        fun encodeTrackDetails(track: Track): String
}