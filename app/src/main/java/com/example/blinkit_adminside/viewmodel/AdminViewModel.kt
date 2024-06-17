package com.example.blinkit_adminside.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.blinkit_adminside.models.Products
import com.example.blinkit_adminside.object_class.Utils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import java.util.UUID

class AdminViewModel: ViewModel() {

    private  val  _isImagesUpload = MutableStateFlow(false)
    var isImagesUpload : StateFlow<Boolean> = _isImagesUpload

    private val _downloadedUrls =MutableStateFlow<ArrayList<String?>>(arrayListOf())
    var downloadedUrls :StateFlow<ArrayList<String?>> = _downloadedUrls

    private  val _isProductSaved =MutableStateFlow(false)
    var isProductSaved:StateFlow<Boolean> = _isProductSaved

    fun saveImagesInDB(imagesUri: ArrayList<Uri>){
        val downloadUrls =ArrayList<String?>()

        imagesUri.forEach{uri->
            val imageRef =FirebaseStorage.getInstance().reference.child(Utils.getCurrentUserId()).child("images")
                .child(UUID.randomUUID().toString())
            imageRef.putFile(uri).continueWithTask {
                imageRef.downloadUrl
            }.addOnCompleteListener { task->
                val  url=task.result
                downloadUrls.add(url.toString())

                if(downloadUrls.size==imagesUri.size){
                    _isImagesUpload.value=true
                    _downloadedUrls.value=downloadUrls
                }
            }
        }
    }


    fun saveProduct(products: Products) {
        // Generate a unique ID for each product
        val productId = UUID.randomUUID().toString()
        products.productRandomId = productId

        FirebaseDatabase.getInstance().getReference("Admins").child("AllProducts/${productId}").setValue(products)
            .addOnSuccessListener {
                FirebaseDatabase.getInstance().getReference("Admins").child("ProductCategory/${productId}").setValue(products)
                    .addOnSuccessListener {
                        FirebaseDatabase.getInstance().getReference("Admins").child("ProductsType/${productId}").setValue(products)
                            .addOnSuccessListener {
                                _isProductSaved.value = true
                            }
                    }
            }
    }


    fun fetchAllTheProducts(category: String?): Flow<List<Products>> = callbackFlow {
        val db= FirebaseDatabase.getInstance().getReference("Admins").child("AllProducts")
        val eventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val products = ArrayList<Products>()
                for (product in snapshot.children){
                    val prod =product.getValue(Products::class.java)
                    if (category=="All" || prod?.productCategory ==category){
                        products.add(prod!!)
                    }
                }
                trySend(products)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }

        db.addValueEventListener(eventListener)
        awaitClose { db.removeEventListener(eventListener)
        }
    }

    fun saveUpdatedProducts(products: Products){
        FirebaseDatabase.getInstance().getReference("Admins")
            .child("AllProducts/${products.productRandomId}").setValue(products)
        FirebaseDatabase.getInstance().getReference("Admins")
            .child("ProductCategory/${products.productRandomId}").setValue(products)
        FirebaseDatabase.getInstance().getReference("Admins")
            .child("ProductsType/${products.productRandomId}").setValue(products)
    }

}