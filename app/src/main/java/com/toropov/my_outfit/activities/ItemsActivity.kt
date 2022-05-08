package com.toropov.my_outfit.activities

import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.toropov.my_outfit.R
import com.toropov.my_outfit.adapters.ItemAdapter
import com.toropov.my_outfit.helperClasses.ItemHelperClass
import org.jetbrains.annotations.NotNull
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.util.*
import javax.security.auth.callback.Callback


class ItemsActivity : AppCompatActivity(), ItemAdapter.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appName: TextView
    private lateinit var userProfile: ImageView
    private lateinit var doc: Document
    private lateinit var hmThread: Thread
    private lateinit var zaraThread: Thread
    private lateinit var wildBerriesThread: Thread
    private lateinit var runnable: Runnable
    private lateinit var runnable1: Runnable
    private lateinit var runnable2: Runnable
    private lateinit var itemsRecycler: RecyclerView
    private val products: MutableList<ItemHelperClass> = mutableListOf()
    private val adapter = ItemAdapter(products,this)

    //Drawer Menu
    private lateinit var contentView: LinearLayout
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var menu: ImageView
    private val END_SCALE: Float = 0.7f

    //DataBase
    private lateinit var database: FirebaseDatabase
    private lateinit var favRef: DatabaseReference
    private lateinit var favListRef: DatabaseReference
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items)

        userProfile = findViewById(R.id.profile)

        auth = Firebase.auth
        val currentUser = auth.currentUser
        database = FirebaseDatabase.getInstance("https://myoutfit-6f5f1-default-rtdb.europe-west1.firebasedatabase.app/")
        favRef = database.getReference("favourites")
        favListRef = database.getReference("favouriteList").child(currentUser!!.uid)

        val intent: Intent = intent
        val appNameIntent: String? = intent.getStringExtra("appName")
        val user_username: String? = intent.getStringExtra("username")
        val user_fullName: String? = intent.getStringExtra("name")
        val user_email: String? = intent.getStringExtra("email")
        val user_password: String? = intent.getStringExtra("password")
        appName = findViewById(R.id.app_name)
        appName.text = appNameIntent

        itemsRecycler = findViewById(R.id.items_rv)
        itemsRecycler.setHasFixedSize(false)
        itemsRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        contentView = findViewById(R.id.content)

        //Menu
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)
        menu = findViewById(R.id.menu)

        navigationDrawer()

        userProfile.setOnClickListener {

            val intent1 = Intent(applicationContext, UserProfileActivity::class.java)

            intent1.putExtra("name",user_fullName)
            intent1.putExtra("username",user_username)
            intent1.putExtra("password",user_password)
            intent1.putExtra("email",user_email)
            startActivity(intent1)
        }

        init()

        itemsRecycler.adapter = adapter

    }

    //Navigation Drawer Functions
    private fun navigationDrawer() {
        //Navigation Drawer
        navigationView.bringToFront()
        navigationView.setNavigationItemSelectedListener(this)

        when(appName.text){
            "T-Shirts" -> {
                navigationView.setCheckedItem(R.id.nav_tshirts)
            }
            "Dresses" -> {
                navigationView.setCheckedItem(R.id.nav_dresses)
            }
            "Jackets" -> {
                navigationView.setCheckedItem(R.id.nav_jackets)
            }
            "Shoes" ->{
                navigationView.setCheckedItem(R.id.nav_shoes)
            }
        }

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

        when (item.itemId) {
            R.id.nav_home -> {
                super.onBackPressed()
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
                val intent1 = Intent(this, ItemsActivity::class.java)
                intent1.putExtra("appName","T-Shirts")
                intent1.putExtra("name",user_fullName)
                intent1.putExtra("username",user_username)
                intent1.putExtra("password",user_password)
                intent1.putExtra("email",user_email)
                startActivity(intent1)
            }
            R.id.nav_dresses -> {
                val intent1 = Intent(this, ItemsActivity::class.java)
                intent1.putExtra("appName","Dresses")
                intent1.putExtra("name",user_fullName)
                intent1.putExtra("username",user_username)
                intent1.putExtra("password",user_password)
                intent1.putExtra("email",user_email)
                startActivity(intent1)
            }
            R.id.nav_jackets-> {
                val intent1 = Intent(this, ItemsActivity::class.java)
                intent1.putExtra("appName","Jackets")
                intent1.putExtra("name",user_fullName)
                intent1.putExtra("username",user_username)
                intent1.putExtra("password",user_password)
                intent1.putExtra("email",user_email)
                startActivity(intent1)
            }
            R.id.nav_shoes -> {
                val intent1 = Intent(this, ItemsActivity::class.java)
                intent1.putExtra("appName","Shoes")
                intent1.putExtra("name",user_fullName)
                intent1.putExtra("username",user_username)
                intent1.putExtra("password",user_password)
                intent1.putExtra("email",user_email)
                startActivity(intent1)
            }
            R.id.nav_favorites -> {

            }
            R.id.nav_about -> {

            }
            R.id.nav_rate -> {

            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun init(){
        runnable = Runnable {
            getWebHM()
        }

        runnable1 = Runnable {
            getWebZara()
        }

        runnable2 = Runnable {
            getWebWildBerries()
        }

        hmThread = Thread(runnable)
        zaraThread = Thread(runnable1)
        wildBerriesThread = Thread(runnable2)
        hmThread.start()
        zaraThread.start()
        wildBerriesThread.start()
        hmThread.join()
        zaraThread.join()
        wildBerriesThread.join()
    }




    override fun onItemClicked(itemHelperClass: ItemHelperClass) {
        val address: Uri = Uri.parse(itemHelperClass.itemHref)
        val openLinkIntent = Intent(Intent.ACTION_VIEW, address)
        startActivity(openLinkIntent)
    }

    override fun OnFavBtnClicked(itemHelperClass: ItemHelperClass) {

        if(itemHelperClass.check){
            favListRef.child(itemHelperClass.postKey).setValue(itemHelperClass)
        }else{
            favListRef.child(itemHelperClass.postKey).removeValue()
        }
    }

    private fun getWebHM(){
        var url: String = ""
        when(appName.text){
            "Shoes"->{
                url = "https://www2.hm.com/en_gb/men/shop-by-product/shoes.html"
            }
            "T-Shirts"->{
                url = "https://www2.hm.com/en_gb/men/shop-by-product/t-shirts-and-tanks.html"
            }
            "Dresses"->{
                url = "https://www2.hm.com/en_gb/ladies/shop-by-product/dresses.html"
            }
            "Jackets"->{
                url = "https://www2.hm.com/en_gb/men/shop-by-product/jackets-and-coats.html"
            }
        }
        doc = Jsoup.connect(url).get()
        val productLists : Elements = doc.getElementsByClass("products-listing small")
        val productList: Element = productLists[0]
        val items : Elements = productList.children()
        for(item in items){
            val imageContainer: Element = item.getElementsByClass("image-container")[0]
            val itemDetails: Element = item.getElementsByClass("item-details")[0]
            val itemName: String = itemDetails.getElementsByClass("item-heading")[0].text()
            val itemPrice: String = itemDetails.getElementsByClass("item-price")[0].text()
            val hrefEnd: String = imageContainer.children()[0].attr("href")
            val href = "https://www2.hm.com$hrefEnd"
            val imgUrlStr: String = imageContainer.select("img")[0].attr("abs:data-src")
            readData(object: FirebaseCallback{
                override fun onCallback(check: Boolean, key: String?) {
                    Log.d("CHeeeeeeck","$check, $key")
                    products.add(ItemHelperClass(key!!,check,imgUrlStr,"H&M",itemName,itemPrice,href))
                }

            }, imgUrlStr, href)
            Log.d("MyLog","H&M: $itemName , $itemPrice , $href , $imgUrlStr")
        }
//        runOnUiThread(Runnable() {
//            itemsRecycler.adapter = adapter
//        })

    }

    private fun getWebZara() {
        var url: String = ""
        when(appName.text){
            "Shoes"->{
                url = "https://www.zara.com/us/en/man-shoes-l769.html?v1=2032982"
            }
            "T-Shirts"->{
                url = "https://www.zara.com/us/en/man-tshirts-l855.html?v1=2032069"
            }
            "Dresses"->{
                url = "https://www.zara.com/us/en/woman-dresses-l1066.html?v1=2026630"
            }
            "Jackets"->{
                url = "https://www.zara.com/us/en/man-jackets-l640.html?v1=2056396"
            }
        }
        doc = Jsoup.connect(url).userAgent("Mozilla")
            .header("Accept", "text/html")
            .header("Accept-Encoding", "gzip,deflate")
            .header("Accept-Language", "it-IT,en;q=0.8,en-US;q=0.6,de;q=0.4,it;q=0.2,es;q=0.2")
            .header("Connection", "keep-alive")
            .ignoreContentType(true)
            .get()
        if(appName.text.equals("Jackets")){
            val productLists : Elements = doc.getElementsByClass("product-grid")
            val items: Elements = productLists[1].getElementsByClass("product-grid__product-list")[0].
            getElementsByAttributeValueContaining("class","product-grid-product _product product-grid-product--zoom1-columns")
            for(item in items){
                if(item.getElementsByAttributeValueContaining("class","media-regions").size!=0)
                    continue
                val itemContainer1: Element = item.select("a")[1]
                val itemName : String = itemContainer1.select("span")[0].text()
                val href : String = itemContainer1.attr("href")
                val itemPrice: String = item.getElementsByClass("price-current__amount")[0].text()
                val imgUrlStr: String = item.getElementsByClass("media-image__image")[0].attr("abs:src")
                if(imgUrlStr.contains("png"))
                    continue
                readData(object: FirebaseCallback{
                    override fun onCallback(check: Boolean, key: String?) {
                        Log.d("CHeeeeeeck","$check, $key")
                        products.add(ItemHelperClass(key!!,check,imgUrlStr,"Zara",itemName,itemPrice,href))
                    }

                }, imgUrlStr, href)
                Log.d("MyLog","Zara: $itemName , $itemPrice , $href , $imgUrlStr")
            }
        } else {
            val productLists : Elements = doc.getElementsByClass("product-grid")
            val items: Elements
            if(appName.text.equals("Dresses")){
                items = productLists[1].getElementsByClass("product-grid__product-list")[0].
                getElementsByAttributeValueContaining("class","product-grid-block-dynamic product-grid-block-dynamic__container")
            } else {
                items = productLists[0].getElementsByClass("product-grid__product-list")[0].
                getElementsByAttributeValueContaining("class","product-grid-block-dynamic product-grid-block-dynamic__container")
            }

            for(item in items){
                val itemContainer1: Elements = item.children()[1].
                getElementsByAttributeValueContaining("class","product-grid-product _product product-grid-product--ZOOM1-columns")
                val itemContainer2: Element = item.children()[2]
                var tmp: Int = 0
                for(i in itemContainer1){
                    if(i.getElementsByClass("media-video__player").size!=0)
                        continue
                    val href : String = i.select("a")[0].attr("href")
                    val imgUrlStr: String = i.getElementsByAttributeValueContaining("class","media-image__image media__wrapper--media")[0].attr("abs:src")
                    if(itemContainer2.children()[0].children().size == 0)
                        continue
                    val itemMargin: Element = itemContainer2.children()[tmp]
                    if(itemMargin.select("span").size == 0 || itemMargin.getElementsByClass("price-current__amount").size == 0)
                        continue
                    val itemName : String = itemMargin.select("span")[0].text()
                    val itemPrice: String = itemMargin.getElementsByClass("price-current__amount")[0].text()
                    if(imgUrlStr.contains("png"))
                        continue
                    readData(object: FirebaseCallback{
                        override fun onCallback(check: Boolean, key: String?) {
                            Log.d("CHeeeeeeck","$check, $key")
                            products.add(ItemHelperClass(key!!,check,imgUrlStr,"Zara",itemName,itemPrice,href))
                        }

                    }, imgUrlStr, href)
                    Log.d("MyLog","Zara: $itemName , $itemPrice , $href , $imgUrlStr")
                    tmp++
                }
            }
        }

    }




    private fun readData(firebaseCallback: FirebaseCallback, imgUrlStr: String, href: String){
        var check = false
        var key: String? = ""
        favListRef.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(snap in snapshot.children){
                    if(snap.child("image").getValue(String::class.java).equals(imgUrlStr) && snap.child("itemHref").getValue(String::class.java).equals(href)){

                        key = snap.child("postKey").getValue(String::class.java)
                        check = true
                        firebaseCallback.onCallback(check,key)

                    }
                }
                if(!check)
                    firebaseCallback.onCallback(false,favListRef.push().key.toString().replace("[_~!@#\$%^&*+-]".toRegex(),"E"))
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


    private interface FirebaseCallback: Callback {
        fun onCallback(check:Boolean ,key: String?)
    }


    private fun getWebWildBerries() {
//        doc = Jsoup.connect("https://www.wildberries.ru/catalog/obuv/muzhskaya").userAgent("Chrome/81.0.4044.138")
//            .header("Accept", "text/html")
//            .header("Accept-Encoding", "gzip,deflate")
//            .header("Accept-Language", "it-IT,en;q=0.8,en-US;q=0.6,de;q=0.4,it;q=0.2,es;q=0.2")
//            .header("Connection", "keep-alive").get()
//        val productList : Element = doc.getElementsByClass("product-card-list")[0]
//        val items: Elements = productList.getElementsByAttributeValueContaining("class", "product-card j-card-item")
//        for(item in items){
//            val itemContainer: Element = item.select("a")[0]
//            val hrefEnd: String = itemContainer.attr("href")
//            val href: String = "https://www.wildberries.ru$hrefEnd"
//            var imgUrlStr: String? = itemContainer.getElementsByClass("product-card__img")[0]
//                .getElementsByAttributeValueContaining("class", "product-card__img-wrap")[0].select("img")[0]
//                .attr("data-original")
//            if(imgUrlStr.equals("")){
//                imgUrlStr = itemContainer.getElementsByClass("product-card__img")[0]
//                .getElementsByAttributeValueContaining("class", "product-card__img-wrap")[0].select("img")[0]
//                    .attr("abs:src")
//            }   else
//            {
//                imgUrlStr = "https:$imgUrlStr"
//            }
//            val itemPrice: String = itemContainer.getElementsByClass("product-card__price")[0]
//                .getElementsByClass("price")[0].getElementsByClass("lower-price")[0].text()
//            val itemStartName: String = itemContainer.getElementsByClass("product-card__brand-name")[0]
//                .getElementsByClass("brand-name")[0].text()
//            val itemEndName: String = itemContainer.getElementsByClass("product-card__brand-name")[0]
//                .getElementsByClass("goods-name")[0].text()
//            val itemName: String = "$itemStartName$itemEndName"
//            shoes.add(ItemHelperClass(imgUrlStr!!,"Wildberries",itemName,itemPrice,href))
//            Log.d("MyLog","Wildberries: $itemName , $itemPrice , $href , $imgUrlStr")
//        }

    }
}