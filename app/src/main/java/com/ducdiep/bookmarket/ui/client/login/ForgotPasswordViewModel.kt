package com.ducdiep.bookmarket.ui.client.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordViewModel : ViewModel() {
    var isSuccess = MutableLiveData<Boolean>()
    var isLoading = MutableLiveData<Boolean>()
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    fun sendEmail(email: String) {
        isLoading.value = true
        auth.sendPasswordResetEmail(email).addOnCompleteListener {
            if (it.isSuccessful) {
                isSuccess.value = true
            } else {
                isSuccess.value = false
            }
            isLoading.value = false
        }
    }
}