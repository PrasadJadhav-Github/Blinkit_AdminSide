package com.example.blinkit_adminside.models

import java.util.UUID

data class Products(
    var productRandomId : String? =null,
    var productTitle : String?=null,
    var productQuantity :Int?=null,
    var productUnit :String?=null,
    var productPrice : Int?=null,
    var productStock : Int? =null,
    var productCategory : String?=null,
    var prooductType : String ?=null,
    var itemCount : Int?=null,
    var adminUid :String?=null,
    var productImagesUris : ArrayList<String?>?=null,
)
