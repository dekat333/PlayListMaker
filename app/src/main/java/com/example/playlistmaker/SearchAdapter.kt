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
    // val track: List<Track> = arrayListOf()

    //private val searchHistory: SearchHistory? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return SearchViewHolder(view)
    }


    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {

        holder.bind(track[position])

        holder.itemView.setOnClickListener {
            val track = track[position]
           // Log.d("Search", track.toString())
           // searchHistory.write(track)
           onClick(track)
           // Log.d("Search", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")
            /*val intent = Intent(holder.itemView.context, AudioPlayer::class.java)
            intent.putExtra("Key", Gson().toJson(track))
            holder.itemView.context.startActivity(intent)*/
        }




    }

    override fun getItemCount(): Int {
        return track.size
    }

     //var onClick: (track: Track) -> Unit ={}


}
