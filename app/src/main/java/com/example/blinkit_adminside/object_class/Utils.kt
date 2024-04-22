package com.example.blinkit_adminside.object_class

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import com.example.blinkit_adminside.databinding.ProgressDialogBinding
import com.google.firebase.auth.FirebaseAuth

object Utils {

    fun showToast(context: Context, message:String){
        Toast.makeText(context,message, Toast.LENGTH_LONG).show()
    }

    private var dialog: AlertDialog? = null

    fun showDialog(context: Context, message: String) {
        val binding = ProgressDialogBinding.inflate(LayoutInflater.from(context))
        binding.tvMessage.text = message
        dialog = AlertDialog.Builder(context)
            .setView(binding.root)
            .setCancelable(false)
            .create()
        dialog?.show()
    }

    fun hideDialog(){
        dialog?.dismiss()
    }

    private  var firebaseAuthInstance : FirebaseAuth?=null
    fun getAuthInstance(): FirebaseAuth {

        if (firebaseAuthInstance ==null){
            firebaseAuthInstance = FirebaseAuth.getInstance()
        }
        return  firebaseAuthInstance!!
    }

    fun getCurrentUserId(): String{
        return  FirebaseAuth.getInstance().currentUser!!.uid
    }

    fun getRandomId():String{
        return (1..25).map { ('A'..'Z') + ('a'..'z') + ('0'..'9') }.random().joinToString("")
    }

}