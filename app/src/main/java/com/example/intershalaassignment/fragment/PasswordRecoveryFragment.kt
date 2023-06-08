package com.example.intershalaassignment.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.room.util.findColumnIndexBySuffix
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.intershalaassignment.R
import com.example.intershalaassignment.activity.LoginActivity
import com.example.intershalaassignment.util.SessionManager
import org.json.JSONObject
import java.lang.Exception

class PasswordRecoveryFragment : Fragment() {

    lateinit var otp: EditText
    lateinit var newPassword: EditText
    lateinit var confirmPassword: EditText
    lateinit var submit: Button
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_password_recovery, container, false)
        sessionManager = SessionManager(activity as Context)
        sharedPreferences = requireActivity().getSharedPreferences(
            sessionManager.PREF_NAME,
            sessionManager.PRIVATE_MODE
        )
        val number = sharedPreferences.getString("mobile_number", "")

        otp = view.findViewById(R.id.ed_otp)
        newPassword = view.findViewById(R.id.ed_newPassword)
        confirmPassword = view.findViewById(R.id.ed_ConfirmPassword)
        submit = view.findViewById(R.id.btn_submit)

        submit.setOnClickListener {

            val OTP = otp.text.toString()
            val password = newPassword.text.toString()
            val cfmPassword = confirmPassword.text.toString()

            if (password.isEmpty() || cfmPassword.isEmpty() || OTP.isEmpty()) {
                Toast.makeText(context, "filed can not Empty", Toast.LENGTH_SHORT).show()
            } else {
                if (password == cfmPassword) {
                    sendRequest(OTP, password, number)
                } else {
                    Toast.makeText(
                        context,
                        "password and confirm password should be match",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        return view
    }

    private fun sendRequest(otp: String, password: String, number: String?) {
        val url = "http://13.235.250.119/v2/reset_password/fetch_result"
        val queue = Volley.newRequestQueue(context)
        val jsonParam = JSONObject()
        jsonParam.put("mobile_number", number)
        jsonParam.put("password", password)
        jsonParam.put("otp", otp)

        val jsonObjectRequest =
            object : JsonObjectRequest(Method.POST, url, jsonParam, Response.Listener { response ->
                try {
                    val data = response.getJSONObject("data")
                    val success = data.getBoolean("success")
                    if (success) {
                        Toast.makeText(
                            context,
                            "your password have been updated",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(context, LoginActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "${e.message}", Toast.LENGTH_SHORT).show()
                }
            }, Response.ErrorListener { error ->
                Toast.makeText(context, "$error", Toast.LENGTH_SHORT).show()
            }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "3e8acc119299ba"
                    return headers
                }
            }
        queue.add(jsonObjectRequest)
    }
}