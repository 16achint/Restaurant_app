package com.example.intershalaassignment.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.intershalaassignment.R

class PasswordRecoveryFragment : Fragment() {

    lateinit var otp : EditText
    lateinit var newPassword : EditText
    lateinit var confirmPassword : EditText
    lateinit var submit : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=  inflater.inflate(R.layout.fragment_password_recovery, container, false)

        otp = view.findViewById(R.id.ed_otp)
        newPassword = view.findViewById(R.id.ed_newPassword)
        confirmPassword = view.findViewById(R.id.ed_ConfirmPassword)

        return view
    }
}