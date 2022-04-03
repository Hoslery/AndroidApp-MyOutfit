package com.toropov.my_outfit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout

class UserProfileActivity : AppCompatActivity() {

    private lateinit var fullName: TextInputLayout
    private lateinit var email: TextInputLayout
    private lateinit var password: TextInputLayout
    private lateinit var fullNameLabel: TextView
    private lateinit var usernameLabel: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        fullName = findViewById(R.id.full_name_profile)
        email = findViewById(R.id.email_profile)
        password = findViewById(R.id.password_profile)
        fullNameLabel = findViewById(R.id.fullname_feild)
        usernameLabel = findViewById(R.id.username_field)

        showAllUserData()
    }

    private fun showAllUserData() {
        val intent: Intent = intent
        val user_username: String? = intent.getStringExtra("username")
        val user_fullName: String? = intent.getStringExtra("name")
        val user_email: String? = intent.getStringExtra("email")
        val user_password: String? = intent.getStringExtra("password")

        fullNameLabel.text = user_fullName
        usernameLabel.text = user_username
        fullName.editText?.setText(user_fullName)
        email.editText?.setText(user_email)
        password.editText?.setText(user_password)
    }
}