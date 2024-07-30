package com.example.playlistmaker.data.dto

import java.io.Serializable

data class TrackDto(
    val trackId: Int?,
    val number: Int?, //Номер трека
    val trackName: String?, // Название композиции
    val artistName: String?, // Имя исполнителя
    val trackTimeMillis: String?, // Продолжительность трека
    val artworkUrl100: String?, // Ссылка на изображение обложки
    val collectionName: String?,
    val releaseDate: Serializable?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?
)