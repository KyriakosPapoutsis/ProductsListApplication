package com.example.productslistapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.activity.viewModels
import com.example.productslistapplication.ProductViewModel

class HomeActivity : AppCompatActivity() {

    private val viewModel: ProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Initializing the BottomNavigationView
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        // Getting the NavController for navigation handling
        val navController = findNavController(R.id.nav_host_fragment)

        // Configuring the AppBarConfiguration with the set of destinations
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.productCatalogFragment,
                R.id.purchasesListFragment,
                R.id.wishlistFragment,
                R.id.reviewsFragment,
                R.id.profileFragment
            )
        )
        // Set up the BottomNavigationView with the NavController
        navView.setupWithNavController(navController)

        // Call a method in the ViewModel to save products to Firestore if not already saved
        viewModel.saveProductsToFirestoreIfNotSaved(applicationContext)

    }
}
