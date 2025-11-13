package com.example.productslistapplication

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class WishlistFragment : Fragment() {

    private lateinit var recyclerViewWishlist: RecyclerView
    private lateinit var wishlistAdapter: ProductAdapter
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wishlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize RecyclerView and set its layout manager
        recyclerViewWishlist = view.findViewById(R.id.recyclerViewWishlist)
        recyclerViewWishlist.layoutManager = LinearLayoutManager(requireContext())

        // Load wishlist items from Firestore
        loadWishlistItems()
    }

    private fun loadWishlistItems() {
        // Getting the current user's email from FirebaseAuth
        val userEmail = FirebaseAuth.getInstance().currentUser?.email

        // Query Firestore for wishlist items belonging to the currently logged in user
        db.collection("wishlist")
            .whereEqualTo("userEmail", userEmail)
            .get()
            .addOnSuccessListener { documents ->
                // Process the documents to create Product objects
                val products = documents.map { document ->
                    Product(
                        name = document.getString("productName") ?: "",
                        description = document.getString("description") ?: "",
                        price = document.getDouble("price") ?: 0.0,
                        discount = (document.getLong("discount") ?: 0L).toInt(),
                        imageResId = (document.getLong("imageResId") ?: 0L).toInt(),
                        reviews = document.getString("reviews") ?: "",
                        specifications = document.getString("specifications") ?: "",
                        size = document.getString("size") ?: "",
                        color = document.getString("color") ?: "",
                        availability = (document.getLong("availability") ?: 0L).toInt()
                    )
                }
                // Initialize the adapter with the retrieved products
                wishlistAdapter = ProductAdapter(products, showWishButton = false)
                recyclerViewWishlist.adapter = wishlistAdapter
            }
            .addOnFailureListener { e ->
                // Handle the error
                Log.e(ContentValues.TAG, "Error loading documents", e)

            }
    }
}
