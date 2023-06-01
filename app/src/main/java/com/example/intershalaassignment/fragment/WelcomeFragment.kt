package com.example.intershalaassignment.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.intershalaassignment.R
import com.example.intershalaassignment.activity.MainActivity
import androidx.activity.OnBackPressedCallback


class WelcomeFragment : Fragment() {

    private lateinit var Register: Button
    private lateinit var Login: Button
    private var doubleBackToExitPressedOnce = false
    lateinit var sharedPreferences : SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Thread.sleep(1)
        requireActivity().installSplashScreen()

        sharedPreferences = requireActivity().getSharedPreferences("userInfo",0)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn",false)
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_welcome, container, false)

        if(isLoggedIn) {
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
        Register = view.findViewById(R.id.btn_resgister)
        Login = view.findViewById(R.id.btn_login)
        Register.setOnClickListener {
            val registerFragment = RegisterFragment()
            requireFragmentManager().beginTransaction().replace(R.id.WelcomeFragment,registerFragment).commit()
        }
        Login.setOnClickListener {
            val loginFragment = LoginFragment()
            requireFragmentManager().beginTransaction().replace(R.id.WelcomeFragment,loginFragment).commit()
        }
        return view
    }

    val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (doubleBackToExitPressedOnce) {
                activity?.onBackPressed()
            } else {
                doubleBackToExitPressedOnce = true
                Toast.makeText(context, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

                Handler(Looper.getMainLooper()).postDelayed({
                    doubleBackToExitPressedOnce = false
                }, 2000)
            }
        }
    }

}