package com.example.blinkit_adminside.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.blinkit_adminside.R
import com.example.blinkit_adminside.adapter.AdapterProduct
import com.example.blinkit_adminside.adapter.CategoriesAdapter
import com.example.blinkit_adminside.databinding.FragmentHomeBinding
import com.example.blinkit_adminside.models.Category
import com.example.blinkit_adminside.object_class.Constant
import com.example.blinkit_adminside.viewmodel.AdminViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    val viewMode :AdminViewModel by viewModels()
    private  lateinit var  binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentHomeBinding.inflate(inflater,container,false)
        setStatusBarColor()
        setCategories()
        getAllTheProducts("All")
        return binding.root
    }

    private fun getAllTheProducts(category: String?) {
        lifecycleScope.launch {
            viewMode.fetchAllTheProducts(category).collect{
                if (it.isEmpty()){
                    binding.recyclerviewProducts.visibility=View.GONE
                    binding.tvText.visibility =View.VISIBLE
                }
                else{
                    binding.recyclerviewProducts.visibility=View.VISIBLE
                    binding.tvText.visibility =View.GONE
                }
                val  adapterProduct = AdapterProduct()
                binding.recyclerviewProducts.adapter=adapterProduct
                adapterProduct.differ.submitList(it)
            }
        }
    }


    private fun setCategories() {
        val  categoryList = ArrayList<Category>()
        for (i in 0 until  Constant.allProductsCategoryIcon.size){
            categoryList.add(Category(Constant.allProductsCategory[i],Constant.allProductsCategoryIcon[i]))
        }
        binding.recyclerviewCategories.adapter=CategoriesAdapter(categoryList,::onCategoryClick)
    }


    private  fun onCategoryClick(category: Category){
        getAllTheProducts(category.category)
    }

    private fun setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity?.window?.apply {
                statusBarColor = ContextCompat.getColor(requireContext(), R.color.yellow)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
            }
        }
    }
}
