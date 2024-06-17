package com.example.blinkit_adminside.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.blinkit_adminside.R
import com.example.blinkit_adminside.adapter.AdapterProduct
import com.example.blinkit_adminside.adapter.CategoriesAdapter
import com.example.blinkit_adminside.databinding.EditProductLayoutBinding
import com.example.blinkit_adminside.databinding.FragmentHomeBinding
import com.example.blinkit_adminside.models.Category
import com.example.blinkit_adminside.models.Products
import com.example.blinkit_adminside.object_class.Constant
import com.example.blinkit_adminside.object_class.Utils
import com.example.blinkit_adminside.viewmodel.AdminViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    val viewMode: AdminViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setStatusBarColor()
        setCategories()
        getAllTheProducts("All")
        return binding.root
    }

    private fun getAllTheProducts(category: String?) {
        binding.shimmerViewContainer.visibility = View.VISIBLE
        lifecycleScope.launch {
            viewMode.fetchAllTheProducts(category).collect {
                if (it.isEmpty()) {
                    binding.recyclerviewProducts.visibility = View.GONE
                    binding.tvText.visibility = View.VISIBLE
                } else {
                    binding.recyclerviewProducts.visibility = View.VISIBLE
                    binding.tvText.visibility = View.GONE
                }
                val adapterProduct = AdapterProduct(::onEditButtonClick)
                binding.recyclerviewProducts.adapter = adapterProduct
                adapterProduct.differ.submitList(it)

                binding.shimmerViewContainer.visibility = View.GONE

            }
        }
    }


    private fun setCategories() {
        val categoryList = ArrayList<Category>()
        for (i in 0 until Constant.allProductsCategoryIcon.size) {
            categoryList.add(
                Category(
                    Constant.allProductsCategory[i],
                    Constant.allProductsCategoryIcon[i]
                )
            )
        }
        binding.recyclerviewCategories.adapter = CategoriesAdapter(categoryList, ::onCategoryClick)
    }


    private fun onCategoryClick(category: Category) {
        getAllTheProducts(category.category)
    }

    private fun onEditButtonClick(products: Products) {
        val editProduct = EditProductLayoutBinding.inflate(LayoutInflater.from(requireContext()))
        editProduct.apply {
            editTextProductTitle.setText(products.productTitle)
            editTextProductQuantity.setText(products.productQuantity.toString())
            editTextProductUnit.setText(products.productUnit)
            editTextProductPrice.setText(products.productPrice.toString())
            editTextProductStock.setText(products.productStock.toString())
            editTextProductCategory.setText(products.productCategory)
            editTextProductType.setText(products.prooductType)
        }
        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(editProduct.root)
            .create()
        alertDialog.show()

        editProduct.btnEditProduct.setOnClickListener {
            editProduct.apply {
                editTextProductTitle.isEnabled = true
                editTextProductUnit.isEnabled = true
                editTextProductPrice.isEnabled = true
                editTextProductStock.isEnabled = true
                editTextProductCategory.isEnabled = true
                editTextProductType.isEnabled = true
            }
        }
        setAutoCompleteTextViews(editProduct)

        editProduct.btnSaveProduct.setOnClickListener{
            lifecycleScope.launch {
                products.productTitle = editProduct.editTextProductTitle.text.toString()
                products.productQuantity =editProduct.editTextProductQuantity.text.toString().toInt()
                products.productUnit =editProduct.editTextProductUnit.text.toString()
                products.productPrice =editProduct.editTextProductPrice.text.toString().toInt()
                products.productStock =editProduct.editTextProductStock.text.toString().toInt()
                products.productCategory=editProduct.editTextProductCategory.text.toString()
                products.prooductType=editProduct.editTextProductPrice.text.toString()
                viewMode.saveUpdatedProducts(products)
            }
            Utils.showToast(requireContext(),"Saved changes!")
            alertDialog.dismiss()
        }
    }

    private fun setAutoCompleteTextViews(editProduct: EditProductLayoutBinding) {

        val units =
            ArrayAdapter(requireContext(),
                R.layout.show_item_list,
                Constant.allUnitsOfProduct)

        val category =
            ArrayAdapter(requireContext(),
                R.layout.show_item_list,
                Constant.allProductsCategory)
        val productType =
            ArrayAdapter(requireContext(),
                R.layout.show_item_list,
                Constant.allProductTypes)

        editProduct.apply {
            editTextProductUnit.setAdapter(units)
            editTextProductCategory.setAdapter(category)
            editTextProductType.setAdapter(productType)
        }
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