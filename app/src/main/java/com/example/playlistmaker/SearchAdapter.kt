package com.example.playlistmaker

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class SearchAdapter(private val track: List<Track>,
                    private val onClick: (clickedTrack : Track) -> Unit) : RecyclerView.Adapter<SearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return SearchViewHolder(view)
    }


    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {

        holder.bind(track[position])

        holder.itemView.setOnClickListener {
            val track = track[position]
           onClick(track)
        }
    }

    override fun getItemCount(): Int {
        return track.size
    }
}
