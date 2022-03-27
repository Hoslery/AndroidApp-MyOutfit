package com.toropov.my_outfit

import android.annotation.SuppressLint
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

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var text: TextView
    private  lateinit var logo: ImageView
    private  lateinit var fromBottom: Animation
    private lateinit var fromTop: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        text = findViewById<TextView>(R.id.app_name)
        logo = findViewById<ImageView>(R.id.search_logo)
        fromBottom = AnimationUtils.loadAnimation(this,R.anim.frombottom)
        fromTop = AnimationUtils.loadAnimation(this,R.anim.fromtop)
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