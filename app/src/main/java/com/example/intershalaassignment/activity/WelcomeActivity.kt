package com.example.intershalaassignment.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.intershalaassignment.R
import com.example.intershalaassignment.util.SessionManager

class WelcomeActivity : AppCompatActivity() {
    private lateinit var Register: Button
    private lateinit var Login: Button
    private var doubleBackToExitPressedOnce = false

    lateinit var sessionManager: SessionManager
    lateinit var sharedPreferences : SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Thread.sleep(1)
        installSplashScreen()

//        sharedPreferences = getSharedPreferences("userInfo",0)
        sessionManager = SessionManager(this)
        sharedPreferences = this.getSharedPreferences(sessionManager.PREF_NAME, sessionManager.PRIVATE_MODE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn",false)

        setContentView(R.layout.activity_welcome)

        if(isLoggedIn) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        Register = findViewById(R.id.btn_resgister)
        Login = findViewById(R.id.btn_login)
        Register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        Login.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed({
            doubleBackToExitPressedOnce = false
        }, 2000)
    }
}