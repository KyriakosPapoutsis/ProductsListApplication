package com.example.productslistapplication

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.SearchView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProductCatalogFragment : Fragment() {

    // Declaring ViewModel, Adapter, and UI components
    private lateinit var productViewModel: ProductViewModel
    private lateinit var productAdapter: ProductAdapter
    private lateinit var searchView: SearchView
    private lateinit var filterSpinner: Spinner
    private lateinit var priceRangeSpinner: Spinner
    private lateinit var brandSpinner: Spinner
    private val db = FirebaseFirestore.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_product_catalog, container, false)

        // Initialize ViewModel
        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        searchView = view.findViewById(R.id.searchView)
        filterSpinner = view.findViewById(R.id.filterSpinner)
        priceRangeSpinner = view.findViewById(R.id.priceRangeSpinner)
        brandSpinner = view.findViewById(R.id.brandSpinner)

        // Set RecyclerView layout manager
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Observe products and set adapter
        productViewModel.products.observe(viewLifecycleOwner, Observer {
            productAdapter = ProductAdapter(
                it,
                showWishButton = true
            ) // send showWishButton as true for it to be visible
            recyclerView.adapter = productAdapter

        })
        productViewModel.products.observe(viewLifecycleOwner, Observer { products ->
            loadWishlistData(products)
        })

        // SearchView is expanded
        searchView.setIconifiedByDefault(false) // Ensure the SearchView is expanded

        // Set SearchView query listeners
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                productViewModel.filterProducts(
                    query.orEmpty(),
                    filterSpinner.selectedItem.toString()
                )
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                productViewModel.filterProducts(
                    newText.orEmpty(),
                    filterSpinner.selectedItem.toString()
                )
                return true
            }
        })

        // Set filterSpinner item selected listener
        filterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                productViewModel.filterProducts(
                    searchView.query.toString(),
                    filterSpinner.selectedItem.toString()
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Set priceRangeSpinner item selected listener
        priceRangeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                productViewModel.filterProductsPrice(
                    searchView.query.toString(),
                    priceRangeSpinner.selectedItem.toString()
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Set brandSpinner item selected listener
        brandSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                productViewModel.filterProductsBrand(
                    searchView.query.toString(),
                    brandSpinner.selectedItem.toString()
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


        return view
    }

    // Load wishlist data for the currently logged-in user and update products list
    private fun loadWishlistData(products: List<Product>) {
        val userEmail = FirebaseAuth.getInstance().currentUser?.email

        // Query wishlist items for the current user from Firestore
        db.collection("wishlist")
            .whereEqualTo("userEmail", userEmail)
            .get()
            .addOnSuccessListener { wishlistDocuments ->
                val wishlistProductNames = wishlistDocuments.map { it.getString("productName") }

                // Update products list with wishlist status
                products.forEach { product ->
                    if (wishlistProductNames.contains(product.name)) {
                        product.inWishlist = true

                    } else {
                        product.inWishlist = false
                    }
                }


            }
            .addOnFailureListener { e ->
                // Handle failure
                Log.e(ContentValues.TAG, "Error getting documents", e)
            }
    }


}

