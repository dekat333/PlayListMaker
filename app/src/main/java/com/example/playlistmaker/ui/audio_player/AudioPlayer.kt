package com.example.playlistmaker.ui.audio_player

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.AudioPlayerInteractor
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson
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
    private lateinit var audioPlayerInteractor: AudioPlayerInteractor

    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private val handler = Handler(Looper.getMainLooper())


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun preparePlayer(trackEntity: Track?) {
        currentPlayTime = findViewById(R.id.player_current_playtime)
        val url = trackEntity?.previewUrl
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

        audioPlayerInteractor.startPlayer()
        playerPlayButton.setImageDrawable(getDrawable(R.drawable.button_play))
        handler.postDelayed(handlerCurrentTime, CURRENT_DEBOUNCE_DELAY)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun pausePlayer() {
        audioPlayerInteractor.pausePlayer()
        handler.removeCallbacks(handlerCurrentTime)
        playerPlayButton.setImageDrawable(getDrawable(R.drawable.play))


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
        audioPlayerInteractor.onDestroy()
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.audiopleer)
        audioPlayerInteractor = Creator.provideAudioPlayerInteractor()

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
        val trackAsJson = intent.getStringExtra(TRACK_KEY)?: ""

        val tracklist = Gson().fromJson(trackAsJson, Track::class.java)
        info_track(trackAsJson)
        trackDetails(tracklist)
        preparePlayer(tracklist)


        playerPlayButton.setOnClickListener {
            playbackControl()
        }


    }


    private val handlerCurrentTime = object : Runnable {
        override fun run() {
            currentPlayTime.text =
                audioPlayerInteractor.getCurrentPosition().toString()
            handler.postDelayed(this, CURRENT_DEBOUNCE_DELAY)

        }

    }


    private fun info_track(trackEntity: String) {

        audioPlayerInteractor.info_track(trackEntity, object : AudioPlayerInteractor.TrackConsumer{
            override fun consume(track: Track) {
                runOnUiThread {
                    trackDetails(track)
                    preparePlayer(track)
                }
            }

            override fun error(t: Throwable) {
                runOnUiThread {
                    Log.e(TAG, "Ошибка: ${t.message}", t)
                }
            }


        })
    }

    private fun trackDetails(trackEntity: Track){
            trackName.text = trackEntity.trackName
            authorTrack.text = trackEntity.artistName
            currentPlayTime.text = ""
            length.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackEntity.trackTimeMillis.toLong())
            albumName.text = trackEntity.collectionName
            releaseYear.text = SimpleDateFormat("yyyy", Locale.getDefault()).format(trackEntity.releaseDate)
            genre.text = trackEntity.primaryGenreName
            country.text = trackEntity.country

            Glide
                .with(iconTrack)
                .load(trackEntity.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
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