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

class PurchasesListFragment : Fragment() {

    private lateinit var viewModel: PurchasesViewModel
    private lateinit var purchasesAdapter: PurchasesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_purchases_list, container, false)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(PurchasesViewModel::class.java)

        // Initialize RecyclerView and adapter
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewPurchases)
        purchasesAdapter = PurchasesAdapter()
        recyclerView.adapter = purchasesAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Set up observer for LiveData
        viewModel.purchases.observe(viewLifecycleOwner, Observer { purchases ->
            purchasesAdapter.submitList(purchases)
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Reload purchases when the fragment is shown or clicked
        loadPurchases()
    }

    // Load Purchases from the PurchasesViewModel
    private fun loadPurchases() {
        viewModel.loadPurchases()
    }
}
