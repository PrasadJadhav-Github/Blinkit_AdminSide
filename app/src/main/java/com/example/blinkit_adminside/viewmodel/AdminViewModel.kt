package com.example.blinkit_adminside.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.blinkit_adminside.models.Products
import com.example.blinkit_adminside.object_class.Utils
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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


    fun saveProduct(products: Products){
        FirebaseDatabase.getInstance().getReference("Admins").child("AllProducts/${products.productRandomId}").setValue(products)
            .addOnSuccessListener {
                FirebaseDatabase.getInstance().getReference("Admins").child("ProductCategory/${products.productRandomId}").setValue(products)
                    .addOnSuccessListener {
                        FirebaseDatabase.getInstance().getReference("Admins").child("ProductsType/${products.productRandomId}").setValue(products)
                            .addOnSuccessListener {
                                _isProductSaved.value=true
                            }

                    }

            }
    }

}