package com.example.intershalaassignment.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Header
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.intershalaassignment.R
import com.example.intershalaassignment.util.ConnectionManager
import com.example.intershalaassignment.util.SessionManager
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class RegisterActivity : AppCompatActivity() {
    private lateinit var Name: EditText
    private lateinit var Email: EditText
    private lateinit var mobileNumber: EditText
    private lateinit var deliveryAddress: EditText
    private lateinit var Password: EditText
    private lateinit var ConfirmPassword: EditText

    private lateinit var Login: TextView
    private lateinit var Register: Button

    lateinit var sessionManager: SessionManager
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

//        sharedPreferences = getSharedPreferences("userInfo", 0)
        sessionManager = SessionManager(this@RegisterActivity)
        sharedPreferences = this@RegisterActivity.getSharedPreferences(sessionManager.PREF_NAME, sessionManager.PRIVATE_MODE)
        Name = findViewById(R.id.ed_name)
        Email = findViewById(R.id.ed_ForgotEmail)
        mobileNumber = findViewById(R.id.ed_mobile_number)
        deliveryAddress = findViewById(R.id.ed_address)
        Password = findViewById(R.id.ed_ForgotPassword)
        ConfirmPassword = findViewById(R.id.ed_ConfirmPassword)
        Register = findViewById(R.id.btn_resgister)
        Login = findViewById(R.id.txt_login)

//        sharedPreferences
        Login.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        Register.setOnClickListener {
            val name = Name.text.toString()
            val email = Email.text.toString()
            val mobileNumber = mobileNumber.text.toString()
            val deliveryAddress = deliveryAddress.text.toString()
            val confirmPassword = ConfirmPassword.text.toString()
            val password = Password.text.toString()

            if (email != "" && password != "" && confirmPassword != "" && name != "" && mobileNumber != "" && deliveryAddress != "") {
                if (password != confirmPassword) {
                    Toast.makeText(
                        this,
                        "Password and confirmPassword Mismatch",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (mobileNumber.length != 10) {
                    Toast.makeText(
                        this,
                        "mobile number should be 10 digit only",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    if (ConnectionManager().checkManager(this)) {
                        sendRequest(name, email, mobileNumber, deliveryAddress, password)
                        val editor = sharedPreferences.edit()
                        editor.putString("email", email)
                        editor.putString("password", password)
                        editor.apply()
//                        Toast.makeText(this, "User Registered Login Now", Toast.LENGTH_SHORT).show()

//                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
//                    intent.putExtra("Forgotemail", email)
//                    intent.putExtra("Forgotpassword", password)
//                    intent.putExtra("confirmPassword", confirmPassword)
//                        startActivity(intent)
                    }
                }
            } else {
                Toast.makeText(this, "Field not filled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendRequest(
        name: String,
        email: String,
        mobileNumber: String,
        deliveryAddress: String,
        password: String
    ) {
        val url = "http://13.235.250.119/v2/register/fetch_result"
        val queue = Volley.newRequestQueue(this)
        val jsonParam = JSONObject()
        jsonParam.put("name", name)
        jsonParam.put("mobile_number", mobileNumber)
        jsonParam.put("password", password)
        jsonParam.put("address", deliveryAddress)
        jsonParam.put("email", email)

        val JsonObjectRequest = object :
            JsonObjectRequest(Method.POST, url, jsonParam, Response.Listener { response ->
                try {
                    val data = response.getJSONObject("data")
                    val success = data.getBoolean("success")
                    if (success) {
                        val response = data.getJSONObject("data")
                        sharedPreferences.edit().putString("user_id", response.getString("user_id"))
                            .apply()
                        sharedPreferences.edit().putString("user_name", response.getString("name"))
                            .apply()
                        sharedPreferences.edit()
                            .putString("mobile_number", response.getString("mobile_number")).apply()
                        sharedPreferences.edit()
                            .putString("user_address", response.getString("address")).apply()
                        sharedPreferences.edit()
                            .putString("user_email", response.getString("email")).apply()
                        sessionManager.setLogin(true)
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        val errorMessage = data.getString("errorMessage")
                        Toast.makeText(this, "$errorMessage 2" , Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener { error ->
                Toast.makeText(this@RegisterActivity, "$error 3", Toast.LENGTH_SHORT).show()
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-type"] = "application/json"
                headers["token"] = "3e8acc119299ba"
                return headers
            }
        }
        queue.add(JsonObjectRequest)
    }
}