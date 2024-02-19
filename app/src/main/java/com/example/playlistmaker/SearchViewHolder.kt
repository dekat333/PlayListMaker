package com.example.playlistmaker

import android.content.Intent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.SearchHistory.Companion.HISTORY_KEY
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val iconTrack: ImageView
    private val trackName: TextView
    private val authorTrack: TextView
    private val timeTrack: TextView
    private val point: ImageView
    private val trackItem: LinearLayout
    private val view: View

    init {
        iconTrack = itemView.findViewById(R.id.iconTrack)
        trackName = itemView.findViewById(R.id.trackName)
        authorTrack = itemView.findViewById(R.id.authorTrack)
        timeTrack = itemView.findViewById(R.id.timeTrack)
        point = itemView.findViewById(R.id.point)
        trackItem = itemView.findViewById(R.id.trackItem)
        view = itemView

    }

    val sharedPreferences = itemView.context.getSharedPreferences(
        HISTORY_KEY,
        AppCompatActivity.MODE_PRIVATE
    )
    val searchHistory = SearchHistory(sharedPreferences)
    fun bind(model: Track) {
        trackName.text = model.trackName
        authorTrack.text = model.artistName
        timeTrack.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTimeMillis.toLong())
        Glide
            .with(trackItem)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(10))
            .into(iconTrack)


        trackItem.setOnClickListener {
            searchHistory.write(model)
            val intent = Intent(trackItem.context, AudioPlayer::class.java)
            intent.putExtra("Key", Gson().toJson(model))
            trackItem.context.startActivity(intent)
    val sharedPreferences = itemView.context.getSharedPreferences(HISTORY_KEY,
        AppCompatActivity.MODE_PRIVATE)
    val searchHistory = SearchHistory(sharedPreferences)
        fun bind(model: Track) {
            trackName.text = model.trackName
            authorTrack.text = model.artistName
            timeTrack.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTimeMillis.toLong())
            Glide
                .with(trackItem)
                .load(model.artworkUrl100)
                .placeholder(R.drawable.placeholder)
                .transform(RoundedCorners(10))
                .into(iconTrack)


            trackItem.setOnClickListener{
searchHistory.write(model)
            }
        }
    }
}