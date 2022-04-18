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
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import id.indosw.liquidswipe_lib.LiquidPager
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var text: TextView
    private  lateinit var logo: ImageView
    private  lateinit var fromBottom: Animation
    private lateinit var fromTop: Animation
    private lateinit var lottieAnim: LottieAnimationView
    private lateinit var bg: ImageView

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        bg = findViewById(R.id.bg_img)
        text = findViewById<TextView>(R.id.app_name)
        logo = findViewById<ImageView>(R.id.search_logo)
        lottieAnim = findViewById(R.id.lottie)

        auth = Firebase.auth
        val currentUser = auth.currentUser

        fromBottom = AnimationUtils.loadAnimation(this,R.anim.frombottom)
        fromTop = AnimationUtils.loadAnimation(this,R.anim.fromtop)


        text.startAnimation(fromBottom)
        logo.startAnimation(fromTop)

        if(currentUser == null){
            bg.animate().translationY(-1950F).setDuration(1000).startDelay = 5000
            text.animate().translationY(1400F).setDuration(1000).startDelay = 5000
            logo.animate().translationY(1650F).setDuration(1000).startDelay = 5000
            lottieAnim.animate().translationY(1400F).setDuration(1000).startDelay = 5000
            Handler().postDelayed({
                val intent = Intent(this, OnBoardingActivity::class.java)
                startActivity(intent)
                finish()
            }, 6110)
        } else if(!currentUser.isEmailVerified){
            bg.animate().translationY(-1950F).setDuration(1000).startDelay = 5000
            text.animate().translationY(1400F).setDuration(1000).startDelay = 5000
            logo.animate().translationY(1650F).setDuration(1000).startDelay = 5000
            lottieAnim.animate().translationY(1400F).setDuration(1000).startDelay = 5000
            Handler().postDelayed({
                val intent = Intent(this, OnBoardingActivity::class.java)
                startActivity(intent)
                finish()
            }, 6110)
        }

//        Handler().postDelayed({
//            val intent = Intent(this, LoginActivity::class.java)
//            val pair = listOf<Pair<View, String>>(
//                Pair<View, String>(logo, "logo_image"),
//                Pair<View, String>(text, "logo_text"),
//            )
//            val options: ActivityOptions = ActivityOptions.makeSceneTransitionAnimation(this,
//                pair[0],pair[1]
//            )
//            startActivity(intent,options.toBundle())
//            finish()
//        }, 5500)
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null && currentUser.isEmailVerified){

            val ref =  FirebaseDatabase.getInstance("https://myoutfit-6f5f1-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users").child(
                FirebaseAuth.getInstance().currentUser!!.uid)
            ref.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val passwordFromDB: String? =
                        dataSnapshot.child("password").getValue(String::class.java)
                    val nameFromDB: String? =
                        dataSnapshot.child("fullName").getValue(String::class.java)
                    val usernameFromDB: String? =
                        dataSnapshot.child("username").getValue(String::class.java)
                    val emailFromDB: String? =
                        dataSnapshot.child("email").getValue(String::class.java)

                    Handler().postDelayed({
                        val intent = Intent(applicationContext, UserProfileActivity::class.java)

                        intent.putExtra("name",nameFromDB)
                        intent.putExtra("username",usernameFromDB)
                        intent.putExtra("password",passwordFromDB)
                        intent.putExtra("email",emailFromDB)

                        startActivity(intent)
                    },5500)


                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        }
    }

}