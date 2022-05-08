package com.toropov.my_outfit.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.toropov.my_outfit.R

class AllCategoriesActivity : AppCompatActivity() {

    private lateinit var backBtn: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_categories)

        backBtn = findViewById(R.id.back_pressed)

        backBtn.setOnClickListener {
            super.onBackPressed()
        }
    }
}