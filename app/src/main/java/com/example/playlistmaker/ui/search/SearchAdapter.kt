package com.example.playlistmaker.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track

class SearchAdapter(private val trackEntity: List<Track>,
                    private val onClick: (clickedTrackEntity : Track) -> Unit) : RecyclerView.Adapter<SearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return SearchViewHolder(view)
    }


    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {

        holder.bind(trackEntity[position])

        holder.itemView.setOnClickListener {
            val track = trackEntity[position]
           onClick(track)
        }




    }

    override fun getItemCount(): Int {
        return trackEntity.size
    }




}
