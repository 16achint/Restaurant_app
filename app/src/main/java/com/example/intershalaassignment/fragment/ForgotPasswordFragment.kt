package com.example.intershalaassignment.fragment

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.room.util.joinIntoString
import androidx.room.util.query
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.intershalaassignment.R
import com.example.intershalaassignment.util.SessionManager
import org.json.JSONObject
import java.lang.Exception

class ForgotPasswordFragment : Fragment() {
    lateinit var mobileNumber : EditText
    lateinit var email : EditText
    lateinit var next : Button
    private lateinit var sharedPreferences : SharedPreferences
    private lateinit var sessionManager : SessionManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_forgot_password, container, false)
        sharedPreferences = requireActivity().getSharedPreferences(sessionManager.PREF_NAME,sessionManager.PRIVATE_MODE)

        mobileNumber = view.findViewById(R.id.ed_mobile_number)
        email = view.findViewById(R.id.ed_email)
        next = view.findViewById(R.id.btn_next)

        next.setOnClickListener {
            val number = mobileNumber.text.toString()
            val Email = email.text.toString()

            if(number.isEmpty() || Email.isEmpty()){
                Toast.makeText(context,"Credential not be empty",Toast.LENGTH_SHORT).show()
            }else{
                val url = "13.235.250.119/v2/forgot_password/fetch_result"
                val queue = Volley.newRequestQueue(context)
                val jsonParam = JSONObject()
                jsonParam.put("mobile_Number",number)
                jsonParam.put("email",Email)

                val jsonObjectRequest = object : JsonObjectRequest(Method.POST,url,jsonParam,Response.Listener { response ->
                    try {
                        val data = response.getJSONObject("data")
                        val success = data.getBoolean("success")
                        if(success){
                            val passwordRecovery = PasswordRecoveryFragment()
                            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.forgetPasswordFragment,passwordRecovery)
                        }
                    }catch (e:Exception){
                        Toast.makeText(context,"${e.message}",Toast.LENGTH_SHORT).show()
                    }
                },Response.ErrorListener {error ->
                    Toast.makeText(context,"$error",Toast.LENGTH_SHORT).show()
                }){
                    override fun getHeaders():HashMap<String,String>{
                        val headers = HashMap<String,String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "3e8acc119299ba"

                        return headers
                    }
                }
                queue.add(jsonObjectRequest)
            }
        }
        return view
    }
}