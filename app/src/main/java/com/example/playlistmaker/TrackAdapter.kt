package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter (
    private val track: List<Track>,
    private val searchHistory: SearchHistory
) : RecyclerView.Adapter<SearchViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(track[position])
        holder.itemView.setOnClickListener {
            searchHistory.write(track[position])}
    }

    override fun getItemCount(): Int {
        return track.size
    }

}