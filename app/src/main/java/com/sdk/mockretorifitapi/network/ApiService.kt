package com.sdk.mockretorifitapi.network

import com.sdk.mockretorifitapi.model.Product
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @GET("products")
    fun getAllProducts(): Call<List<Product>>

    @GET("products/{id}")
    fun getProductById(
        @Path("id") id: Int
    ): Call<Product>

    @DELETE("products/{id}")
    fun deleteProduct(
        @Path("id") id: Int
    ): Call<Product>

    @POST("products")
    fun createProduct(
        @Body product: Product
    ): Call<Product>

    @PUT("products/{id}")
    fun updateProduct(
        @Path("id") id: Int,
        @Body product: Product
    ): Call<Product>
}