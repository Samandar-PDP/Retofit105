package com.sdk.mockretorifitapi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sdk.mockretorifitapi.databinding.ItemLayoutBinding
import com.sdk.mockretorifitapi.model.Product

class ProductAdapter : ListAdapter<Product, ProductAdapter.ImageViewHolder>(DiffCallBack()) {
    lateinit var onClick: (Int) -> Unit
    lateinit var onLongClick: (Int,Int) -> Unit

    private class DiffCallBack : ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            ItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ImageViewHolder(private val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            with(binding) {
                tvName.text = product.name
                tvDate.text = product.date
                tvIndex.text = adapterPosition.plus(1).toString()
                itemView.setOnClickListener {
                    onClick(product.id)
                }
                itemView.setOnLongClickListener {
                    onLongClick(product.id,adapterPosition)
                    true
                }
            }
        }
    }
}