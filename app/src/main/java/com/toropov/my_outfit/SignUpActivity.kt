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
            registerUser()
        }

    }

    private fun validateName(): Boolean{
        val fullName: String = regFullName.editText?.text.toString()

        return if(fullName.isEmpty()){
            regFullName.error = "Field can't be empty"
            false;
        } else {
            regFullName.error = null
            regFullName.isErrorEnabled = false
            true;
        }
    }

    private fun validateUsername(): Boolean{
        val username: String = regUsername.editText?.text.toString()
        val noWhiteSpace: Regex = "\\A\\w{4,20}\\z".toRegex()

        if(username.isEmpty()){
            regUsername.error = "Field can't be empty"
            return false
        } else if (username.length <= 3){
            regUsername.error = "Username is too short"
            return false
        }
        else if (username.length >= 15){
            regUsername.error = "Username too long"
            return false
        } else if(!username.matches(noWhiteSpace)){
            regUsername.error = "White Spaces are not allowed"
            return false
        }
        else {
            regUsername.error = null
            regUsername.isErrorEnabled = false
            return true
        }
    }

    private fun validateEmail(): Boolean{
        val email: String = regEmail.editText?.text.toString()
        val emailPattern : Regex = "[a-zA-Z0-9._]+@[a-z]+\\.+[a-z]+".toRegex()

        if(email.isEmpty()){
            regEmail.error = "Field can't be empty"
            return false
        } else if(!email.matches(emailPattern)){
            regEmail.error = "Invalid email address"
            return false
        }
        else {
            regEmail.error = null
            regEmail.isErrorEnabled = false
            return true
        }
    }

    private fun validatePassword(): Boolean{
        val password: String = regPassword.editText?.text.toString()
        val passwordPattern: Regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$".toRegex()

        if(password.isEmpty()){
            regPassword.error = "Field can't be empty"
            return false
        } else if(!password.matches(passwordPattern)){
            regPassword.error = "Password is too weak"
            return false
        }
        else {
            regPassword.error = null
            regPassword.isErrorEnabled = false
            return true
        }
    }

    private fun registerUser() {

        print(2)
        if(!validateName() or !validateUsername() or !validateEmail() or !validatePassword()){
            return
        }

        val fullName: String = regFullName.editText?.text.toString()
        val username: String = regUsername.editText?.text.toString()
        val email: String = regEmail.editText?.text.toString()
        val password: String = regPassword.editText?.text.toString()


        val helperClass = UserHelperClass(fullName,username,email,password)
        reference.child(username).setValue(helperClass)
    }


}


