package com.toropov.my_outfit.activities

import android.app.ActivityOptions
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Pair
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.toropov.my_outfit.R

class LoginActivity : AppCompatActivity() {

    private lateinit var text: TextView
    private lateinit var logo: ImageView
    private lateinit var logUsername: TextInputLayout
    private lateinit var logPassword: TextInputLayout
    private lateinit var callSignUp: Button
    private lateinit var logBtn: Button
    private lateinit var forgotPas: Button

    private lateinit var auth: FirebaseAuth

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

        auth = Firebase.auth

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

        forgotPas = findViewById(R.id.forgotPas_btn)

        forgotPas.setOnClickListener{
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
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
                    for(ds in dataSnapshot.children){
                        val passwordFromDB: String? = ds.child("password").getValue(String::class.java)
                        if(passwordFromDB.equals(userEnteredPassword)){

                            logPassword.error = null
                            logPassword.isErrorEnabled = false

                            val nameFromDB: String? =
                                ds.child("fullName").getValue(String::class.java)
                            val usernameFromDB: String? =
                                ds.child("username").getValue(String::class.java)
                            val emailFromDB: String? =
                                ds.child("email").getValue(String::class.java)

                            val intent = Intent(applicationContext, DashBoardActivity::class.java)

                            intent.putExtra("name",nameFromDB)
                            intent.putExtra("username",usernameFromDB)
                            intent.putExtra("password",passwordFromDB)
                            intent.putExtra("email",emailFromDB)

                            if (emailFromDB != null && passwordFromDB!=null ) {
                                auth.signInWithEmailAndPassword(emailFromDB, passwordFromDB)
                                    .addOnCompleteListener(this@LoginActivity) { task ->
                                        if (task.isSuccessful) {
                                            if(auth.currentUser?.isEmailVerified == true){
                                                // Sign in success, update UI with the signed-in user's information
                                                Log.d(TAG, "signInWithEmail:success")
                                                val user = auth.currentUser
                                                Toast.makeText(this@LoginActivity, "User logged in successfully", Toast.LENGTH_SHORT).show()
                                                startActivity(intent)
                                            } else {
                                                Toast.makeText(this@LoginActivity, "Please verify your email address", Toast.LENGTH_SHORT).show()
                                            }

                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                                            Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            }

                        } else {
                            logPassword.error = "Wrong Password"
                            logPassword.requestFocus()
                        }
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


