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

class PurchasesViewModel : ViewModel() {

    // Firebase Firestore instance for database interaction
    private val db = FirebaseFirestore.getInstance()

    // Current user's email obtained from FirebaseAuth
    private val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email

    private val _purchases = MutableLiveData<List<Purchase>>()
    val purchases: LiveData<List<Purchase>> get() = _purchases

    init {
        // Initialize ViewModel by loading purchases data
        loadPurchases()
    }

    // Function to load purchases data from Firestore
    fun loadPurchases() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val purchasesList = mutableListOf<Purchase>()
                // Perform Firestore query to fetch purchases for the current user
                val querySnapshot = db.collection("purchases")
                    .whereEqualTo("userEmail", currentUserEmail)
                    .get()
                    .await()

                // Process each document retrieved from the query result
                for (document in querySnapshot.documents) {
                    // Extract fields from Firestore document
                    val productName = document.getString("productName") ?: ""
                    val price = document.getDouble("price") ?: 0.0
                    val quantity = document.getLong("quantity")?.toInt() ?: 0
                    val purchaseId = document.id // Get the document ID
                    val userEmail = document.getString("userEmail") ?: ""
                    // Create a Purchase object and add it to purchasesList
                    val purchase = Purchase(productName, price, quantity, purchaseId, userEmail)
                    purchasesList.add(purchase)
                }
                // Update _purchases LiveData with the fetched purchasesList
                _purchases.postValue(purchasesList)
            } catch (e: Exception) {
                // Handle error
                Log.e(ContentValues.TAG, "Error loading documents", e)
            }
        }
    }


}


