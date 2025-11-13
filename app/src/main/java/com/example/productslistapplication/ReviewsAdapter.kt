package com.example.productslistapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Adapter for managing and displaying a list of reviews in a RecyclerView
class ReviewsAdapter : RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>() {

    // List of reviews to be displayed
    private var reviews: List<Review> = listOf()

    // Inflates the item layout and returns a new instance of ReviewViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(view)
    }

    // Binds data to the ReviewViewHolder at the specified position
    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(reviews[position])
    }

    // Returns the total number of items in the dataset (just in case)
    override fun getItemCount(): Int {
        return reviews.size
    }

    // Submits a new list of reviews to be displayed and notifies the adapter of the change
    fun submitList(reviews: List<Review>) {
        this.reviews = reviews
        notifyDataSetChanged()
    }

    // ViewHolder class that holds references to the views for each review item
    class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Views are in the item_review layout
        private val textViewProductName: TextView = itemView.findViewById(R.id.textViewProductName)
        private val textViewUserEmail: TextView = itemView.findViewById(R.id.textViewUserEmail)
        private val textViewRating: TextView = itemView.findViewById(R.id.textViewRating)
        private val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
        private val textViewReviewText: TextView = itemView.findViewById(R.id.textViewReviewText)

        // Binds review data to the views
        fun bind(review: Review) {
            textViewProductName.text = "Product: ${review.productName}"
            textViewUserEmail.text = "User: ${review.userEmail}"
            textViewRating.text = "Rating: ${String.format("%.1f", review.rating)}"
            ratingBar.rating = review.rating.toFloat()
            textViewReviewText.text = "Comment: ${review.reviewText}"
        }
    }
}




