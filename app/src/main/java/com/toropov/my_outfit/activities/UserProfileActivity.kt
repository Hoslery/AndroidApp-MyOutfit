package com.toropov.my_outfit.activities

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.toropov.my_outfit.R
import com.toropov.my_outfit.helperClasses.FavouriteHelperClass
import javax.security.auth.callback.Callback

class UserProfileActivity : AppCompatActivity() {

    private lateinit var email: TextView
    private lateinit var password: TextView
    private lateinit var fullNameLabel: TextView
    private lateinit var usernameLabel: TextView
    private lateinit var exitBtn: Button
    private lateinit var backBtn: ImageView
    private lateinit var countFav: TextView
    private lateinit var favourites: MaterialCardView
    private lateinit var radioGroup: RadioGroup
    private lateinit var radioMen: RadioButton
    private lateinit var radioWoman: RadioButton

    //DataBase
    private lateinit var database: FirebaseDatabase
    private lateinit var favListRef: DatabaseReference
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        email = findViewById(R.id.email_profile)
        password = findViewById(R.id.password_profile)
        fullNameLabel = findViewById(R.id.fullname_feild)
        usernameLabel = findViewById(R.id.username_field)
        countFav = findViewById(R.id.count_fav)
        favourites = findViewById(R.id.fav)
        exitBtn = findViewById(R.id.exit_btn)

        val intent: Intent = intent
        val intent1 = Intent(this, DashBoardActivity::class.java)
        val intent2 = Intent(this, ItemsActivity::class.java)
        val activityId: Int = intent.getIntExtra("activity",0)

        val user_username: String? = intent.getStringExtra("username")
        val user_fullName: String? = intent.getStringExtra("name")
        val user_email: String? = intent.getStringExtra("email")
        val user_password: String? = intent.getStringExtra("password")

        val appName: String? = intent.getStringExtra("appName")

        intent1.putExtra("username", user_username)
        intent1.putExtra("name", user_fullName)
        intent1.putExtra("email", user_email)
        intent1.putExtra("password", user_password)

        intent2.putExtra("username", user_username)
        intent2.putExtra("name", user_fullName)
        intent2.putExtra("email", user_email)
        intent2.putExtra("password", user_password)
        intent2.putExtra("appName", appName)

        auth = Firebase.auth
        val currentUser = auth.currentUser
        database = FirebaseDatabase.getInstance("https://myoutfit-6f5f1-default-rtdb.europe-west1.firebasedatabase.app/")
        favListRef = database.getReference("favouriteList").child(currentUser!!.uid)

        exitBtn.setOnClickListener {
            auth.signOut()
            val intent3 = Intent(this, LoginActivity::class.java)
            startActivity(intent3)
            finish()
        }

        backBtn = findViewById(R.id.back_pressed)

        backBtn.setOnClickListener {
            if(radioMen.isChecked){
                if(activityId == 1){
                    intent1.putExtra("gender","M")
                    startActivity(intent1)
                } else if(activityId == 2) {
                    intent2.putExtra("gender","M")
                    startActivity(intent2)
                }
            } else if(radioWoman.isChecked) {
                if(activityId == 1){
                    intent1.putExtra("gender","W")
                    startActivity(intent1)
                } else if(activityId == 2) {
                    intent2.putExtra("gender","W")
                    startActivity(intent2)
                }
            } else {
                if(activityId == 1){
                    intent1.putExtra("gender","M")
                    startActivity(intent1)
                } else if(activityId == 2) {
                    intent2.putExtra("gender","M")
                    startActivity(intent2)
                }
            }
            finish()
        }

        showAllUserData()

        readData(object: UserProfileActivity.FirebaseCallback {
            override fun onCallback(count: Long) {
                countFav.text = count.toString()
            }
        })

        favourites.setOnClickListener {
            startActivity(Intent(this,FavouritesActivity::class.java))
        }

        radioGroup = findViewById(R.id.radioGroup)
        radioMen = findViewById(R.id.radio_men)
        radioWoman = findViewById(R.id.radio_woman)

        radioMen.isChecked = update("radioMen")
        radioWoman.isChecked = update("radioWoman")

        radioMen.setOnCheckedChangeListener { _, men_isChecked ->
            saveIntoSharedPrefs("radioMen", men_isChecked)
        }

        radioWoman.setOnCheckedChangeListener { _, woman_isChecked ->
            saveIntoSharedPrefs("radioWoman", woman_isChecked)
        }

    }

    private fun showAllUserData() {
        val intent: Intent = intent
        val user_username: String? = intent.getStringExtra("username")
        val user_fullName: String? = intent.getStringExtra("name")
        val user_email: String? = intent.getStringExtra("email")
        val user_password: String? = intent.getStringExtra("password")

        fullNameLabel.text = user_fullName
        usernameLabel.text = user_username
        email.text = user_email
        password.text = user_password
    }

    private fun readData(firebaseCallback: FirebaseCallback) {
        favListRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                firebaseCallback.onCallback(snapshot.childrenCount)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }



    private interface FirebaseCallback: Callback {
        fun onCallback(count: Long)
    }

    private fun saveIntoSharedPrefs(key: String, value: Boolean){
        val sp: SharedPreferences = getSharedPreferences("saveBtn", MODE_PRIVATE)
        val edidtor: SharedPreferences.Editor = sp.edit()
        edidtor.putBoolean(key,value)
        edidtor.apply()
    }

    private fun update(key: String): Boolean{
        val sp: SharedPreferences = getSharedPreferences("saveBtn", MODE_PRIVATE)
        return sp.getBoolean(key,false)
    }


}