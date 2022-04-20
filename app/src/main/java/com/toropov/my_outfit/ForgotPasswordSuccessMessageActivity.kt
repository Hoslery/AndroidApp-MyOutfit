package com.toropov.my_outfit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ForgotPasswordSuccessMessageActivity : AppCompatActivity() {

    private lateinit var logBtn : Button

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password_success_message)

        auth = Firebase.auth

        logBtn = findViewById(R.id.login_btn)

        logBtn.setOnClickListener {
            auth.signOut()
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}