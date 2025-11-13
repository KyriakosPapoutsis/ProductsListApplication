package com.example.productslistapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ReadReviewsFragment : Fragment() {

    private lateinit var viewModel: ReviewsViewModel
    private lateinit var reviewsAdapter: ReviewsAdapter
    private lateinit var productName: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_read_reviews, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get productName from arguments
        arguments?.let {
            productName = it.getString("productName", "")
        }

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(ReviewsViewModel::class.java)

        // Initialize RecyclerView and adapter
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewReadReviews)
        reviewsAdapter = ReviewsAdapter()
        recyclerView.adapter = reviewsAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Setting up observer for LiveData
        viewModel.reviews.observe(viewLifecycleOwner, Observer { reviews ->
            reviewsAdapter.submitList(reviews)
        })

        // Load Reviews from the ReviewsViewModel
        viewModel.loadReviews(productName)
    }
}


