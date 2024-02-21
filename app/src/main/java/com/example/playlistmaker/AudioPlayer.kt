package com.example.playlistmaker

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.Locale


class AudioPlayer : AppCompatActivity() {

    private lateinit var trackName: TextView
    private lateinit var authorTrack: TextView
    private lateinit var currentPlayTime: TextView
    private lateinit var length: TextView
    private lateinit var albumName: TextView
    private lateinit var releaseYear: TextView
    private lateinit var genre: TextView
    private lateinit var country: TextView
    private lateinit var iconTrack: ImageView
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.audiopleer)

        val buttonBack = findViewById<ImageView>(R.id.player_back_button)
        trackName = findViewById(R.id.player_track_name)
        authorTrack = findViewById(R.id.player_artist_name)
        currentPlayTime = findViewById(R.id.player_current_playtime)
        length = findViewById(R.id.track_info_playtime)
        albumName = findViewById(R.id.track_info_album)
        releaseYear = findViewById(R.id.track_info_year)
        genre = findViewById(R.id.track_info_genre)
        country = findViewById(R.id.track_info_country)
        iconTrack = findViewById(R.id.player_cover_artwork)

        buttonBack.setOnClickListener {
            finish()
        }
        val trackAsJson = intent.getStringExtra(TRACK_KEY)

         Log.d("Search", trackAsJson.toString())
       val tracklist = Gson().fromJson(trackAsJson, Track::class.java)

        info_track(tracklist)


    }


    private fun info_track(track: Track?) {
        trackName.text = track?.trackName
        authorTrack.text = track?.artistName
        currentPlayTime.text = "1:30"
        length.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track?.trackTimeMillis?.toLong())
        albumName.text = track?.collectionName
        releaseYear.text = SimpleDateFormat("yyyy", Locale.getDefault()).format(track?.releaseDate)
        genre.text = track?.primaryGenreName
        country.text = track?.country

        Glide
            .with(iconTrack)
            .load(track?.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.track_pl)
            .transform(RoundedCorners(10))
            .into(iconTrack)
    }

    companion object {
        const val TRACK_KEY = "TRACK"
    }
}