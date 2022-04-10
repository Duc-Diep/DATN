package com.ducdiep.bookmarket.ui.client.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel:ViewModel() {
    var isSuccess = MutableLiveData<Boolean>()
    var isLoading = MutableLiveData<Boolean>()
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    fun logIn(email:String,password:String){
        isLoading.value = true
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener{
            if (it.isSuccessful){
                var user = FirebaseAuth.getInstance().currentUser
                isSuccess.value = true
            }else{
                isSuccess.value = false
            }
            isLoading.value = false
        }
    }
}