package com.example.productslistapplication

// Data class representing a purchase
data class Purchase(
    val productName: String,
    val price: Double,
    val quantity: Int,
    val purchaseId: String,
    val userEmail: String
)
