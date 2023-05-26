package com.sdk.mockretorifitapi

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sdk.mockretorifitapi.adapter.ProductAdapter
import com.sdk.mockretorifitapi.databinding.FragmentFirstBinding
import com.sdk.mockretorifitapi.model.Product
import com.sdk.mockretorifitapi.network.RetroInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private val productAdapter by lazy { ProductAdapter() }
    private val productList = mutableListOf<Product>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rv.apply {
            adapter = productAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        callApi()
        productAdapter.onClick = {
            val bundle = bundleOf("id" to it)
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment, bundle)
        }
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        productAdapter.onLongClick = { id: Int, position: Int ->
            showDialog(id, position)
        }
    }


    private fun callApi() {
        binding.rv.isVisible = false
        binding.progressBar.isVisible = true
        RetroInstance.provideApiService().getAllProducts()
            .enqueue(object : Callback<List<Product>> {
                override fun onResponse(
                    call: Call<List<Product>>,
                    response: Response<List<Product>>
                ) {
                    if (response.isSuccessful) {
                        productList.clear()
                        binding.progressBar.isVisible = false
                        binding.rv.isVisible = true
                        productList.addAll(response.body() ?: emptyList())
                        productAdapter.submitList(productList)
                    }
                }

                override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                    Log.d("@@@", "onFailure: ${t.message}")
                }
            })
    }

    private fun showDialog(id: Int, position: Int) {
        MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle("Delete")
            setMessage("Are you sure do you want to delete this product?")
            setPositiveButton("Ok") { _, _ ->
                productList.removeAt(position)
                productAdapter.notifyItemRemoved(position)
                productAdapter.notifyItemChanged(position)
                RetroInstance.provideApiService().deleteProduct(id)
                    .enqueue(object : Callback<Product> {
                        override fun onResponse(call: Call<Product>, response: Response<Product>) {
                            Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show()
                        }

                        override fun onFailure(call: Call<Product>, t: Throwable) {

                        }
                    })
            }
            setNegativeButton("Cancel", null)
        }.create().show()
    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
}