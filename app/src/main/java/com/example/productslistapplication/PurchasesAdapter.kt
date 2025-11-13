package com.example.productslistapplication

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.slider.Slider
import com.google.firebase.firestore.FirebaseFirestore


class PurchasesAdapter :
    ListAdapter<Purchase, PurchasesAdapter.PurchaseViewHolder>(PurchaseDiffCallback()) {

    // Create ViewHolders
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchaseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_purchase, parent, false)
        return PurchaseViewHolder(view)
    }

    // Bind data to ViewHolders
    override fun onBindViewHolder(holder: PurchaseViewHolder, position: Int) {
        val purchase = getItem(position)
        holder.bind(purchase)
    }

    // ViewHolder class to hold references to UI elements
    class PurchaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewPurchaseName: TextView = itemView.findViewById(R.id.textViewPurchaseName)
        private val textViewPurchaseQuantity: TextView = itemView.findViewById(R.id.textViewPurchaseQuantity)
        private val btnUpdate: Button = itemView.findViewById(R.id.buttonUpdate)
        private val btnDelete: Button = itemView.findViewById(R.id.buttonDelete)
        private val sliderQuantity: Slider = itemView.findViewById(R.id.sliderQuantity)

        // Bind data to UI elements
        fun bind(purchase: Purchase) {
            textViewPurchaseName.text = purchase.productName
            textViewPurchaseQuantity.text = "Quantity: ${purchase.quantity}"
            sliderQuantity.value = purchase.quantity.toFloat()

            // Update button click listener
            btnUpdate.setOnClickListener {
                val db = FirebaseFirestore.getInstance()
                val newQuantity = sliderQuantity.value.toInt() // Get the new quantity from the slider
                val purchaseId = purchase.purchaseId

                // Update quantity in Firestore
                db.collection("purchases")
                    .document(purchaseId)
                    .update("quantity", newQuantity)
                    .addOnSuccessListener {
                        // Update successful
                        textViewPurchaseQuantity.text = "Quantity: $newQuantity" // Update displayed quantity
                    }
                    .addOnFailureListener { e ->
                        // Handle failure
                        Log.e(TAG, "Error updating document", e)
                    }
            }

            // Delete button click listener
            btnDelete.setOnClickListener {
                val db = FirebaseFirestore.getInstance()
                val purchaseId = purchase.purchaseId

                // Delete document from Firestore
                db.collection("purchases")
                    .document(purchaseId)
                    .delete()
                    // Remove the product from the screen
                    .addOnSuccessListener {
                        textViewPurchaseName.visibility = View.GONE
                          textViewPurchaseQuantity.visibility = View.GONE
                          btnUpdate.visibility = View.GONE
                          btnDelete.visibility = View.GONE
                          sliderQuantity.visibility = View.GONE

                    }
                    .addOnFailureListener { e ->
                        // Handle failure
                        Log.e(TAG, "Error deleting document", e)
                    }
            }



        }
    }

    // DiffCallback for efficient updates when the dataset changes
    class PurchaseDiffCallback : DiffUtil.ItemCallback<Purchase>() {
        override fun areItemsTheSame(oldItem: Purchase, newItem: Purchase): Boolean {
            return oldItem.productName == newItem.productName
        }

        override fun areContentsTheSame(oldItem: Purchase, newItem: Purchase): Boolean {
            return oldItem == newItem
        }
    }
}
