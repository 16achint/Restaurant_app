package com.example.intershalaassignment.activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.intershalaassignment.R
import com.example.intershalaassignment.util.SessionManager
import org.json.JSONObject
import java.lang.Exception
import java.util.Objects

class LoginActivity : AppCompatActivity() {
    private lateinit var mobileNumber: EditText
    private lateinit var Password: EditText
    private lateinit var login: Button
    private lateinit var ForgotPassword : TextView

    lateinit var sessionManager: SessionManager
    lateinit var sharedPreferences : SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        sharedPreferences = getSharedPreferences("userInfo",0)
        sessionManager = SessionManager(this)
        sharedPreferences = this.getSharedPreferences(sessionManager.PREF_NAME, sessionManager.PRIVATE_MODE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn",false)
        if(isLoggedIn){
            val intent = Intent(this@LoginActivity,MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
            return
        }
        setContentView(R.layout.activity_login)

        mobileNumber = findViewById(R.id.ed_mobile_number)
        Password = findViewById(R.id.ed_pass)
        login = findViewById(R.id.btn_login)
        ForgotPassword = findViewById(R.id.txt_Forgot_password)

        ForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
        login.setOnClickListener {
            val Useremail = mobileNumber.text.toString()
            val Userpassword = Password.text.toString()

            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            if (Useremail.isEmpty() || Userpassword.isEmpty()) {
                Toast.makeText(this@LoginActivity, "Please fill in all fields",Toast.LENGTH_SHORT).show()
            }
            else{
//                val mobileNumber = sharedPreferences.getString("email", "")
//                val password = sharedPreferences.getString("password", "")
//                if (Useremail.equals(mobileNumber) && Userpassword.equals(password)) {
                    sendRequest(Useremail,Userpassword)
                    savePreference()
//                    val intent = Intent(this@LoginActivity, MainActivity::class.java)

//                    startActivity(intent)
//                    finish()
                }
//                else{
//                    Toast.makeText(
//                        this@LoginActivity,
//                        "field not filled",
//                        Toast.LENGTH_SHORT
//                    )
//                        .show()
//                }
//            }

        }
    }

    private fun sendRequest(mobileNumber: String, password: String) {
        val url = "http://13.235.250.119/v2/login/fetch_result"
        val queue = Volley.newRequestQueue(this)
        val jsonParam = JSONObject()
        jsonParam.put("mobile_number",mobileNumber)
        jsonParam.put("password",password)
        val JsonObjectRequest = object : JsonObjectRequest(Method.POST, url, jsonParam, Response.Listener { response ->
            try {
                val data = response.getJSONObject("data")
                val success = data.getBoolean("success")
                if(success){
                    val response = data.getJSONObject("data")
                    Toast.makeText(this@LoginActivity, "READY TO GO", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@LoginActivity,MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }else{
                    Toast.makeText(this@LoginActivity, "Success error occurred", Toast.LENGTH_SHORT).show()
                }
            }catch (e: Exception) {
                e.printStackTrace()
            }
        },Response.ErrorListener {error ->
            Toast.makeText(this@LoginActivity, "$error", Toast.LENGTH_SHORT).show()
        }){
            override fun getHeaders(): HashMap<String,String>{
                val headers = HashMap<String, String>()
                headers["Content-type"] = "application/json"
                headers["token"] = "3e8acc119299ba"
                return headers
            }
        }
        queue.add(JsonObjectRequest)
    }

    override fun onPause() {
        super.onPause()
        finish()
    }

    private fun savePreference() {
        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
    }
    override fun onBackPressed(){
        val intent = Intent(this,WelcomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}