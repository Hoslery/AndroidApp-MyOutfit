package com.toropov.my_outfit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import id.indosw.liquidswipe_lib.LiquidPager

class OnBoardingActivity : AppCompatActivity() {

    private lateinit var pager: LiquidPager
    private lateinit var adapter: ViewPagerAdapter
    private lateinit var o_b_anim: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)

        o_b_anim = AnimationUtils.loadAnimation(this,R.anim.o_b_anim)

        pager = findViewById(R.id.pager)

        adapter = ViewPagerAdapter(supportFragmentManager)
        pager.adapter = adapter

        pager.startAnimation(o_b_anim)

    }
}