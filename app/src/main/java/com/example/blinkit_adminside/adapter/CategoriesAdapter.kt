package com.example.blinkit_adminside.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.blinkit_adminside.databinding.ItemViewProductCategoriesBinding
import com.example.blinkit_adminside.models.Category

class CategoriesAdapter(
    private val categoryArrayList: ArrayList<Category>,
    val onCategoryClick: (Category) -> Unit,
): RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>(){
    class CategoriesViewHolder (val binding : ItemViewProductCategoriesBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        return  CategoriesViewHolder(ItemViewProductCategoriesBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return categoryArrayList.size
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val category=categoryArrayList[position]
        holder.binding.apply {
            imageviewCategoryImage.setImageResource(category.icon)
            textviewCategoryTitle.text=category.category

        }
        holder.itemView.setOnClickListener {
            onCategoryClick(category)
        }
    }
}
