package com.example.productslistapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var tvUserEmail: TextView
    private lateinit var btnLogout: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Initializing Firebase Authentication instance
        auth = FirebaseAuth.getInstance()

        // Bind views from layout
        tvUserEmail = view.findViewById(R.id.tvUserEmail)
        btnLogout = view.findViewById(R.id.btnLogout)

        // Fetch currently logged-in user and display their email
        val currentUser = auth.currentUser
        currentUser?.let {
            tvUserEmail.text = it.email
        }

        // Logout button click listener
        btnLogout.setOnClickListener {
            // Sign out user from Firebase Authentication
            auth.signOut()

            // Redirect to MainActivity with Intent to handle login/signup functionalities
            val intent = Intent(activity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        return view
    }
}

