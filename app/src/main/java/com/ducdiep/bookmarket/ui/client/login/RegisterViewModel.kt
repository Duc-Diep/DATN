package com.ducdiep.bookmarket.ui.client.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ducdiep.bookmarket.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterViewModel : ViewModel() {
    var isSuccess = MutableLiveData<Boolean>()
    var auth :FirebaseAuth
    init {
        auth = FirebaseAuth.getInstance()
    }

    fun register(user:User){
        auth.createUserWithEmailAndPassword(user.email,user.password).addOnCompleteListener {
            if (it.isSuccessful){
                val firebaseUser = auth.currentUser
                val newUser = user
                newUser.user_id = firebaseUser?.uid.toString()
                FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser?.uid.toString()).setValue(newUser).addOnCompleteListener {
                    isSuccess.value = it.isSuccessful
                }
            }else{
                isSuccess.value = false
            }
        }
    }
}