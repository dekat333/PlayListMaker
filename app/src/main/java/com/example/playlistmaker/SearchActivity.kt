package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import android.widget.ProgressBar
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
    private lateinit var searchHistory: SearchHistory
    private lateinit var clearButton: ImageButton
    private lateinit var buttonBack: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var textHistory: TextView
    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var clearHistory: Button
    private lateinit var buttonReturn: Button
    private lateinit var ImageNothing: ImageView
    private lateinit var TextNothing: TextView
    private lateinit var ImageNoInternet: ImageView
    private lateinit var TextNoInternet: TextView
    private lateinit var TextNoInternet2: TextView
    private lateinit var progressBar:ProgressBar


    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val trackApiService = retrofit.create<ITunesAPI>()

    private val trackListener: (Track) -> Unit = { model ->
        searchHistory.write(model)
        listeners(model)
    }

    private val trackHistoryListener: (Track) -> Unit = { model ->
        listeners(model)
    }

    private fun listeners(model: Track) {
        if (clickDebounce()) {
            val intent = Intent(this, AudioPlayer::class.java)
            val track = Gson().toJson(model)
            intent.putExtra(AudioPlayer.TRACK_KEY, track)
            startActivity(intent)
        }
    }

    private fun historyFalse() {
        historyRecyclerView.isVisible = false
        textHistory.isVisible = false
        clearHistory.isVisible = false
    }

    private fun historyTrue() {
        historyRecyclerView.isVisible = true
        textHistory.isVisible = true
        clearHistory.isVisible = true
    }

    private fun noInternetFalse() {
        ImageNoInternet.isVisible = false
        TextNoInternet.isVisible = false
        TextNoInternet2.isVisible = false
    }

    private fun noInternetTrue() {
        ImageNoInternet.isVisible = true
        TextNoInternet.isVisible = true
        TextNoInternet2.isVisible = true
    }

    private fun nothingFalse(){
        ImageNothing.isVisible = false
        TextNothing.isVisible = false
    }

    private fun nothingTrue(){
        ImageNothing.isVisible = true
        TextNothing.isVisible = true
    }

    private fun clearSearchLine(){
        searchline.setText("")
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(searchline.windowToken, 0)
    }

    private fun searchLineText(){
        val sharedPref = getSharedPreferences(HISTORY_KEY, MODE_PRIVATE)
        if (searchline.text.isEmpty() && sharedPref.getString(
                HISTORY_KEY,
                null
            ) != null
        ) {
            historyTrue()
            historyRecyclerView.adapter =
                SearchAdapter(searchHistory.read(), trackHistoryListener)
        } else
            historyFalse()
    }


    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchline = findViewById(R.id.search_line)
        clearButton = findViewById(R.id.clear_button)
        buttonBack = findViewById(R.id.icon1)
        recyclerView = findViewById(R.id.searchList)
        textHistory = findViewById(R.id.TextHistory)
        historyRecyclerView = findViewById(R.id.historyList)
        clearHistory = findViewById(R.id.clearHistory)
        buttonReturn = findViewById(R.id.buttonReturn)


        recyclerView.isVisible = false
        buttonBack.setOnClickListener {
            finish()

        }


        val sharedPref = getSharedPreferences(HISTORY_KEY, MODE_PRIVATE)


        searchHistory = SearchHistory(sharedPref)
        val adapter = SearchAdapter(searchHistory.read(), trackHistoryListener)
        historyRecyclerView.adapter = adapter



        clearHistory.setOnClickListener {
            searchHistory.clear()
            adapter.notifyDataSetChanged()
            historyFalse()
            searchline.clearFocus()
        }



        searchline.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && searchline.text.isEmpty() && sharedPref.getString(HISTORY_KEY, null) != null) {
                historyTrue()
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
                historyFalse()

                clearButton.setOnClickListener {
                    clearSearchLine()
                }

                searchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }


        }

        searchline.addTextChangedListener(searchTextWatcher)


        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = SearchAdapter(trackList, trackListener)

        buttonReturn.setOnClickListener {
            searchTrack()

        }


    }



    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())

    private val searchRunnable = Runnable { searchTrack() }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)

    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun searchTrack() {
        val sharedPref = getSharedPreferences(HISTORY_KEY, MODE_PRIVATE)
        buttonReturn = findViewById(R.id.buttonReturn)
        recyclerView = findViewById(R.id.searchList)
        clearButton = findViewById(R.id.clear_button)
        ImageNothing = findViewById(R.id.placeholders)
        TextNothing = findViewById(R.id.TextHolder)
        ImageNoInternet = findViewById(R.id.no_internet)
        TextNoInternet = findViewById(R.id.TextHolderNoInternet)
        TextNoInternet2 = findViewById(R.id.TextHolderNoInternet2)
        textHistory = findViewById(R.id.TextHistory)
        historyRecyclerView = findViewById(R.id.historyList)
        clearHistory = findViewById(R.id.clearHistory)
        progressBar = findViewById(R.id.progressBar)

        if (searchline.text.isNotEmpty()) {
            noInternetFalse()
            nothingFalse()
            historyFalse()
            buttonReturn.isVisible = false
            recyclerView.isVisible = false
            progressBar.isVisible = true

            trackApiService.search(searchline.text.toString())
                .enqueue(object : Callback<TrackResponse> {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onResponse(
                        call: Call<TrackResponse>,
                        response: Response<TrackResponse>
                    ) {
                        progressBar.isVisible = false
                        noInternetFalse()
                        nothingFalse()
                        historyFalse()
                        buttonReturn.isVisible = false
                        recyclerView.isVisible = false
                        if (response.isSuccessful) {
                            Log.d("Search", response.body()?.results.toString())
                            trackList.clear()
                            val trackAnswer = response.body()?.results

                            if (trackAnswer?.isNotEmpty() == true) {
                                recyclerView.isVisible = true
                                trackList.addAll(trackAnswer)
                                recyclerView.adapter?.notifyDataSetChanged()
                                clearButton.isVisible = true
                                clearButton.setOnClickListener {
                                    clearSearchLine()
                                    trackList.clear()
                                    recyclerView.adapter?.notifyDataSetChanged()
                                    searchLineText()
                                }
                            } else {
                                trackList.clear()
                                recyclerView.adapter?.notifyDataSetChanged()
                                nothingTrue()

                                clearButton.setOnClickListener {
                                    clearSearchLine()
                                    nothingFalse()
                                    searchLineText()
                                }
                            }
                        } else {
                            trackList.clear()
                            recyclerView.adapter?.notifyDataSetChanged()
                            buttonReturn.isVisible = true
                            noInternetTrue()
                            nothingFalse()

                            clearButton.setOnClickListener {
                                clearSearchLine()
                                buttonReturn.isVisible = false
                                noInternetFalse()
                                searchLineText()
                            }
                        }
                    }


                    @SuppressLint("NotifyDataSetChanged")
                    override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                        progressBar.isVisible = false
                        trackList.clear()
                        recyclerView.adapter?.notifyDataSetChanged()
                        buttonReturn.isVisible = true
                        noInternetTrue()
                        nothingFalse()
                        historyFalse()
                        clearButton.setOnClickListener {
                            clearSearchLine()
                            buttonReturn.isVisible = false
                            noInternetFalse()
                            searchLineText()
                        }
                    }


                })
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY, input)

    }

    companion object {
        const val KEY = "Value Edit Text"
        var trackList = ArrayList<Track>()
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        input = savedInstanceState.getString("Value Edit Text", "")
        searchline.setText(input);
    }


}
