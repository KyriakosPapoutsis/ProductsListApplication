package com.example.productslistapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// Fragment for handling user reviews
class ReviewsFragment : Fragment() {

    private lateinit var viewModel: PurchasesViewModel
    private lateinit var spinner: Spinner
    private lateinit var ratingBar: RatingBar
    private lateinit var editTextReview: EditText
    private lateinit var buttonSubmitReview: Button
    private lateinit var ratingText: TextView

    private var userPurchases: List<Purchase> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reviews, container, false)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(PurchasesViewModel::class.java)

        // Initialize views from the layout
        spinner = view.findViewById(R.id.spinnerPurchases)
        ratingBar = view.findViewById(R.id.ratingBar)
        editTextReview = view.findViewById(R.id.editTextReview)
        buttonSubmitReview = view.findViewById(R.id.buttonSubmitReview)
        ratingText = view.findViewById(R.id.ratingText) // Initialize ratingText

        // Set initial rating text to 0.0
        ratingText.text = getString(R.string.rating_value, 0.0)

        // Set up spinner, submit button, and rating bar listener
        setupSpinner()
        setupSubmitButton()
        setupRatingBarListener()

        return view
    }

    override fun onResume() {
        super.onResume()
        // Reload purchases data when the fragment resumes
        viewModel.loadPurchases()
    }

    // Set up the spinner with user's purchases
    private fun setupSpinner() {
        viewModel.purchases.observe(viewLifecycleOwner) { purchasesList ->
            userPurchases = purchasesList.filter { it.userEmail == FirebaseAuth.getInstance().currentUser?.email }
            // Extract product names for spinner
            val purchaseNames = userPurchases.map { it.productName }
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, purchaseNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }

    // Set up the submit button click listener
    private fun setupSubmitButton() {
        buttonSubmitReview.setOnClickListener {
            // Get selected purchase, rating, and review text
            val selectedPurchase = userPurchases[spinner.selectedItemPosition]
            val rating = ratingBar.rating
            val reviewText = editTextReview.text.toString().trim()

            // Check if review text is not empty
            if (reviewText.isNotEmpty()) {
                // Add review to Firestore
                addReviewToFirestore(selectedPurchase, rating, reviewText)
            } else {
                // Show toast message if review text is empty
                Toast.makeText(requireContext(), "Please write a review", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Set up listener for rating bar changes
    private fun setupRatingBarListener() {
        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            // Update the rating text dynamically
            ratingText.text = getString(R.string.rating_value, rating)
        }
    }

    // Add a review to Firestore
    private fun addReviewToFirestore(purchase: Purchase, rating: Float, reviewText: String) {
        val db = FirebaseFirestore.getInstance()
        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email

        // Create a new review document
        val review = hashMapOf(
            "productName" to purchase.productName,
            "userEmail" to currentUserEmail,
            "rating" to rating,
            "reviewText" to reviewText,
            "timestamp" to System.currentTimeMillis() // No actual use (coded in just in case)
        )

        // Add the review document to Firestore
        db.collection("reviews")
            .add(review)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(requireContext(), "Review added successfully", Toast.LENGTH_SHORT).show()
                // Clear fields and update UI
                ratingBar.rating = 0.0f
                editTextReview.text.clear()
                ratingText.text = getString(R.string.rating_value, 0.0f) // Reset rating text after submit

                // Reload purchases data after adding review
                viewModel.loadPurchases()
            }
            .addOnFailureListener { e ->
                // Show failure toast message
                Toast.makeText(requireContext(), "Failed to add review: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
