package com.toropov.my_outfit.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.toropov.my_outfit.R
import com.toropov.my_outfit.adapters.FavouritesAdapter
import com.toropov.my_outfit.adapters.ItemAdapter
import com.toropov.my_outfit.helperClasses.FavouriteHelperClass
import com.toropov.my_outfit.helperClasses.ItemHelperClass
import javax.security.auth.callback.Callback

class FavouritesActivity : AppCompatActivity(), FavouritesAdapter.OnItemClickListener {

    private lateinit var backBtn: ImageView

    private lateinit var itemsRecycler: RecyclerView
    private val products: MutableList<FavouriteHelperClass> = mutableListOf()
    private val adapter = FavouritesAdapter(products,this)

    //DataBase
    private lateinit var database: FirebaseDatabase
    private lateinit var favListRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourites)

        auth = Firebase.auth
        val currentUser = auth.currentUser
        database = FirebaseDatabase.getInstance("https://myoutfit-6f5f1-default-rtdb.europe-west1.firebasedatabase.app/")
        favListRef = database.getReference("favouriteList").child(currentUser!!.uid)

        itemsRecycler = findViewById(R.id.items_rv)
        itemsRecycler.setHasFixedSize(false)
        itemsRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        backBtn = findViewById(R.id.back_pressed)

        backBtn.setOnClickListener {
            super.onBackPressed()
        }

        readData(object: FavouritesActivity.FirebaseCallback {
            override fun onCallback() {
                itemsRecycler.adapter = adapter
            }
        })


    }

    private fun readData(firebaseCallback: FirebaseCallback) {
        favListRef.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(snap in snapshot.children){
                    val imgUrlStr: String? = snap.child("image").getValue(String::class.java)
                    val itemName: String? = snap.child("itemName").getValue(String::class.java)
                    val itemPrice: String = "${snap.child("itemPrice").getValue(Double::class.java).toString()} $"
                    val itemShop: String? = snap.child("itemShop").getValue(String::class.java)
                    val href: String? = snap.child("itemHref").getValue(String::class.java)
                    products.add(FavouriteHelperClass(imgUrlStr,itemShop,itemName,itemPrice,href))
                }
                firebaseCallback.onCallback()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }



    private interface FirebaseCallback: Callback {
        fun onCallback()
    }

    override fun onItemClicked(favHelperClass: FavouriteHelperClass) {
        val address: Uri = Uri.parse(favHelperClass.itemHref)
        val openLinkIntent = Intent(Intent.ACTION_VIEW, address)
        startActivity(openLinkIntent)
    }
}