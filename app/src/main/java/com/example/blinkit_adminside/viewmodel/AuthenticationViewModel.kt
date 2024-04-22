package com.example.blinkit_adminside.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.example.blinkit_adminside.object_class.Utils
import com.example.blinkit_adminside.models.Admin
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.concurrent.TimeUnit

class AuthenticationViewModel :ViewModel() {

    private val _verificationID = MutableStateFlow<String?>(null)
    private val _otpSent= MutableStateFlow(false)
    val otpSend=_otpSent

    private val _isSignedInSuccessful= MutableStateFlow(false)
    val isSignedInSuccessful=_isSignedInSuccessful

    private  val _isACurrentUser = MutableStateFlow(false)
    val isACurrentUser = _isACurrentUser



    init {
        Utils.getAuthInstance().currentUser?.let {
            _isACurrentUser.value=true
        }
    }


    fun sendOTP(phoneNumber: String,activity: Activity) {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

            }

            override fun onVerificationFailed(e: FirebaseException) {

            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken) {

                _verificationID.value=verificationId
                _otpSent.value=true

            }
        }

        val options = PhoneAuthOptions.newBuilder(Utils.getAuthInstance())
            .setPhoneNumber("+91 $phoneNumber") // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout duration
            .setActivity(activity) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)


    }
    fun signInWithPhoneAuthCredential(otp: String, phoneNumber: String, user: Admin) {
        val credential = PhoneAuthProvider.getCredential(_verificationID.value.toString(), otp)
        Utils.getAuthInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                user.uid= Utils.getCurrentUserId()
                if (task.isSuccessful) {
                    FirebaseDatabase.getInstance().getReference("Admins").child("AdminInfo").child(user.uid!!).setValue(user)
                    _isSignedInSuccessful.value=true
                } else {

                }
            }
    }
}