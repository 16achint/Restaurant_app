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
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentManager
import com.example.intershalaassignment.R
import com.example.intershalaassignment.activity.ForgotPasswordActivity
import com.example.intershalaassignment.activity.MainActivity
import com.example.intershalaassignment.activity.WelcomeActivity

class LoginFragment : Fragment() {

    private lateinit var Email: EditText
    private lateinit var Password: EditText
    private lateinit var login: Button
    private lateinit var ForgotPassword : TextView
    lateinit var sharedPreferences : SharedPreferences

      override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_login, container, false)

          sharedPreferences = requireActivity().getSharedPreferences("userInfo",0)
          val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn",false)
          if(isLoggedIn){
              val intent = Intent(context, MainActivity::class.java)
              intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
              startActivity(intent)
              activity?.finish()
          }

          Email = view.findViewById(R.id.ed_email_phone)
          Password = view.findViewById(R.id.ed_pass)
          login = view.findViewById(R.id.btn_login)
          ForgotPassword = view.findViewById(R.id.txt_Forgot_password)

          ForgotPassword.setOnClickListener {
              val intent = Intent(context, ForgotPasswordActivity::class.java)
              startActivity(intent)
          }

          login.setOnClickListener {
              val Useremail = Email.text.toString()
              val Userpassword = Password.text.toString()
              val intent = Intent(context, MainActivity::class.java)
              intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
              if (Useremail.isEmpty() || Userpassword.isEmpty()) {
                  Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
              }
              else{
                  val validEmail = sharedPreferences.getString("email", "")
                  val validpassword = sharedPreferences.getString("password", "")
                  if (Useremail.equals(validEmail) && Userpassword.equals(validpassword)) {
                      savePreference()
                      val intent = Intent(context, MainActivity::class.java)
                      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                      startActivity(intent)
                      activity?.finish()
                  }
                  else{
                      Toast.makeText(
                          context,
                          "field not filled",
                          Toast.LENGTH_SHORT
                      )
                          .show()
                  }
              }

          }

          return view
    }

    override fun onPause() {
        super.onPause()
        activity?.finish()
    }
    private fun savePreference() {
        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
    }
    val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val welcomeFragment = WelcomeFragment() // Create an instance of the WelcomeFragment
            val transaction = requireFragmentManager().beginTransaction().replace(R.id.loginFragment, welcomeFragment).commit()
            activity?.finish()
        }
    }

}