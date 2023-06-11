package com.example.intershalaassignment.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.intershalaassignment.R
import com.example.intershalaassignment.activity.MainActivity

class SuccessScreenFragment : Fragment() {
    lateinit var btnOk : Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_success_screen, container, false)
        btnOk = view.findViewById(R.id.btnOk)
        btnOk.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
        }
        return view
    }
}