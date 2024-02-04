package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity() {
    private var input = ""
    private lateinit var searchline: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        searchline = findViewById<EditText>(R.id.search_line)
        val clearButton = findViewById<ImageButton>(R.id.clear_button)
        val buttonBack = findViewById<ImageButton>(R.id.icon1)


        buttonBack.setOnClickListener {
            finish()

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

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY, input)

    }

    companion object{
        const val KEY = "Value Edit Text"
        var trackList: MutableList<Track> = mutableListOf(
            Track(
                1, "Smells Like Teen Spirit",
                "Nirvana", "5:01",
                "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"
            ),
            Track(
                2, "Billie Jean",
                "Michael Jackson", "4:35",
                "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"
            ),
            Track(
                3,
                "Stayin' Alive",
                "Bee Gees",
                "4:10",
                "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"
            ),
            Track(
                4,
                "Whole Lotta Love",
                "Led Zeppelin",
                "5:33",
                "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"
            ),
            Track(
                5,
                "Sweet Child O'Mine",
                "Guns N' Roses",
                "5:03",
                "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg"
            )
        )
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Вторым параметром мы передаём значение по умолчанию
        //val searchline = findViewById<EditText>(R.id.search_line)
        input = savedInstanceState.getString("Value Edit Text", "")
        searchline.setText(input);
    }

}