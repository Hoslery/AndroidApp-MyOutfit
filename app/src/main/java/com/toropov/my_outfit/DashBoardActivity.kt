package com.toropov.my_outfit

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class DashBoardActivity : AppCompatActivity() {

    private lateinit var featuredRecycler: RecyclerView
    private lateinit var categoriesRecycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)

        featuredRecycler = findViewById(R.id.featured_recycler)
        categoriesRecycler = findViewById(R.id.categories_recycler)

        featuredRecycler()

        categoriesRecycler()
    }

    private fun categoriesRecycler() {
        categoriesRecycler.setHasFixedSize(true)
        categoriesRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val categories: MutableList<CategoriesHelperClass> = mutableListOf()

        val gradient1 = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(-0x852331, -0x852331)
        )
        val gradient2 = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(-0x2b341b, -0x2b341b)
        )
       val gradient3 = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(-0x83a61, -0x83a61)
        )
        val gradient4 = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(-0x47280b, -0x47280b)
        )

        categories.add(CategoriesHelperClass(R.drawable.t_shirt, "T-Shirts",gradient2))
        categories.add(CategoriesHelperClass(R.drawable.dress, "Dresses",gradient1))
        categories.add(CategoriesHelperClass(R.drawable.jacket, "Jackets",gradient3))
        categories.add(CategoriesHelperClass(R.drawable.shoes, "Shoes",gradient4))

        val adapter = CategoriesAdapter(categories)
        categoriesRecycler.adapter = adapter
    }

    private fun featuredRecycler() {

        featuredRecycler.setHasFixedSize(true)
        featuredRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val featuredStores: MutableList<FeaturedHelperClass> = mutableListOf()

        featuredStores.add(FeaturedHelperClass(R.drawable.hm_logo, "HM", "Fashionable clothes at affordable prices"))
        featuredStores.add(FeaturedHelperClass(R.drawable.zara_logo, "Zara", "Youth and audacity"))
        featuredStores.add(FeaturedHelperClass(R.drawable.bershka_logo, "Bershka", "Youth and audacity"))

        val adapter = FeaturedAdapter(featuredStores)
        featuredRecycler.adapter = adapter


    }
}