package com.example.productslistapplication

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.slider.Slider
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProductAdapter(private var products: List<Product>, private val showWishButton: Boolean) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    // Firestore instance
    private val db = FirebaseFirestore.getInstance()

    // Create a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    // Bind the ViewHolder with product data
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product)

        // Handle Buy button click
        holder.btnBuy.setOnClickListener {
            onBuyButtonClick(product, holder.sliderQuantity.value.toInt(), holder.itemView)
        }

        // Handle click on btnWish Image Button or btnWishText TextView (together as a 'Add To Wishlist' button)
        // If clause to determine showing the Wishlist button or not (not showing it in Wishlist Fragment)
        if (showWishButton) {
            holder.btnWish.visibility = View.VISIBLE
            holder.btnWish.setOnClickListener {
                if (product.inWishlist) {
                    removeFromWishlist(product, holder.itemView)
                    setWishlistButtonState(holder.btnWish, product.inWishlist)
                } else {
                    addToWishlist(product, holder.itemView)
                    setWishlistButtonState(holder.btnWish, product.inWishlist)
                }
            }
            holder.btnWishText.setOnClickListener {
                if (product.inWishlist) {
                    removeFromWishlist(product, holder.itemView)
                    setWishlistButtonState(holder.btnWish, product.inWishlist)
                } else {
                    addToWishlist(product, holder.itemView)
                    setWishlistButtonState(holder.btnWish, product.inWishlist)
                }
            }
        } else {
            holder.btnWish.visibility = View.GONE
            holder.btnWishText.visibility = View.GONE

            // Not showing btnReviews and btnReviewsText (the 'Read Reviews' button) in Wishlist Fragment
            holder.btnReviews.visibility = View.GONE
            holder.btnReviewsText.visibility = View.GONE
        }

        // Handle click on btnReviews Image Button or btnReviewsText TextView (together as a 'Read Reviews' button)
        holder.btnReviews.setOnClickListener {
            val context = holder.itemView.context
            val productName = product.name
            val bundle = Bundle().apply {
                putString("productName", productName)
            }
            val navController = Navigation.findNavController(holder.itemView)
            navController.navigate(R.id.action_productCatalogFragment_to_readReviewsFragment, bundle)
        }
        holder.btnReviewsText.setOnClickListener {
            val context = holder.itemView.context
            val productName = product.name
            val bundle = Bundle().apply {
                putString("productName", productName)
            }
            val navController = Navigation.findNavController(holder.itemView)
            navController.navigate(R.id.action_productCatalogFragment_to_readReviewsFragment, bundle)
        }

    }

    override fun getItemCount() = products.size

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Initialize UI elements
        private val imageViewProduct: ImageView = itemView.findViewById(R.id.imageViewProduct)
        private val textViewName: TextView = itemView.findViewById(R.id.textViewName)
        private val textViewDescription: TextView = itemView.findViewById(R.id.textViewDescription)
        private val textViewPrice: TextView = itemView.findViewById(R.id.textViewPrice)
        private val textViewDiscount: TextView = itemView.findViewById(R.id.textViewDiscount)
        private val textViewReviews: TextView = itemView.findViewById(R.id.textViewReviews)
        private val textViewSpecifications: TextView = itemView.findViewById(R.id.textViewSpecifications)
        private val textViewSize: TextView = itemView.findViewById(R.id.textViewSize)
        private val textViewColor: TextView = itemView.findViewById(R.id.textViewColor)
        private val textViewAvailability: TextView = itemView.findViewById(R.id.textViewAvailability)
        val btnBuy: Button = itemView.findViewById(R.id.btnBuy)
        val sliderQuantity: Slider = itemView.findViewById(R.id.sliderQuantity)
        val btnWish: ImageButton = itemView.findViewById(R.id.btnWish)
        val btnWishText: TextView = itemView.findViewById(R.id.btnWishText)
        val btnReviews: ImageButton = itemView.findViewById(R.id.btnReviews)
        val btnReviewsText: TextView = itemView.findViewById(R.id.btnReviewsText)

        // Bind product data to UI elements
        fun bind(product: Product) {
            textViewName.text = product.name
            textViewDescription.text = product.description
            textViewPrice.text = "Price: $${product.price}"
            textViewDiscount.text = "-${product.discount}% Discount"
            textViewReviews.text = "Reviews: ${product.reviews}"
            textViewSpecifications.text = "Specifications: ${product.specifications}"
            textViewSize.text = "Size: ${product.size}"
            textViewColor.text = "Color: ${product.color}"
            textViewAvailability.text = "Availability: ${product.availability}"

            Glide.with(itemView.context)
                .load(product.imageResId)
                .into(imageViewProduct)
        }
    }
    // Handle Buy button (or 'Add To List' button) click event
    private fun onBuyButtonClick(product: Product, quantity: Int, view: View) {
        val userEmail = FirebaseAuth.getInstance().currentUser?.email

        // Prepare purchase data
        val purchase = hashMapOf(
            "userEmail" to userEmail,
            "productName" to product.name,
            "price" to product.price,
            "quantity" to quantity
        )

        // Add purchase to Firestore
        db.collection("purchases")
            .add(purchase)
            .addOnSuccessListener {
                // Show success message
                showSnackbar(view, "Added To List Successfully!", "OK")
            }
            .addOnFailureListener { e ->
                // Show error message
                showSnackbar(view, "Error: ${e.message}", "OK")
            }
    }

    // Add product to wishlist
    private fun addToWishlist(product: Product, view: View) {
        val userEmail = FirebaseAuth.getInstance().currentUser?.email
        product.inWishlist = true
        // Prepare wishlist data
        val wishlistItem = hashMapOf(
            "userEmail" to userEmail,
            "productName" to product.name,
            "imageResId" to product.imageResId,
            "description" to product.description,
            "price" to product.price,
            "discount" to product.discount,
            "reviews" to product.reviews,
            "specifications" to product.specifications,
            "size" to product.size,
            "color" to product.color,
            "availability" to product.availability,
            "inWishlist" to product.inWishlist
        )

        // Add wishlist item to Firestore
        db.collection("wishlist")
            .add(wishlistItem)
            .addOnSuccessListener {
                // Show success message
                showSnackbar(view, "Added To Wishlist Successfully!", "OK")
            }
            .addOnFailureListener { e ->
                // Show error message
                showSnackbar(view, "Error: ${e.message}", "OK")
            }
    }

    // Remove product from wishlist
    private fun removeFromWishlist(product: Product, view: View) {
        product.inWishlist = false
        val db = FirebaseFirestore.getInstance()
        val userEmail = FirebaseAuth.getInstance().currentUser?.email

        // Query Firestore to find the document with productName and userEmail
        db.collection("wishlist")
            .whereEqualTo("productName", product.name)
            .whereEqualTo("userEmail", userEmail)
            .get()
            .addOnSuccessListener { documents ->
                // Iterate through the result (supposed to be one single document)
                for (document in documents) {
                    // Delete the document
                    db.collection("wishlist")
                        .document(document.id)
                        .delete()
                        .addOnSuccessListener {
                            // Show success message
                            showSnackbar(view, "Removed From Wishlist!", "OK")
                        }
                        .addOnFailureListener { e ->
                            // Handle failure
                            Log.e(ContentValues.TAG, "Error deleting document", e)
                        }
                }
            }
            .addOnFailureListener { e ->
                // Handle failure
                Log.e(ContentValues.TAG, "Error getting documents", e)
            }
    }

    // Show a Snackbar message
    private fun showSnackbar(view: View, message: String, actionText: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
            .setAction(actionText) {}
            .show()
    }

    // Set the state of the wishlist button (when clicked, it changes to ic_heart_filled -> yellow color)
    fun setWishlistButtonState(button: ImageButton, inWishlist: Boolean) {
        if (inWishlist) {
            button.setImageResource(R.drawable.ic_heart_filled) // Change to filled icon
        } else {
            button.setImageResource(R.drawable.ic_heart_outline) // Change to outline icon
        }
    }
}
