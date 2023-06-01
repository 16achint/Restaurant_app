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
import androidx.appcompat.app.AppCompatActivity
import com.example.intershalaassignment.R
import com.example.intershalaassignment.activity.LoginActivity
import com.example.intershalaassignment.util.SessionManager

class MyProfileFragment : Fragment() {
    lateinit var logOut : Button
    lateinit var sessionManager: SessionManager
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var activity: AppCompatActivity
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