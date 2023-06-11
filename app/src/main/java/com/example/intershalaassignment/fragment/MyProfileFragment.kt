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
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.intershalaassignment.R
import com.example.intershalaassignment.activity.LoginActivity
import com.example.intershalaassignment.util.SessionManager

class MyProfileFragment : Fragment() {
    lateinit var logOut : Button
    lateinit var sessionManager: SessionManager
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var activity: AppCompatActivity
    lateinit var username : TextView
    lateinit var email : TextView
    lateinit var phoneNumber : TextView
    lateinit var addess : TextView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as AppCompatActivity
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        sharedPreferences = this.requireActivity().getSharedPreferences("userInfo",0)
        sessionManager = SessionManager(activity as Context)
        sharedPreferences = this.requireActivity().getSharedPreferences(sessionManager.PREF_NAME, sessionManager.PRIVATE_MODE)
        val view = inflater.inflate(R.layout.fragment_my_profile, container, false)

        username = view.findViewById(R.id.txt_name)
        email = view.findViewById(R.id.txt_email)
        phoneNumber = view.findViewById(R.id.txt_mobileNumber)
        addess = view.findViewById(R.id.txt_address)

        username.text = sharedPreferences.getString("name","")
        email.text = sharedPreferences.getString("email","")
        phoneNumber.text = sharedPreferences.getString("mobile_number","")
        addess.text = sharedPreferences.getString("address","")

        logOut = view.findViewById(R.id.btn_logOut)

        logOut.setOnClickListener {
            val intent = Intent(context,LoginActivity::class.java)
            startActivity(intent)
            sharedPreferences.edit().remove("isLoggedIn").apply()
            activity.finish()
        }
        return view
    }
}