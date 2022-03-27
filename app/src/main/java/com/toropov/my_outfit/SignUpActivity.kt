package com.toropov.my_outfit

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Pair
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout

class SignUpActivity : AppCompatActivity() {

    private lateinit var text: TextView
    private lateinit var logo: ImageView
    private lateinit var username: TextInputLayout
    private lateinit var password: TextInputLayout
    private lateinit var callLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        text = findViewById<TextView>(R.id.welcome_text)
        logo = findViewById<ImageView>(R.id.search_logo)
        username = findViewById<TextInputLayout>(R.id.username)
        password = findViewById<TextInputLayout>(R.id.password)
        callLogin = findViewById(R.id.have_acc)

        callLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
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