package com.example.blinkit_adminside.fragments

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.blinkit_adminside.object_class.Constant
import com.example.blinkit_adminside.R
import com.example.blinkit_adminside.activities.AdminMainActivity
import com.example.blinkit_adminside.adapter.AdapterSelectedImages
import com.example.blinkit_adminside.databinding.FragmentAddProductBinding
import com.example.blinkit_adminside.models.Products
import com.example.blinkit_adminside.object_class.Utils
import com.example.blinkit_adminside.viewmodel.AdminViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AddProductFragment : Fragment() {
    private val viewModel: AdminViewModel by viewModels()
    private lateinit var binding: FragmentAddProductBinding
    private val imageUris: ArrayList<Uri> = arrayListOf()
    private val selectedImages =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { listOfUri ->
            val fiveImages = listOfUri.take(5)
            imageUris.clear()
            imageUris.addAll(fiveImages)
            binding.recyclerViewProductImages.adapter = AdapterSelectedImages(imageUris)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddProductBinding.inflate(inflater, container, false)
        setStatusBarColor()
        onImageSelectClick()
        setAutoCompleteTextViews()
        onAddButtonClick()

        return binding.root
    }

    private fun onAddButtonClick() {
        binding.btnAddProduct.setOnClickListener {
            Utils.showDialog(requireContext(), "Uploading Images...")
            val productTitle = binding.editTextProductTitle.text.toString()
            val productQuantity = binding.editTextProductQuantity.text.toString()
            val productUnit = binding.editTextProductUnit.text.toString()
            val productPrice = binding.editTextProductPrice.text.toString()
            val productStock = binding.editTextProductStock.text.toString()
            val productCategory = binding.editTextProductCategory.text.toString()
            val productType = binding.editTextProductType.text.toString()

            if (productTitle.isEmpty() || productQuantity.isEmpty() || productUnit.isEmpty() ||
                productPrice.isEmpty() || productStock.isEmpty() || productCategory.isEmpty() || productType.isEmpty()
            ) {
                Utils.hideDialog()
                Utils.showToast(requireContext(), "Empty Fields are not allowed")
            } else if (imageUris.isEmpty()) {
                Utils.hideDialog()
                Utils.showToast(requireContext(), "Please upload some images")
            } else {
                val product = Products(
                    productTitle = productTitle,
                    productQuantity = productQuantity.toInt(),
                    productUnit = productUnit,
                    productPrice = productPrice.toInt(),
                    productStock = productStock.toInt(),
                    productCategory = productCategory,
                    prooductType = productType,
                    itemCount = 0,
                    adminUid = Utils.getCurrentUserId(),
                    productRandomId = Utils.getRandomId()             )

                saveImage(product)
            }
        }
    }

    private fun saveImage(products: Products) {
        viewModel.saveImagesInDB(imageUris)
        lifecycleScope.launch {
            viewModel.isImagesUpload.collect {
                if (it) {
                    Utils.hideDialog()
                    Utils.showToast(requireContext(), "Image saved")
                }
                getUrls(products)
            }
        }
    }

    private fun getUrls(products: Products) {
        Utils.showDialog(requireContext(), "Publishing product...")
        lifecycleScope.launch {
            viewModel.downloadedUrls.collect {
                val urls = it
                products.productImagesUris = urls
                saveProduct(products)
            }
        }
    }

    private fun saveProduct(products: Products) {
        viewModel.saveProduct(products)
        lifecycleScope.launch {
            viewModel.isProductSaved.collect {
                if (it) {
                    Utils.hideDialog()
                    startActivity(Intent(requireContext(), AdminMainActivity::class.java))
                    Utils.showToast(requireContext(), "Your product is live")
                }
            }
        }
    }

    private fun onImageSelectClick() {
        binding.imgbuttonSelectImage.setOnClickListener {
            selectedImages.launch("image/*")
        }
    }

    private fun setAutoCompleteTextViews() {
        val units = ArrayAdapter(
            requireContext(),
            R.layout.show_item_list,
            Constant.allUnitsOfProduct
        )
        val category = ArrayAdapter(
            requireContext(),
            R.layout.show_item_list,
            Constant.allProductsCategory
        )
        val productType = ArrayAdapter(
            requireContext(),
            R.layout.show_item_list,
            Constant.allProductTypes
        )

        binding.apply {
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

