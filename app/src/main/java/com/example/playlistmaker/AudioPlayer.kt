package com.example.playlistmaker

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import androidx.core.view.isVisible
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
    private lateinit var playerPlayButton: ImageView

    private var mediaPlayer = MediaPlayer()

    private var playerState = STATE_DEFAULT


    private val handler = Handler(Looper.getMainLooper())


    private fun preparePlayer(track: Track?) {
        currentPlayTime = findViewById(R.id.player_current_playtime)
        var url = track?.previewUrl
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {

            playerPlayButton.setImageDrawable(getDrawable(R.drawable.play))
            playerState = STATE_PREPARED
            handler.removeCallbacks(handlerCurrentTime)
            currentPlayTime.text = "00:00"
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun startPlayer() {

        mediaPlayer.start()
        playerPlayButton.setImageDrawable(getDrawable(R.drawable.button_play))
        playerState = STATE_PLAYING
        handler.postDelayed(handlerCurrentTime, CURRENT_DEBOUNCE_DELAY)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun pausePlayer() {
        handler.removeCallbacks(handlerCurrentTime)
        mediaPlayer.pause()
        playerPlayButton.setImageDrawable(getDrawable(R.drawable.play))
        playerState = STATE_PAUSED

    }

    private fun playbackControl() {

        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()

            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()


            }
        }

    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        handler.removeCallbacks(handlerCurrentTime)
        super.onDestroy()
        mediaPlayer.release()
    }


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
        playerPlayButton = findViewById(R.id.player_play_button)




        buttonBack.setOnClickListener {
            finish()
        }
        val trackAsJson = intent.getStringExtra(TRACK_KEY)

        Log.d("Search", trackAsJson.toString())
        val tracklist = Gson().fromJson(trackAsJson, Track::class.java)

        info_track(tracklist)
        preparePlayer(tracklist)


        playerPlayButton.setOnClickListener {
            playbackControl()
        }


    }


    private val handlerCurrentTime = object : Runnable {
        override fun run() {
            currentPlayTime.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
            handler.postDelayed(this, CURRENT_DEBOUNCE_DELAY)
            Log.d("Audio", mediaPlayer.currentPosition.toString())
        }

    }


    private fun info_track(track: Track?) {
        trackName.text = track?.trackName
        authorTrack.text = track?.artistName
        currentPlayTime.text = ""
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
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        const val CURRENT_DEBOUNCE_DELAY = 300L
    }
}