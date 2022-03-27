package com.toropov.my_outfit

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Pair
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private lateinit var text: TextView
    private lateinit var logo: ImageView
    private lateinit var regFullName: TextInputLayout
    private lateinit var regEmail: TextInputLayout
    private lateinit var regUsername: TextInputLayout
    private lateinit var regPassword: TextInputLayout
    private lateinit var callLogin: Button
    private lateinit var regBtn: Button

    //DataBase
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        text = findViewById<TextView>(R.id.welcome_text)
        logo = findViewById<ImageView>(R.id.search_logo)
        regUsername = findViewById<TextInputLayout>(R.id.username)
        regPassword = findViewById<TextInputLayout>(R.id.password)
        regFullName = findViewById<TextInputLayout>(R.id.name)
        regEmail = findViewById<TextInputLayout>(R.id.email)
        callLogin = findViewById(R.id.have_acc)

        callLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            val pair = listOf<Pair<View, String>>(
                Pair<View, String>(logo, "logo_image"),
                Pair<View, String>(text, "logo_text"),
                Pair<View, String>(regUsername, "username"),
                Pair<View, String>(regPassword, "password"),
            )
            val options: ActivityOptions = ActivityOptions.makeSceneTransitionAnimation(this,
                pair[0],pair[1],pair[2],pair[3]
            )
            startActivity(intent,options.toBundle())
        }

        regBtn = findViewById(R.id.signup_btn)

        regBtn.setOnClickListener{
            database = FirebaseDatabase.getInstance("https://myoutfit-6f5f1-default-rtdb.europe-west1.firebasedatabase.app/")
            reference = database.getReference("users")
            print(2)
            val fullName: String = regFullName.editText?.text.toString()
            val username: String = regUsername.editText?.text.toString()
            val email: String = regEmail.editText?.text.toString()
            val password: String = regPassword.editText?.text.toString()

            val key: String = "$email$password"

            val helperClass = UserHelperClass(fullName,username,email,password)
            reference.push().setValue(helperClass)
        }

    }

}