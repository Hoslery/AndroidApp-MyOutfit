package com.toropov.my_outfit

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Pair
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_login)
        val text: TextView = findViewById<TextView>(R.id.welcome_text)
        val logo: ImageView = findViewById<ImageView>(R.id.search_logo)
        val username: TextInputLayout= findViewById<TextInputLayout>(R.id.username)
        val password: TextInputLayout= findViewById<TextInputLayout>(R.id.password)
        val callSignUp: Button = findViewById(R.id.signup_screen)

        callSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            val pair = listOf<Pair<View, String>>(
                Pair<View, String>(logo, "logo_image"),
                Pair<View, String>(text, "logo_text"),
                Pair<View, String>(username, "username"),
                Pair<View, String>(password, "password"),
            )
            val options: ActivityOptions = ActivityOptions.makeSceneTransitionAnimation(this,
                pair[0],pair[1],pair[2],pair[3]
            )
            startActivity(intent,options.toBundle())
        }
    }
}