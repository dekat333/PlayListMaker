package com.example.playlistmaker.ui.search

import android.content.SharedPreferences
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class SearchHistory(private val sharedPreferences: SharedPreferences) {




    fun read(): ArrayList<Track> {
        val json = sharedPreferences.getString(HISTORY_KEY, null)
        var jsonList = if (json != null) {
            val type: Type = object : TypeToken<ArrayList<Track>>() {}.type
            Gson().fromJson(json, type)
        } else {
            arrayListOf<Track>()
        }
        return jsonList
    }

    fun write(trackEntity: Track) {

        var trackListHistory = read()

            var trackDouble = trackListHistory.find { it.trackId == trackEntity.trackId }
            trackListHistory.remove(trackDouble)

            if (trackListHistory.size < 10) {
                trackListHistory.add(0, trackEntity)
            }else{
                trackListHistory.removeAt(9)
                trackListHistory.add(0, trackEntity)
            }

        val json = Gson().toJson(trackListHistory)
        sharedPreferences.edit()
            .putString(HISTORY_KEY, json)
            .apply()
    }


    fun clear(){
        sharedPreferences.edit().remove(HISTORY_KEY).apply()
    }

    companion object {
        const val HISTORY_KEY = "history_key"
        const val ADD_KEY = "add_key"
    }
}