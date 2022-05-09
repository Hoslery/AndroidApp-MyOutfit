package com.toropov.my_outfit.activities

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.SubMenu
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.toropov.my_outfit.R
import com.toropov.my_outfit.adapters.CategoriesAdapter
import com.toropov.my_outfit.adapters.FeaturedAdapter
import com.toropov.my_outfit.helperClasses.CategoriesHelperClass
import com.toropov.my_outfit.helperClasses.FeaturedHelperClass


class DashBoardActivity : AppCompatActivity(), FeaturedAdapter.OnItemClickListener, CategoriesAdapter.OnItemClickListener,
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var featuredRecycler: RecyclerView
    private lateinit var categoriesRecycler: RecyclerView
    private lateinit var viewAll: Button
    private lateinit var userProfile: ImageView
    private lateinit var tshirts: LinearLayout
    private lateinit var dresses: LinearLayout
    private lateinit var jackets: LinearLayout
    private lateinit var shoes: LinearLayout
    private lateinit var contentView: LinearLayout


    //Drawer Menu
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var menu: ImageView
    private val END_SCALE: Float = 0.7f


    override fun onCreate(savedInstanceState: Bundle?) {

        //UserData
        val intent: Intent = intent
        val user_username: String? = intent.getStringExtra("username")
        val user_fullName: String? = intent.getStringExtra("name")
        val user_email: String? = intent.getStringExtra("email")
        val user_password: String? = intent.getStringExtra("password")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)

        userProfile = findViewById(R.id.profile)
        viewAll = findViewById(R.id.view_all)

        featuredRecycler = findViewById(R.id.featured_recycler)
        categoriesRecycler = findViewById(R.id.categories_recycler)

        tshirts = findViewById(R.id.tshirts)
        dresses = findViewById(R.id.dresses)
        jackets = findViewById(R.id.jackets)
        shoes = findViewById(R.id.shoes)

        contentView = findViewById(R.id.content)

        //Menu
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)
        menu = findViewById(R.id.menu)

        navigationDrawer()

        featuredRecycler()

        categoriesRecycler()

        userProfile.setOnClickListener {

            val intent1 = Intent(applicationContext, UserProfileActivity::class.java)

            intent1.putExtra("name",user_fullName)
            intent1.putExtra("username",user_username)
            intent1.putExtra("password",user_password)
            intent1.putExtra("email",user_email)
            startActivity(intent1)
        }

        tshirts.setOnClickListener {
            val intent1 = Intent(this, ItemsActivity::class.java)
            intent1.putExtra("appName","T-Shirts")
            intent1.putExtra("name",user_fullName)
            intent1.putExtra("username",user_username)
            intent1.putExtra("password",user_password)
            intent1.putExtra("email",user_email)
            startActivity(intent1)
        }

        dresses.setOnClickListener {
            val intent1 = Intent(this, ItemsActivity::class.java)
            intent1.putExtra("appName","Dresses")
            intent1.putExtra("name",user_fullName)
            intent1.putExtra("username",user_username)
            intent1.putExtra("password",user_password)
            intent1.putExtra("email",user_email)
            startActivity(intent1)
        }

        jackets.setOnClickListener {
            val intent1 = Intent(this, ItemsActivity::class.java)
            intent1.putExtra("appName","Jackets")
            intent1.putExtra("name",user_fullName)
            intent1.putExtra("username",user_username)
            intent1.putExtra("password",user_password)
            intent1.putExtra("email",user_email)
            startActivity(intent1)
        }

        shoes.setOnClickListener {
            val intent1 = Intent(this, ItemsActivity::class.java)
            intent1.putExtra("appName","Shoes")
            intent1.putExtra("name",user_fullName)
            intent1.putExtra("username",user_username)
            intent1.putExtra("password",user_password)
            intent1.putExtra("email",user_email)
            startActivity(intent1)
        }

        viewAll.setOnClickListener {
            val intent1 = Intent(this, AllCategoriesActivity::class.java)
            intent1.putExtra("name",user_fullName)
            intent1.putExtra("username",user_username)
            intent1.putExtra("password",user_password)
            intent1.putExtra("email",user_email)
            startActivity(intent1)
        }


    }

    //Navigation Drawer Functions
    private fun navigationDrawer() {

        //Navigation Drawer
        navigationView.bringToFront()
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.setCheckedItem(R.id.nav_home)

        menu.setOnClickListener {
            if(drawerLayout.isDrawerVisible(GravityCompat.START)){
                drawerLayout.closeDrawer(GravityCompat.START)
            } else{
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        animateNavigationDrawer()
    }
    private fun animateNavigationDrawer() {

        drawerLayout.setScrimColor(resources.getColor(R.color.white))
        drawerLayout.addDrawerListener(object: DrawerLayout.SimpleDrawerListener(){
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                val diffScaledOffset: Float = slideOffset*(1 - END_SCALE)
                val offsetScale = 1 - diffScaledOffset
                contentView.scaleX = offsetScale
                contentView.scaleY = offsetScale

                val xOffset: Float = drawerView.width*slideOffset
                val xOffsetDiff: Float = contentView.width *diffScaledOffset/2
                val xTranslation: Float = xOffset - xOffsetDiff
                contentView.translationX = xTranslation
            }

        })

    }
    override fun onBackPressed() {

        if(drawerLayout.isDrawerVisible(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        //UserData
        val intent: Intent = intent
        val user_username: String? = intent.getStringExtra("username")
        val user_fullName: String? = intent.getStringExtra("name")
        val user_email: String? = intent.getStringExtra("email")
        val user_password: String? = intent.getStringExtra("password")

        val intent2 = Intent(this, ItemsActivity::class.java)
        intent2.putExtra("name",user_fullName)
        intent2.putExtra("username",user_username)
        intent2.putExtra("password",user_password)
        intent2.putExtra("email",user_email)

        when (item.itemId) {
            R.id.nav_home -> {

            }
            R.id.nav_profile -> {

                val intent1 = Intent(applicationContext, UserProfileActivity::class.java)

                intent1.putExtra("name",user_fullName)
                intent1.putExtra("username",user_username)
                intent1.putExtra("password",user_password)
                intent1.putExtra("email",user_email)
                startActivity(intent1)
            }
            R.id.nav_tshirts -> {
                intent2.putExtra("appName","T-Shirts")
                startActivity(intent2)
            }
            R.id.nav_dresses -> {
                intent2.putExtra("appName","Dresses")
                startActivity(intent2)
            }
            R.id.nav_jackets-> {
                intent2.putExtra("appName","Jackets")
                startActivity(intent2)
            }
            R.id.nav_shoes -> {
                intent2.putExtra("appName","Shoes")
                startActivity(intent2)
            }
            R.id.nav_jeans -> {
                intent2.putExtra("appName","Jeans")
                startActivity(intent2)
            }
            R.id.nav_hoodies -> {
                intent2.putExtra("appName","Hoodies")
                startActivity(intent2)
            }
            R.id.nav_pants -> {
                intent2.putExtra("appName","Pants")
                startActivity(intent2)
            }
            R.id.nav_blazers -> {
                intent2.putExtra("appName","Blazers")
                startActivity(intent2)
            }
            R.id.nav_favourites -> {
                startActivity(Intent(this,FavouritesActivity::class.java))
            }
            R.id.nav_about -> {

            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onItemClicked(featuredHelperClass: FeaturedHelperClass) {
        Log.i("Store",featuredHelperClass.title)


        when(featuredHelperClass.title){
            "HM"->{
                val address: Uri = Uri.parse("https://www2.hm.com/en_gb/")
                val openLinkIntent = Intent(Intent.ACTION_VIEW, address)
                startActivity(openLinkIntent)
            }
            "WB"->{
                val address: Uri = Uri.parse("https://www.wildberries.ru/")
                val openLinkIntent = Intent(Intent.ACTION_VIEW, address)
                startActivity(openLinkIntent)
            }
            "Zara"->{
                val address: Uri = Uri.parse("https://www.zara.com/ru/")
                val openLinkIntent = Intent(Intent.ACTION_VIEW, address)
                startActivity(openLinkIntent)
            }
        }
    }

    override fun onItemCategoriesClicked(categoriesHelperClass: CategoriesHelperClass) {
        Log.i("Category",categoriesHelperClass.title)

        //UserData
        val intent: Intent = intent
        val user_username: String? = intent.getStringExtra("username")
        val user_fullName: String? = intent.getStringExtra("name")
        val user_email: String? = intent.getStringExtra("email")
        val user_password: String? = intent.getStringExtra("password")

        val intent1 = Intent(this, ItemsActivity::class.java)
        intent1.putExtra("appName", categoriesHelperClass.title)
        intent1.putExtra("name",user_fullName)
        intent1.putExtra("username",user_username)
        intent1.putExtra("password",user_password)
        intent1.putExtra("email",user_email)
        startActivity(intent1)
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

        val adapter = CategoriesAdapter(categories,this)
        categoriesRecycler.adapter = adapter
    }

    private fun featuredRecycler() {

        featuredRecycler.setHasFixedSize(true)
        featuredRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val featuredStores: MutableList<FeaturedHelperClass> = mutableListOf()

        featuredStores.add(FeaturedHelperClass(R.drawable.hm_logo, "HM", "Fashionable clothes at affordable prices"))
        featuredStores.add(FeaturedHelperClass(R.drawable.zara_logo, "Zara", "Youth and audacity"))
        featuredStores.add(FeaturedHelperClass(R.drawable.wildberries_logo, "WB", "The rhythm of modern life"))

        val adapter = FeaturedAdapter(featuredStores,this)
        featuredRecycler.adapter = adapter


    }

}