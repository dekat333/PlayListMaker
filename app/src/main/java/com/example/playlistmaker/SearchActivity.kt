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

class SearchActivity : AppCompatActivity() {
    var input = ""
    lateinit var searchline: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        searchline = findViewById<EditText>(R.id.search_line)
        val clear_button = findViewById<ImageButton>(R.id.clear_button)
        val button_back = findViewById<ImageButton>(R.id.icon1)


        button_back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    clear_button.visibility = View.GONE
                }else{
                    input = searchline.text.toString()
                    searchline.requestFocus()
                    clear_button.visibility = View.VISIBLE
                    clear_button.setOnClickListener {
                        searchline.setText("")
                        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                        inputMethodManager?.hideSoftInputFromWindow(searchline.windowToken, 0)
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }


        }

        //добавляем созданный simpleTextWatcher к EditText
        searchline.addTextChangedListener(searchTextWatcher)

    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("Value Edit Text", input)

    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Вторым параметром мы передаём значение по умолчанию
        //val searchline = findViewById<EditText>(R.id.search_line)
        input = savedInstanceState.getString("Value Edit Text", "")
        searchline.setText(input);
    }

}