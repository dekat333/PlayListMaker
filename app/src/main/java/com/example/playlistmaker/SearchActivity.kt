package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.SearchHistory.Companion.HISTORY_KEY
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {
    private var input = ""
    private lateinit var searchline: EditText
    private lateinit var trackItem: LinearLayout
    private lateinit var searchHistory: SearchHistory


    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val trackApiService = retrofit.create<ITunesAPI>()

    private val trackListener: (Track) -> Unit = { model ->
        searchHistory.write(model)
        val intent = Intent(this, AudioPlayer::class.java)
        val track = Gson().toJson(model)
        intent.putExtra(AudioPlayer.TRACK_KEY, track)
        startActivity(intent)
    }

    private val trackHistoryListener: (Track) -> Unit = { model ->
        //searchHistory.write(model)
        val intent = Intent(this, AudioPlayer::class.java)
        val track = Gson().toJson(model)
        intent.putExtra(AudioPlayer.TRACK_KEY, track)
        startActivity(intent)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        searchline = findViewById(R.id.search_line)
        val clearButton = findViewById<ImageButton>(R.id.clear_button)
        val buttonBack = findViewById<ImageButton>(R.id.icon1)
        val recyclerView = findViewById<RecyclerView>(R.id.searchList)
        recyclerView.isVisible = false
        buttonBack.setOnClickListener {
            finish()

        }



        val sharedPref = getSharedPreferences(HISTORY_KEY, MODE_PRIVATE)


        val textHistory = findViewById<TextView>(R.id.TextHistory)

        val historyRecyclerView = findViewById<RecyclerView>(R.id.historyList)
        val clearHistory = findViewById<Button>(R.id.clearHistory)


        searchHistory = SearchHistory(sharedPref)
        val adapter = SearchAdapter(searchHistory.read(), trackHistoryListener)




        historyRecyclerView.adapter = adapter



        clearHistory.setOnClickListener {
            searchHistory.clear()
            adapter.notifyDataSetChanged()
            historyRecyclerView.isVisible = false
            textHistory.isVisible = false
            clearHistory.isVisible = false
            searchline.clearFocus()
        }

        val buttonReturn = findViewById<Button>(R.id.buttonReturn)


        searchline.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && searchline.text.isEmpty() && sharedPref.getString(
                    HISTORY_KEY,
                    null
                ) != null
            ) {


                historyRecyclerView.isVisible = true
                textHistory.isVisible = true
                clearHistory.isVisible = true
                historyRecyclerView.adapter = SearchAdapter(searchHistory.read(), trackHistoryListener)
            } else historyRecyclerView.isVisible = false
        }

        searchline.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchTrack()
            }
            false
        }


        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.isVisible = !s.isNullOrEmpty()
                input = searchline.text.toString()
                searchline.requestFocus()
                historyRecyclerView.isVisible = false
                textHistory.isVisible = false
                clearHistory.isVisible = false

                clearButton.setOnClickListener {
                    searchline.setText("")
                    val inputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    inputMethodManager?.hideSoftInputFromWindow(searchline.windowToken, 0)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }


        }

        searchline.addTextChangedListener(searchTextWatcher)


        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = SearchAdapter(trackList, trackListener)





        /*SearchAdapter(trackList).onClick = { model ->
            searchHistory.write(model)
           // Log.d("Search", "NNNNNNNNNNNNNAAAAAAAAAAAAAAAAAAAAAAAA")
            val intent = Intent(this, AudioPlayer::class.java)
            val track = Gson().toJson(model)
            intent.putExtra(AudioPlayer.TRACK_KEY, track)
          //  Log.d("Search", "SSSSSSSSSSSSSAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")
            startActivity(intent)
        }*/

        buttonReturn.setOnClickListener {
            searchTrack()

        }



    }

    private fun searchTrack() {
        val buttonReturn = findViewById<Button>(R.id.buttonReturn)
        val recyclerView = findViewById<RecyclerView>(R.id.searchList)
        val clearButton = findViewById<ImageButton>(R.id.clear_button)
        val ImageNothing = findViewById<ImageView>(R.id.placeholders)
        val TextNothing = findViewById<TextView>(R.id.TextHolder)
        val ImageNoInternet = findViewById<ImageView>(R.id.no_internet)
        val TextNoInternet = findViewById<TextView>(R.id.TextHolderNoInternet)
        val TextNoInternet2 = findViewById<TextView>(R.id.TextHolderNoInternet2)
        val textHistory = findViewById<TextView>(R.id.TextHistory)
        val historyRecyclerView = findViewById<RecyclerView>(R.id.historyList)
        val clearHistory = findViewById<Button>(R.id.clearHistory)
        trackApiService.search(searchline.text.toString())
            .enqueue(object : Callback<TrackResponse> {
                //@SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>
                ) {
                    ImageNoInternet.isVisible = false
                    TextNoInternet.isVisible = false
                    TextNoInternet2.isVisible = false
                    ImageNothing.isVisible = false
                    TextNothing.isVisible = false
                    buttonReturn.isVisible = false
                    textHistory.isVisible = false
                    clearHistory.isVisible = false
                    historyRecyclerView.isVisible = false
                    recyclerView.isVisible = false
                    if (response.isSuccessful) {
                        Log.d("Search", response.body()?.results.toString())
                        trackList.clear()
                        val trackAnswer = response.body()?.results

                        if (trackAnswer?.isNotEmpty() == true) {
                            Log.d("Search", recyclerView.isVisible.toString())
                            recyclerView.isVisible = true
                            Log.d("Search", recyclerView.isVisible.toString())
                            trackList.addAll(trackAnswer!!)
                            recyclerView.adapter?.notifyDataSetChanged()
                           // Log.d("Search", trackList.toString())
                            clearButton.isVisible = true
                            clearButton.setOnClickListener {
                                searchline.setText("")
                                trackList.clear()
                                recyclerView.adapter?.notifyDataSetChanged()
                                val inputMethodManager =
                                    getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                                inputMethodManager?.hideSoftInputFromWindow(
                                    searchline.windowToken,
                                    0
                                )
                            }
                        } else {
                            trackList.clear()
                            recyclerView.adapter?.notifyDataSetChanged()
                            ImageNothing.isVisible = true
                            TextNothing.isVisible = true

                            clearButton.setOnClickListener {
                                searchline.setText("")
                                ImageNothing.isVisible = false
                                TextNothing.isVisible = false

                                val inputMethodManager =
                                    getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                                inputMethodManager?.hideSoftInputFromWindow(
                                    searchline.windowToken,
                                    0
                                )
                            }
                        }
                    } else {
                        trackList.clear()
                        recyclerView.adapter?.notifyDataSetChanged()
                        ImageNoInternet.isVisible = true
                        TextNoInternet.isVisible = true
                        TextNoInternet2.isVisible = true
                        buttonReturn.isVisible = true
                        ImageNothing.isVisible = false
                        TextNothing.isVisible = false

                        clearButton.setOnClickListener {
                            searchline.setText("")
                            ImageNoInternet.isVisible = false
                            TextNoInternet.isVisible = false
                            TextNoInternet2.isVisible = false
                            buttonReturn.isVisible = false
                            val inputMethodManager =
                                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                            inputMethodManager?.hideSoftInputFromWindow(searchline.windowToken, 0)
                        }
                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    trackList.clear()
                    recyclerView.adapter?.notifyDataSetChanged()
                    ImageNoInternet.isVisible = true
                    TextNoInternet.isVisible = true
                    TextNoInternet2.isVisible = true
                    buttonReturn.isVisible = true
                    ImageNothing.isVisible = false
                    TextNothing.isVisible = false
                    textHistory.isVisible = false
                    clearHistory.isVisible = false
                    historyRecyclerView.isVisible = false
                    clearButton.setOnClickListener {
                        searchline.setText("")
                        ImageNoInternet.isVisible = false
                        TextNoInternet.isVisible = false
                        TextNoInternet2.isVisible = false
                        buttonReturn.isVisible = false
                        val inputMethodManager =
                            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                        inputMethodManager?.hideSoftInputFromWindow(searchline.windowToken, 0)
                    }
                }


            })
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY, input)

    }

    companion object {
        const val KEY = "Value Edit Text"
        var trackList = ArrayList<Track>()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        input = savedInstanceState.getString("Value Edit Text", "")
        searchline.setText(input);
    }


}
