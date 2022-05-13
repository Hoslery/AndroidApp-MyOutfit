package com.toropov.my_outfit.activities

import android.R.attr.data
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
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
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.toropov.my_outfit.R
import com.toropov.my_outfit.adapters.ItemAdapter
import com.toropov.my_outfit.helperClasses.ItemHelperClass
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.util.*
import javax.security.auth.callback.Callback


class ItemsActivity : AppCompatActivity(), ItemAdapter.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appName: TextView
    private lateinit var sortBtn: MaterialButton
    private lateinit var sortDescBtn: MaterialButton
    private lateinit var userProfile: ImageView
    private lateinit var doc: Document
    private lateinit var doc1: Document
    private lateinit var hmThread: Thread
    private lateinit var zaraThread: Thread
    private lateinit var farfetchThread: Thread
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

    private var stop = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items)

        userProfile = findViewById(R.id.profile)
        sortBtn = findViewById(R.id.sortBtn)
        sortDescBtn = findViewById(R.id.sortDescBtn)
        sortBtn.isEnabled = false
        sortDescBtn.isEnabled = false

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
            farfetchThread.interrupt()
            val intent1 = Intent(applicationContext, UserProfileActivity::class.java)
            intent1.putExtra("name",user_fullName)
            intent1.putExtra("username",user_username)
            intent1.putExtra("password",user_password)
            intent1.putExtra("email",user_email)
            intent1.putExtra("appName", appName.text)
            intent1.putExtra("activity",2)
            startActivity(intent1)
            finish()
        }

        itemsRecycler.adapter = adapter

        init()


        sortBtn.setOnClickListener {
            products.sortBy {
                it.itemPrice
            }
            adapter.notifyDataSetChanged()
        }

        sortDescBtn.setOnClickListener {
            products.sortByDescending {
                it.itemPrice
            }
            adapter.notifyDataSetChanged()
        }

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
            "Jeans" -> {
                navigationView.setCheckedItem(R.id.nav_jeans)
            }
            "Hoodies" -> {
                navigationView.setCheckedItem(R.id.nav_hoodies)
            }
            "Pants" -> {
                navigationView.setCheckedItem(R.id.nav_pants)
            }
            "Blazers" -> {
                navigationView.setCheckedItem(R.id.nav_blazers)
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
        val user_gender: String? = intent.getStringExtra("gender")

        val intent2 = Intent(this, ItemsActivity::class.java)
        intent2.putExtra("name",user_fullName)
        intent2.putExtra("username",user_username)
        intent2.putExtra("password",user_password)
        intent2.putExtra("email",user_email)
        intent2.putExtra("gender", user_gender)

        when (item.itemId) {
            R.id.nav_home -> {
                farfetchThread.interrupt()
                stop = true
                super.onBackPressed()
                finish()
            }
            R.id.nav_profile -> {
                farfetchThread.interrupt()
                stop = true
                val intent1 = Intent(applicationContext, UserProfileActivity::class.java)
                intent1.putExtra("name",user_fullName)
                intent1.putExtra("username",user_username)
                intent1.putExtra("password",user_password)
                intent1.putExtra("email",user_email)
                intent1.putExtra("activity",2)
                intent1.putExtra("appName", appName.text)
                startActivity(intent1)
                finish()
            }
            R.id.nav_tshirts -> {
                farfetchThread.interrupt()
                stop = true
                intent2.putExtra("appName","T-Shirts")
                startActivity(intent2)
                finish()
            }
            R.id.nav_dresses -> {
                farfetchThread.interrupt()
                stop = true
                intent2.putExtra("appName","Dresses")
                startActivity(intent2)
                finish()
            }
            R.id.nav_jackets-> {
                farfetchThread.interrupt()
                stop = true
                intent2.putExtra("appName","Jackets")
                startActivity(intent2)
                finish()
            }
            R.id.nav_shoes -> {
                farfetchThread.interrupt()
                stop = true
                intent2.putExtra("appName","Shoes")
                startActivity(intent2)
                finish()
            }
            R.id.nav_jeans -> {
                farfetchThread.interrupt()
                stop = true
                intent2.putExtra("appName","Jeans")
                startActivity(intent2)
                finish()
            }
            R.id.nav_hoodies -> {
                farfetchThread.interrupt()
                stop = true
                intent2.putExtra("appName","Hoodies")
                startActivity(intent2)
                finish()
            }
            R.id.nav_pants -> {
                farfetchThread.interrupt()
                stop = true
                intent2.putExtra("appName","Pants")
                startActivity(intent2)
                finish()
            }
            R.id.nav_blazers -> {
                farfetchThread.interrupt()
                stop = true
                intent2.putExtra("appName","Blazers")
                startActivity(intent2)
                finish()
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



    private fun init(){
        runnable = Runnable {
            getWebHM()
        }

        runnable1 = Runnable {
            getWebZara()
        }

        runnable2 = Runnable {
            getFarfetch()
        }


        hmThread = Thread(runnable)
        zaraThread = Thread(runnable1)
        farfetchThread = Thread(runnable2)
        hmThread.start()
        zaraThread.start()
        farfetchThread.start()
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
        val user_gender: String? = intent.getStringExtra("gender")
        var url: String = ""
        when(appName.text){
            "Shoes"->{
                url = "https://www2.hm.com/en_gb/men/shop-by-product/shoes.html"
                if(user_gender.equals("W")){
                    url = "https://www2.hm.com/en_gb/ladies/shop-by-product/shoes.html"
                }
            }
            "T-Shirts"->{
                url = "https://www2.hm.com/en_gb/men/shop-by-product/t-shirts-and-tanks.html"
                if(user_gender.equals("W")){
                    url = "https://www2.hm.com/en_gb/ladies/shop-by-product/tops.html"
                }
            }
            "Dresses"->{
                url = "https://www2.hm.com/en_gb/ladies/shop-by-product/dresses.html"
            }
            "Jackets"->{
                url = "https://www2.hm.com/en_gb/men/shop-by-product/jackets-and-coats.html"
                if(user_gender.equals("W")){
                    url = "https://www2.hm.com/en_gb/ladies/shop-by-product/jackets-and-coats.html"
                }
            }
            "Jeans" -> {
                url = "https://www2.hm.com/en_us/men/products/jeans.html"
                if(user_gender.equals("W")){
                    url = "https://www2.hm.com/en_gb/ladies/shop-by-product/jeans.html"
                }
            }
            "Hoodies" -> {
                url = "https://www2.hm.com/en_us/men/products/hoodies-sweatshirts.html"
                if(user_gender.equals("W")){
                    url = "https://www2.hm.com/en_gb/ladies/shop-by-product/hoodies-sweatshirts.html"
                }
            }
            "Pants" -> {
                url = "https://www2.hm.com/en_us/men/products/pants.html"
                if(user_gender.equals("W")){
                    url = "https://www2.hm.com/en_gb/ladies/shop-by-product/trousers.html"
                }
            }
            "Blazers" -> {
                url = "https://www2.hm.com/en_us/men/products/suits-blazers.html"
                if(user_gender.equals("W")){
                    url = "https://www2.hm.com/en_gb/ladies/shop-by-product/blazers-and-waistcoats.html"
                }
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
            val itemPrice: String = itemDetails.getElementsByClass("item-price")[0].text().drop(1)
            val hrefEnd: String = imageContainer.children()[0].attr("href")
            val href = "https://www2.hm.com$hrefEnd"
            val imgUrlStr: String = imageContainer.select("img")[0].attr("abs:data-src")
            readData(object: FirebaseCallback{
                override fun onCallback(check: Boolean, key: String?) {
                    products.add(ItemHelperClass(key!!,check,imgUrlStr,"H&M",itemName,itemPrice.toDouble(),href))
                    adapter.notifyDataSetChanged()
                }

            }, imgUrlStr, href)
            Log.d("MyLog","H&M: $itemName , $itemPrice , $href , $imgUrlStr")
        }

    }

    private fun getWebZara() {
        val user_gender: String? = intent.getStringExtra("gender")
        var url: String = ""
        when(appName.text){
            "Shoes"->{
                url = "https://www.zara.com/us/en/man-shoes-l769.html?v1=2032982"
                if(user_gender.equals("W")){
                    url = "https://www.zara.com/us/en/woman-shoes-l1251.html?v1=2026736"
                }
            }
            "T-Shirts"->{
                url = "https://www.zara.com/us/en/man-tshirts-l855.html?v1=2032069"
                if(user_gender.equals("W")){
                    url = "https://www.zara.com/us/en/woman-tshirts-l1362.html?v1=2025627"
                }
            }
            "Dresses"->{
                url = "https://www.zara.com/us/en/woman-dresses-l1066.html?v1=2026630"
            }
            "Jackets"->{
                url = "https://www.zara.com/us/en/man-jackets-l640.html?v1=2056396"
                if(user_gender.equals("W")){
                    url = "https://www.zara.com/us/en/woman-jackets-l1114.html?v1=2025657"
                }
            }
            "Jeans" -> {
                url = "https://www.zara.com/us/en/man-jeans-slim-l675.html?v1=2032230"
                if(user_gender.equals("W")){
                    url = "https://www.zara.com/us/en/woman-jeans-l1119.html?v1=2025798"
                }
            }
            "Hoodies" -> {
                url = "https://www.zara.com/us/en/man-sweatshirts-l821.html?v1=2032879"
                if(user_gender.equals("W")){
                    url = "https://www.zara.com/us/en/woman-sweatshirts-l1320.html?v1=2026528"
                }
            }
            "Pants" -> {
                url = "https://www.zara.com/us/en/man-trousers-l838.html?v1=2057415"
                if(user_gender.equals("W")){
                    url = "https://www.zara.com/us/en/woman-trousers-l1335.html?v1=2025900"
                }
            }
            "Blazers" -> {
                url = "https://www.zara.com/us/en/man-blazers-l608.html?v1=2031947"
                if(user_gender.equals("W")){
                    url = "https://www.zara.com/us/en/woman-blazers-l1055.html?v1=2025498"
                }
            }
        }
        doc = Jsoup.connect(url).userAgent("Mozilla")
            .header("Accept", "text/html")
            .header("Accept-Encoding", "gzip,deflate")
            .header("Accept-Language", "it-IT,en;q=0.8,en-US;q=0.6,de;q=0.4,it;q=0.2,es;q=0.2")
            .header("Connection", "keep-alive")
            .ignoreContentType(true)
            .get()
        if((appName.text.equals("Jackets") && user_gender.equals("M")) || (appName.text.equals("Pants") && user_gender.equals("M"))){
            val productLists : Elements = doc.getElementsByClass("product-grid")
            val items: Elements = productLists[1].getElementsByClass("product-grid__product-list")[0].
            getElementsByAttributeValueContaining("class","product-grid-product _product product-grid-product--zoom1-columns")
            for(item in items){
                if(item.getElementsByAttributeValueContaining("class","media-regions").size!=0)
                    continue
                val itemContainer1: Element = item.select("a")[1]
                val itemName : String = itemContainer1.select("span")[0].text()
                val href : String = itemContainer1.attr("href")
                val itemPrice: String = item.getElementsByClass("price-current__amount")[0].text().split(" ")[0]
                val imgUrlStr: String = item.getElementsByClass("media-image__image")[0].attr("abs:src")
                if(imgUrlStr.contains("png"))
                    continue
                readData(object: FirebaseCallback{
                    override fun onCallback(check: Boolean, key: String?) {
                        products.add(ItemHelperClass(key!!,check,imgUrlStr,"Zara",itemName,itemPrice.toDouble(),href))
                        adapter.notifyDataSetChanged()
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
                if(item.childrenSize()<=2)
                    continue
                val itemContainer1: Elements = item.children()[1].
                getElementsByAttributeValueContaining("class","product-grid-product _product product-grid-product--ZOOM1-columns")
                val itemContainer2: Element = item.children()[2]
                var tmp: Int = 0
                for(i in itemContainer1){
                    if(i.getElementsByClass("media-video__player").size!=0)
                        continue
                    if(i.select("a").size == 0)
                        continue
                    val href : String = i.select("a")[0].attr("href")
                    val imgUrlStr: String = i.getElementsByAttributeValueContaining("class","media-image__image media__wrapper--media")[0].attr("abs:src")
                    if(itemContainer2.children()[0].children().size == 0)
                        continue
                    val itemMargin: Element = itemContainer2.children()[tmp]
                    if(itemMargin.select("span").size == 0 || itemMargin.getElementsByClass("price-current__amount").size == 0)
                        continue
                    val itemName : String = itemMargin.select("span")[0].text()
                    if(itemName.equals("+2"))
                        continue
                    val itemPrice: String = itemMargin.getElementsByClass("price-current__amount")[0].text().split(" ")[0]
                    if(imgUrlStr.contains("png"))
                        continue
                    readData(object: FirebaseCallback{
                        override fun onCallback(check: Boolean, key: String?) {
                            products.add(ItemHelperClass(key!!,check,imgUrlStr,"Zara",itemName,itemPrice.toDouble(),href))
                            adapter.notifyDataSetChanged()
                        }

                    }, imgUrlStr, href)
                    Log.d("MyLog","Zara: $itemName , $itemPrice , $href , $imgUrlStr")
                    tmp++
                }
            }
        }

    }

    private fun getFarfetch(){
        val user_gender: String? = intent.getStringExtra("gender")
        var url: String = ""
        when(appName.text){
            "Shoes"->{
                url = "https://www.farfetch.com/fi/shopping/men/shoes-2/items.aspx?page=1&view=90&sort=3&scale=282&category=137174"
                if(user_gender.equals("W")){
                    url = "https://www.farfetch.com/fi/shopping/women/shoes-1/items.aspx?page=1&view=90&sort=3&scale=274&category=136302"
                }
            }
            "T-Shirts"->{
                url = "https://www.farfetch.com/fi/shopping/men/clothing-2/items.aspx?page=1&view=90&sort=3&category=136332"
                if(user_gender.equals("W")){
                    url = "https://www.farfetch.com/fi/shopping/women/clothing-1/items.aspx?page=1&view=90&sort=3&category=136091"
                }
            }
            "Dresses"->{
                url = "https://www.farfetch.com/fi/shopping/women/dresses-1/items.aspx?page=1&view=90&sort=3&category=136189"
            }
            "Jackets"->{
                url = "https://www.farfetch.com/fi/shopping/men/clothing-2/items.aspx?page=1&view=90&sort=3&category=136335"
                if(user_gender.equals("W")){
                    url = "https://www.farfetch.com/fi/shopping/women/clothing-1/items.aspx?page=1&view=90&sort=3&category=136226"
                }
            }
            "Jeans" -> {
                url = "https://www.farfetch.com/fi/shopping/men/clothing-2/items.aspx?page=1&view=90&sort=3&category=136421"
                if(user_gender.equals("W")){
                    url = "https://www.farfetch.com/fi/shopping/women/clothing-1/items.aspx?page=1&view=90&sort=3&category=136175"
                }
            }
            "Hoodies" -> {
                url = "https://www.farfetch.com/fi/shopping/men/clothing-2/items.aspx?page=1&view=90&sort=3&category=136398"
                if(user_gender.equals("W")){
                    url = "https://www.farfetch.com/fi/shopping/women/clothing-1/items.aspx?page=1&view=90&sort=3&category=136157"
                }
            }
            "Pants" -> {
                url = "https://www.farfetch.com/fi/shopping/men/trousers-2/items.aspx?page=1&view=90&sort=3&category=136433"
                if(user_gender.equals("W")){
                    url = "https://www.farfetch.com/fi/shopping/women/clothing-1/items.aspx?page=1&view=90&sort=3&category=135981"
                }
            }
            "Blazers" -> {
                url = "https://www.farfetch.com/fi/shopping/men/jackets-2/items.aspx?page=1&view=90&sort=3&category=136402"
                if(user_gender.equals("W")){
                    url = "https://www.farfetch.com/fi/shopping/women/clothing-1/items.aspx?page=1&view=90&sort=3&category=136229"
                }
            }
        }

        try{
            for(i in 1..18){
                if(stop)
                    return
                val urlStart = url.substringBefore("=")
                val urlEnd = url.substringAfter("=").drop(1)
                val urlCon: String = "$urlStart=$i$urlEnd"

                doc = Jsoup.connect(urlCon).userAgent("Mozilla")
                    .header("Accept", "text/html")
                    .header("Accept-Encoding", "gzip,deflate")
                    .header("Accept-Language", "it-IT,en;q=0.8,en-US;q=0.6,de;q=0.4,it;q=0.2,es;q=0.2")
                    .header("Connection", "keep-alive")
                    .ignoreContentType(false)
                    .get()


                if(doc.getElementsByAttributeValueContaining("data-testid","productArea").size == 0)
                    continue
                val productList : Element = doc.getElementsByAttributeValueContaining("data-testid","productArea")[0]
                val items: Elements = productList.getElementsByAttributeValueContaining("data-testid", "productCard")
                for(item in items){
                    val itemName : String = item.select("a").attr("aria-label")
                    val hrefEnd: String = item.select("a").attr("href")
                    val href = "https://www.farfetch.com$hrefEnd"
                    val itemPrice: String = item.select("a")[0].getElementsByAttributeValueContaining("data-component", "ProductCardInfo")[0].
                    getElementsByAttributeValueContaining("data-component", "PriceBrief")[0].select("p")[0].text().split(" ")[0].
                    replace(",","")
                    var imgUrlStr: String
                    if(item.select("a")[0].getElementsByAttributeValueContaining("data-component", "ProductCardHeader")[0].
                        select("link").size == 0){
                            imgUrlStr = item.select("a")[0].getElementsByAttributeValueContaining("data-component", "ProductCardHeader")[0].
                            select("img")[0].attr("abs:src")
                    }else {
                        imgUrlStr = item.select("a")[0].getElementsByAttributeValueContaining("data-component", "ProductCardHeader")[0].
                        select("link")[0].attr("href")
                    }
                    if(imgUrlStr.equals(""))
                        continue
                    readData(object: FirebaseCallback{
                        override fun onCallback(check: Boolean, key: String?) {
                            products.add(ItemHelperClass(key!!,check,imgUrlStr,"Farfetch",itemName,itemPrice.toDouble(),href))
                            adapter.notifyDataSetChanged()
                        } }, imgUrlStr, href)
                    Log.d("MyLog","Farfetch: $itemName , $itemPrice , $href , $imgUrlStr")
                }

            }
        } catch(e: InterruptedException ){
            return
        }

        runOnUiThread(Runnable() {
            sortBtn.isEnabled = true
            sortDescBtn.isEnabled = true
        })

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

}