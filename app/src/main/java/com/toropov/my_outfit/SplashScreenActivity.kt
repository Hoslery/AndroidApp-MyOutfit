package com.toropov.my_outfit

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Pair
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView

class SplashScreenActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val text: TextView = findViewById<TextView>(R.id.app_name)
        val logo: ImageView = findViewById<ImageView>(R.id.search_logo)
        val fromBottom: Animation = AnimationUtils.loadAnimation(this,R.anim.frombottom)
        val fromTop: Animation = AnimationUtils.loadAnimation(this,R.anim.fromtop)
        text.startAnimation(fromBottom)
        logo.startAnimation(fromTop)
        Handler().postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            val pair = listOf<Pair<View, String>>(
                Pair<View, String>(logo, "logo_image"),
                Pair<View, String>(text, "logo_text"),
            )
            val options: ActivityOptions = ActivityOptions.makeSceneTransitionAnimation(this,
                pair[0],pair[1]
            )
            startActivity(intent,options.toBundle())
            finish()
        }, 3000)
    }
}