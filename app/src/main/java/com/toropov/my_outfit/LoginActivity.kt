package com.toropov.my_outfit

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Pair
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {

    private lateinit var text: TextView
    private lateinit var logo: ImageView
    private lateinit var logUsername: TextInputLayout
    private lateinit var logPassword: TextInputLayout
    private lateinit var callSignUp: Button
    private lateinit var logBtn: Button

    //DataBase
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_login)
        text = findViewById<TextView>(R.id.welcome_text)
        logo = findViewById<ImageView>(R.id.search_logo)
        logUsername = findViewById<TextInputLayout>(R.id.username)
        logPassword = findViewById<TextInputLayout>(R.id.password)
        callSignUp = findViewById(R.id.signup_screen)

        callSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            val pair = listOf<Pair<View, String>>(
                Pair<View, String>(logo, "logo_image"),
                Pair<View, String>(text, "logo_text"),
                Pair<View, String>(logUsername, "username"),
                Pair<View, String>(logPassword, "password"),
            )
            val options: ActivityOptions = ActivityOptions.makeSceneTransitionAnimation(this,
                pair[0],pair[1],pair[2],pair[3]
            )
            startActivity(intent,options.toBundle())
        }

        logBtn = findViewById(R.id.signin_btn)

        logBtn.setOnClickListener {
            loginUser()
        }
    }

    private fun validateUsername(): Boolean{
        val username: String = logUsername.editText?.text.toString()


        if(username.isEmpty()){
            logUsername.error = "Field can't be empty"
            return false
        }
        else {
            logUsername.error = null
            logUsername.isErrorEnabled = false
            return true
        }
    }

    private fun validatePassword(): Boolean{
        val password: String = logPassword.editText?.text.toString()


        if(password.isEmpty()){
            logPassword.error = "Field can't be empty"
            return false
        }
        else {
            logPassword.error = null
            logPassword.isErrorEnabled = false
            return true
        }
    }

    private fun loginUser(){
        if(!validateUsername() or !validatePassword()){
            return
        } else {
            isUser()
        }
    }

    private fun isUser(){
        val userEnteredUsername: String = logUsername.editText?.text.toString().trim()
        val userEnteredPassword: String = logPassword.editText?.text.toString().trim()

        database = FirebaseDatabase.getInstance("https://myoutfit-6f5f1-default-rtdb.europe-west1.firebasedatabase.app/")
        reference = database.getReference("users")

        val checkUser: Query = reference.orderByChild("username").equalTo(userEnteredUsername)

        checkUser.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){

                    logUsername.error = null
                    logUsername.isErrorEnabled = false

                    val passwordFromDB: String? =
                        dataSnapshot.child(userEnteredUsername).child("password").getValue(String::class.java)
                    if(passwordFromDB.equals(userEnteredPassword)){

                        logPassword.error = null
                        logPassword.isErrorEnabled = false

                        val nameFromDB: String? =
                            dataSnapshot.child(userEnteredUsername).child("fullName").getValue(String::class.java)
                        val usernameFromDB: String? =
                            dataSnapshot.child(userEnteredUsername).child("username").getValue(String::class.java)
                        val emailFromDB: String? =
                            dataSnapshot.child(userEnteredUsername).child("email").getValue(String::class.java)

                        val intent = Intent(applicationContext, UserProfileActivity::class.java)

                        intent.putExtra("name",nameFromDB)
                        intent.putExtra("username",usernameFromDB)
                        intent.putExtra("password",passwordFromDB)
                        intent.putExtra("email",emailFromDB)

                        startActivity(intent)
                    } else {
                        logPassword.error = "Wrong Password"
                        logPassword.requestFocus()
                    }
                } else {
                    logUsername.error = "No such User exist"
                    logUsername.requestFocus()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}


