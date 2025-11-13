package com.example.productslistapplication

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// ViewModel for handling reviews data
class ReviewsViewModel : ViewModel() {

    // Firebase Firestore instance
    private val db = FirebaseFirestore.getInstance()

    // Current user's email from Firebase Authentication (not used but coded in just in case)
    private val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email

    // LiveData to hold reviews list
    private val _reviews = MutableLiveData<List<Review>>()
    val reviews: LiveData<List<Review>> get() = _reviews

    // Function to load reviews for one specific product
    fun loadReviews(productName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val reviewsList = mutableListOf<Review>()

                // Query Firestore for reviews of the specified product
                val querySnapshot = db.collection("reviews")
                    .whereEqualTo("productName", productName)
                    .get()
                    .await()

                // Iterate through documents and create Review objects
                for (document in querySnapshot.documents) {
                    val userEmail = document.getString("userEmail") ?: ""
                    val rating = document.getDouble("rating") ?: 0.0
                    val reviewText = document.getString("reviewText") ?: ""
                    val review = Review(productName, userEmail, rating, reviewText)
                    reviewsList.add(review)
                }
                // Post the reviews list to the LiveData
                _reviews.postValue(reviewsList)
            } catch (e: Exception) {
                // Handle error
                Log.e(ContentValues.TAG, "Error loading documents", e)
            }
        }
    }
}

