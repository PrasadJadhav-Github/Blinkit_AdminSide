package com.example.blinkit_adminside.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.blinkit_adminside.databinding.ItemViewImageSelectorBinding

class AdapterSelectedImages(val imageUris: ArrayList<Uri>) : RecyclerView.Adapter<AdapterSelectedImages.SelectedImagesViewHolder>() {

    class SelectedImagesViewHolder(val binding: ItemViewImageSelectorBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedImagesViewHolder {
        return  SelectedImagesViewHolder(ItemViewImageSelectorBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    }

    override fun getItemCount(): Int {
        return imageUris.size
    }

    override fun onBindViewHolder(holder: SelectedImagesViewHolder, position: Int) {
        val image = imageUris[position]
        holder.binding.apply {
            ivImage.setImageURI(image)
        }

        holder.binding.closeButton.setOnClickListener {
            if (position < imageUris.size){
                imageUris.removeAt(position)
                notifyItemRemoved(position)
            }
        }
    }
}
