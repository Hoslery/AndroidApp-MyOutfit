package com.toropov.my_outfit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class SetNewPasswordActivity : AppCompatActivity() {

    private lateinit var back : ImageView
    private lateinit var updateBtn : Button
    private lateinit var newPas: TextInputLayout
    private lateinit var confirmPas: TextInputLayout

    private lateinit var auth: FirebaseAuth

    //DataBase
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_new_password)

        auth = Firebase.auth
        newPas = findViewById(R.id.new_password)
        confirmPas = findViewById(R.id.confirm_password)

        back = findViewById(R.id.back)
        updateBtn = findViewById(R.id.set_new_password_btn)

        back.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        updateBtn.setOnClickListener {
            updatePassword()
        }
    }

    private fun updatePassword() {
        //Check Internet Connection


        //Validation
        if(!validatePassword() or !validateConfirmPassword()){
            return
        }

        val newPassword: String = newPas.editText?.text.toString()
        val currentUser = auth.currentUser

        currentUser?.updatePassword(newPassword)

        database = FirebaseDatabase.getInstance("https://myoutfit-6f5f1-default-rtdb.europe-west1.firebasedatabase.app/")
        reference = database.getReference("users")

        reference.child(
            FirebaseAuth.getInstance().currentUser!!.uid).child("password").setValue(newPassword)

        val intent = Intent(applicationContext,ForgotPasswordSuccessMessageActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun validatePassword(): Boolean{
        val password: String = newPas.editText?.text.toString()
        val passwordPattern: Regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$".toRegex()

        if(password.isEmpty()){
            newPas.error = "Field can't be empty"
            return false
        } else if(!password.matches(passwordPattern)){
            newPas.error = "Password is too weak"
            return false
        }
        else {
            newPas.error = null
            newPas.isErrorEnabled = false
            return true
        }
    }


    private fun validateConfirmPassword(): Boolean{
        val password: String = newPas.editText?.text.toString()
        val confirmPassword: String = confirmPas.editText?.text.toString()
        if(!confirmPassword.equals(password)){
            confirmPas.error = "Password does not match! Please try again."
            return false
        }else{
            confirmPas.error = null
            confirmPas.isErrorEnabled = false
            return true
        }
    }
}