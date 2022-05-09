package com.toropov.my_outfit.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.toropov.my_outfit.R

class AllCategoriesActivity : AppCompatActivity() {

    private lateinit var backBtn: ImageView
    private lateinit var jeansBtn: Button
    private lateinit var hoodiesBtn: Button
    private lateinit var pantsBtn: Button
    private lateinit var blazersBtn: Button
    private lateinit var tshirtsBtn: Button
    private lateinit var dressesBtn: Button
    private lateinit var jacketsBtn: Button
    private lateinit var shoesBtn: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_categories)

        //UserData
        val intent: Intent = intent
        val user_username: String? = intent.getStringExtra("username")
        val user_fullName: String? = intent.getStringExtra("name")
        val user_email: String? = intent.getStringExtra("email")
        val user_password: String? = intent.getStringExtra("password")

        val intent1 = Intent(this, ItemsActivity::class.java)
        intent1.putExtra("name",user_fullName)
        intent1.putExtra("username",user_username)
        intent1.putExtra("password",user_password)
        intent1.putExtra("email",user_email)

        backBtn = findViewById(R.id.back_pressed)
        jeansBtn = findViewById(R.id.expand_jeans)
        hoodiesBtn = findViewById(R.id.expand_hoodies)
        pantsBtn = findViewById(R.id.expand_pants)
        blazersBtn = findViewById(R.id.expand_blazers)
        tshirtsBtn = findViewById(R.id.expand_tshirts)
        dressesBtn = findViewById(R.id.expand_dresses)
        jacketsBtn = findViewById(R.id.expand_jackets)
        shoesBtn = findViewById(R.id.expand_shoes)

        backBtn.setOnClickListener {
            super.onBackPressed()
        }

        jeansBtn.setOnClickListener {
            intent1.putExtra("appName","Jeans")
            startActivity(intent1)
        }

        hoodiesBtn.setOnClickListener {
            intent1.putExtra("appName","Hoodies")
            startActivity(intent1)
        }

        pantsBtn.setOnClickListener {
            intent1.putExtra("appName","Pants")
            startActivity(intent1)
        }

        blazersBtn.setOnClickListener {
            intent1.putExtra("appName","Blazers")
            startActivity(intent1)
        }

        tshirtsBtn.setOnClickListener {
            intent1.putExtra("appName","T-Shirts")
            startActivity(intent1)
        }

        dressesBtn.setOnClickListener {
            intent1.putExtra("appName","Dresses")
            startActivity(intent1)
        }

        jacketsBtn.setOnClickListener {
            intent1.putExtra("appName","Jackets")
            startActivity(intent1)
        }

        shoesBtn.setOnClickListener {
            intent1.putExtra("appName","Shoes")
            startActivity(intent1)
        }

    }
}