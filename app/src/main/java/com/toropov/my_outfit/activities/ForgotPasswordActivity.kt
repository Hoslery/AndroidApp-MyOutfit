package com.toropov.my_outfit.activities

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.toropov.my_outfit.R

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var back : ImageView
    private lateinit var nextBtn : Button
    private lateinit var userEmail: TextInputLayout

    private lateinit var auth: FirebaseAuth

    //DataBase
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        userEmail = findViewById(R.id.email)
        auth = Firebase.auth

        back = findViewById(R.id.back)

        back.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        nextBtn = findViewById(R.id.forget_password_next_btn)

        nextBtn.setOnClickListener {
            checkEmail()
        }
    }


    private fun checkEmail(){
        if(!validateEmail())
            return
        else
            isUser()
    }

    private fun validateEmail(): Boolean{
        val email: String = userEmail.editText?.text.toString()
        val emailPattern : Regex = "[a-zA-Z0-9._]+@[a-z]+\\.+[a-z]+".toRegex()

        if(email.isEmpty()){
            userEmail.error = "Field can't be empty"
            return false
        } else if(!email.matches(emailPattern)){
            userEmail.error = "Invalid email address"
            return false
        }
        else {
            userEmail.error = null
            userEmail.isErrorEnabled = false
            return true
        }
    }

    private fun isUser(){
        val userEnteredEmail: String = userEmail.editText?.text.toString()

        database = FirebaseDatabase.getInstance("https://myoutfit-6f5f1-default-rtdb.europe-west1.firebasedatabase.app/")
        reference = database.getReference("users")

        val checkUser: Query = reference.orderByChild("email").equalTo(userEnteredEmail)

        checkUser.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){

                    userEmail.error = null
                    userEmail.isErrorEnabled = false
                    for(ds in dataSnapshot.children){
                        val userPas: String? = ds.child("password").getValue(String::class.java)
                        if (userPas != null) {
                            auth.signInWithEmailAndPassword(userEnteredEmail,userPas)
                            Log.d(ContentValues.TAG, "$userPas")

                            val intent = Intent(applicationContext, SetNewPasswordActivity::class.java)
                            intent.putExtra("userEmail",userEnteredEmail)
                            intent.putExtra("userPas",userPas)
                            startActivity(intent)
                        }
                    }


                } else {
                    userEmail.error = "There is no User with the Entered Email"
                    userEmail.requestFocus()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
}