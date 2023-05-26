package com.sdk.mockretorifitapi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.sdk.mockretorifitapi.databinding.FragmentSecondBinding
import com.sdk.mockretorifitapi.model.Product
import com.sdk.mockretorifitapi.network.RetroInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    private var id: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = arguments?.getInt("id")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (id != null) {
            binding.btn.isVisible = false
            binding.btn.text = getString(R.string.update)
            (requireActivity() as MainActivity).supportActionBar?.title =
                getString(R.string.update_product)
            RetroInstance.provideApiService().getProductById(id!!)
                .enqueue(object : Callback<Product> {
                    override fun onResponse(call: Call<Product>, response: Response<Product>) {
                        if (response.isSuccessful) {
                            val product = response.body()
                            binding.apply {
                                btn.isVisible = true
                                name.setText(product?.name)
                                price.setText(product?.price.toString())
                                category.setText(product?.category)
                            }
                        }
                    }

                    override fun onFailure(call: Call<Product>, t: Throwable) {

                    }
                })
        }

        binding.btn.setOnClickListener {
            binding.btn.isVisible = false
            val name = binding.name.text.toString().trim()
            val price = binding.price.text.toString().trim().toInt()
            val category = binding.category.text.toString().trim()
            val date = SimpleDateFormat("HH:MM:SS yyyy-MM-dd", Locale.getDefault()).format(Date())
            createOrUpdate(name, price, category, date)
        }
    }

    private fun createOrUpdate(name: String, price: Int, category: String, date: String) {
        if (id != null) {
            RetroInstance.provideApiService().updateProduct(
                id!!,
                Product(
                    id!!, name, price, category, date
                )
            ).enqueue(object : Callback<Product> {
                override fun onResponse(call: Call<Product>, response: Response<Product>) {
                    if (response.isSuccessful) {
                        binding.btn.isVisible = true
                        Toast.makeText(requireContext(), "Updated", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Product>, t: Throwable) {

                }
            })
        } else {
            RetroInstance.provideApiService().createProduct(
                Product(
                    name = name,
                    price = price,
                    category = category,
                    date = date
                )
            ).enqueue(object : Callback<Product> {
                override fun onResponse(call: Call<Product>, response: Response<Product>) {
                    if (response.isSuccessful) {
                        binding.btn.isVisible = true
                        findNavController().popBackStack()
                        Toast.makeText(requireContext(), "Created", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Product>, t: Throwable) {
                    Toast.makeText(requireContext(), "Error occurred", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}