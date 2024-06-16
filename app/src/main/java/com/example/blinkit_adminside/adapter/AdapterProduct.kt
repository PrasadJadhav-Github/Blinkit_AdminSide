package com.example.blinkit_adminside.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.denzcoskun.imageslider.models.SlideModel
import com.example.blinkit_adminside.databinding.ItemViewProductBinding
import com.example.blinkit_adminside.models.Products



class AdapterProduct(
    val onEditButtonClick: (Products) -> Unit) :RecyclerView.Adapter<AdapterProduct.ProductViewHolder>() {
    class ProductViewHolder(val binding: ItemViewProductBinding) : ViewHolder(binding.root)

    val diffutil =object  :DiffUtil.ItemCallback<Products>(){
        override fun areItemsTheSame(oldItem: Products, newItem: Products): Boolean {
            return oldItem.productRandomId == newItem.productRandomId
        }

        override fun areContentsTheSame(oldItem: Products, newItem: Products): Boolean {
            return oldItem == newItem
        }
    }

    val  differ = AsyncListDiffer(this,diffutil)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(ItemViewProductBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return  differ.currentList.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.binding.apply {
            val imageList = ArrayList<SlideModel>()
            val productImages = product.productImagesUris
            if (productImages != null) {
                for (i in productImages.indices) {
                    imageList.add(SlideModel(productImages[i].toString()))
                }
                ivImageSlider.setImageList(imageList)
            }
            textviewProductTitle.text = product.productTitle
            val quantity = product.productQuantity.toString() + product.productUnit
            textviewProductQuantity.text = quantity
            textviewProductPrice.text = "₹" + product.productPrice
        }
        holder.itemView.setOnClickListener {
            onEditButtonClick(product)
        }
    }

}
