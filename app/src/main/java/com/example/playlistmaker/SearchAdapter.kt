package com.example.playlistmaker

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class SearchAdapter (
    private val track: List<Track>,
    ) : RecyclerView.Adapter<SearchViewHolder> () {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
            return SearchViewHolder(view)
        }



        override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {

            val sharedPreferences = holder.itemView.context.getSharedPreferences(
                SearchHistory.HISTORY_KEY,
                AppCompatActivity.MODE_PRIVATE
            )

            val searchHistory = SearchHistory(sharedPreferences)

            holder.itemView.setOnClickListener{
                val track = track[position]
                searchHistory.write(track)
                val intent = Intent(holder.itemView.context, AudioPlayer::class.java)
                intent.putExtra("Key", Gson().toJson(track))
                holder.itemView.context.startActivity(intent)
            }

            holder.bind(track[position])



        }

        override fun getItemCount(): Int {
            return track.size
        }

    }