package com.example.intershalaassignment.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.intershalaassignment.R

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        title = "Forgot Password"
    }
    override fun onBackPressed(){
        val intent = Intent(this,WelcomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}