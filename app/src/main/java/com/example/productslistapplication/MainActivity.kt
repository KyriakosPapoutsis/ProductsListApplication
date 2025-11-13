package com.example.productslistapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    // Firebase Authentication instance
    private lateinit var auth: FirebaseAuth

    // UI elements
    private lateinit var btnRegister: Button
    private lateinit var btnLogin: Button
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText

    // Firestore users collection reference
    private val userCollectionRef = Firebase.firestore.collection("users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initializing Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Binding UI elements
        btnRegister = findViewById(R.id.btnRegister)
        btnLogin = findViewById(R.id.btnLogin)
        etEmail = findViewById(R.id.etMail)
        etPassword = findViewById(R.id.etPassword)

        // Sign out any existing user
        auth.signOut()

        // Set onClickListener for the register button
        btnRegister.setOnClickListener {
            registerUser()
        }

        // Set onClickListener for the login button
        btnLogin.setOnClickListener {
            loginUser()
        }
    }

    // Function to register a new user
    private fun registerUser() {
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()
        Log.d("Auth", "Attempting to register user")
        Log.d("Auth", "Attempting to login user")

        // Hide the keyboard so that the snackbar can be shown
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(etEmail.windowToken, 0)

        if (email.isNotEmpty() && password.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.createUserWithEmailAndPassword(email, password).await()
                    // Save user data to database
                    saveDataToDb(User(email))

                    // Show success message on the screen
                    withContext(Dispatchers.Main) {
                        Snackbar.make(
                            findViewById(android.R.id.content),
                            getString(R.string.registration_success_message),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                } catch (e: Exception) {
                    // Show error message
                    withContext(Dispatchers.Main) {
                        Snackbar.make(
                            findViewById(android.R.id.content),
                            e.message ?: "Registration Failed",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    // Function to log in an existing user
    private fun loginUser() {
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()
        Log.e("My Message", "$email $password")
        if (email.isNotEmpty() && password.isNotEmpty()) {
            if (auth.currentUser == null) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        // Sign in with email and password
                        auth.signInWithEmailAndPassword(email, password).await()
                        withContext(Dispatchers.Main) {
                            // Navigate to HomeActivity on successful login with Intent
                            val intent = Intent(this@MainActivity, HomeActivity::class.java)
                            startActivity(intent)
                            finish() // Close MainActivity
                        }
                    } catch (e: Exception) {
                        // Show error message
                        withContext(Dispatchers.Main) {
                            Snackbar.make(
                                findViewById(android.R.id.content),
                                e.message ?: "Login Failed",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            } else {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Somebody is already registered",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }
    // Function to save user data to Firestore
    private fun saveDataToDb(user: User) {
        CoroutineScope(Dispatchers.IO).launch {
            userCollectionRef.add(user).await()
        }
    }
}
