package com.example.intershalaassignment.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.intershalaassignment.R

class SuccessScreenActivity : AppCompatActivity() {
    lateinit var btnOk : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success_screen)
        btnOk = findViewById(R.id.btnOk)

        btnOk.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }
}