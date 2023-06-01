package com.example.intershalaassignment.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.intershalaassignment.R
import com.example.intershalaassignment.activity.LoginActivity

class RegisterFragment : Fragment() {
    private lateinit var Email: EditText
    private lateinit var Password: EditText
    private lateinit var ConfirmPassword: EditText
    private lateinit var Login: TextView
    private lateinit var Register: Button
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        sharedPreferences = requireActivity().getSharedPreferences("userInfo",0)

        Email = view.findViewById(R.id.ed_ForgotEmail)
        Password = view.findViewById(R.id.ed_ForgotPassword)
        ConfirmPassword = view.findViewById(R.id.ed_ConfirmPassword)
        Register = view.findViewById(R.id.btn_resgister)
        Login = view.findViewById(R.id.txt_login)

        sharedPreferences
        Login.setOnClickListener {
            val loginFragment = LoginFragment()
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.registerFragment,loginFragment).commit()
            activity?.finish()
        }

        Register.setOnClickListener {
            val email = Email.text.toString()
            val password = Password.text.toString()
            val confirmPassword = ConfirmPassword.text.toString()

            if (email != "" && password != "" && confirmPassword != "") {
                if (password != confirmPassword) {
                    Toast.makeText(
                        context,
                        "Password and confirmPassword Mismatch",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val editor = sharedPreferences.edit()
                    editor.putString("email",email)
                    editor.putString("password",password)
                    editor.apply()
                    Toast.makeText(context, "User Registered Login Now", Toast.LENGTH_SHORT).show()

                    val loginFragment = LoginFragment()
                    requireActivity().supportFragmentManager.beginTransaction().replace(R.id.registerFragment,loginFragment).commit()
                    activity?.finish()
//                    intent.putExtra("Forgotemail", email)
//                    intent.putExtra("Forgotpassword", password)
//                    intent.putExtra("confirmPassword", confirmPassword)
//                    startActivity(intent)
                }
            } else {
                Toast.makeText(context, "Field not filled", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}