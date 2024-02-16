package com.example.playlistmaker

import android.content.Context
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
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {
    private var input = ""
    private lateinit var searchline: EditText

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val trackApiService = retrofit.create<ITunesAPI>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        searchline = findViewById<EditText>(R.id.search_line)
        val clearButton = findViewById<ImageButton>(R.id.clear_button)
        val buttonBack = findViewById<ImageButton>(R.id.icon1)


        buttonBack.setOnClickListener {
            finish()

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

        val recyclerView = findViewById<RecyclerView>(R.id.searchList)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = SearchAdapter(trackList)

        val buttonReturn = findViewById<Button>(R.id.buttonReturn)
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
        trackApiService.search(searchline.text.toString())
            .enqueue(object : Callback<TrackResponse> {
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
                    if (response.isSuccessful) {
                        Log.d("Search", response.body()?.results.toString())
                        trackList.clear()
                        val trackAnswer = response.body()?.results

                        if (trackAnswer?.isNotEmpty() == true) {
                            trackList.addAll(trackAnswer!!)
                            recyclerView.adapter?.notifyDataSetChanged()
                            clearButton.isVisible = true
                            clearButton.setOnClickListener {
                                searchline.setText("")
                                trackList.clear()
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


