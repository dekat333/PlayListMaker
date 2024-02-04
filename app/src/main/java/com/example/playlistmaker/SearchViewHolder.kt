package com.example.playlistmaker

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

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
        fun bind(model: Track) {
            trackName.text = model.trackName
            authorTrack.text = model.artistName
            timeTrack.text = model.trackTime
            Glide
                .with(trackItem)
                .load(model.artworkUrl100)
                .placeholder(R.drawable.placeholder)
                .transform(RoundedCorners(10))
                .into(iconTrack)
        }
    }