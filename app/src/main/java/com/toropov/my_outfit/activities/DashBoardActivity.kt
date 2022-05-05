package com.toropov.my_outfit.activities

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.toropov.my_outfit.R
import com.toropov.my_outfit.adapters.CategoriesAdapter
import com.toropov.my_outfit.adapters.FeaturedAdapter
import com.toropov.my_outfit.helperClasses.CategoriesHelperClass
import com.toropov.my_outfit.helperClasses.FeaturedHelperClass


class DashBoardActivity : AppCompatActivity(), FeaturedAdapter.OnItemClickListener, CategoriesAdapter.OnItemClickListener {

    private lateinit var featuredRecycler: RecyclerView
    private lateinit var categoriesRecycler: RecyclerView
    private lateinit var userProfile: ImageView
    private lateinit var tshirts: LinearLayout
    private lateinit var dresses: LinearLayout
    private lateinit var jackets: LinearLayout
    private lateinit var shoes: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)

        userProfile = findViewById(R.id.profile)

        featuredRecycler = findViewById(R.id.featured_recycler)
        categoriesRecycler = findViewById(R.id.categories_recycler)

        tshirts = findViewById(R.id.tshirts)
        dresses = findViewById(R.id.dresses)
        jackets = findViewById(R.id.jackets)
        shoes = findViewById(R.id.shoes)

        featuredRecycler()

        categoriesRecycler()

        userProfile.setOnClickListener {
            val intent: Intent = intent
            val user_username: String? = intent.getStringExtra("username")
            val user_fullName: String? = intent.getStringExtra("name")
            val user_email: String? = intent.getStringExtra("email")
            val user_password: String? = intent.getStringExtra("password")

            val intent1 = Intent(applicationContext, UserProfileActivity::class.java)

            intent1.putExtra("name",user_fullName)
            intent1.putExtra("username",user_username)
            intent1.putExtra("password",user_password)
            intent1.putExtra("email",user_email)
            startActivity(intent1)
        }

        tshirts.setOnClickListener {
            val intent = Intent(this, ItemsActivity::class.java)
            intent.putExtra("appName","T-Shirts")
            startActivity(intent)
        }

        dresses.setOnClickListener {
            val intent = Intent(this, ItemsActivity::class.java)
            intent.putExtra("appName","Dresses")
            startActivity(intent)
        }

        jackets.setOnClickListener {
            val intent = Intent(this, ItemsActivity::class.java)
            intent.putExtra("appName","Jackets")
            startActivity(intent)
        }

        shoes.setOnClickListener {
            val intent = Intent(this, ItemsActivity::class.java)
            intent.putExtra("appName","Shoes")
            startActivity(intent)
        }

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

        when(categoriesHelperClass.title){
            "Shoes"->{
                val intent = Intent(this, ItemsActivity::class.java)
                intent.putExtra("appName","Shoes")
                startActivity(intent)
            }
            "T-Shirts"->{
                val intent = Intent(this, ItemsActivity::class.java)
                intent.putExtra("appName","T-Shirts")
                startActivity(intent)
            }
            "Dresses"->{
                val intent = Intent(this, ItemsActivity::class.java)
                intent.putExtra("appName","Dresses")
                startActivity(intent)
            }
            "Jackets"->{
                val intent = Intent(this, ItemsActivity::class.java)
                intent.putExtra("appName","Jackets")
                startActivity(intent)
            }
        }
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