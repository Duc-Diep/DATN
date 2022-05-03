package com.ducdiep.bookmarket.ui.client.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ducdiep.bookmarket.base.TABLE_USERS
import com.ducdiep.bookmarket.extensions.createInvisiblePassword
import com.ducdiep.bookmarket.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterViewModel : ViewModel() {
    var isSuccess = MutableLiveData<Boolean>()
    var isLoading = MutableLiveData<Boolean>()
    var auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun register(user: User) {
        isLoading.value = true
        auth.createUserWithEmailAndPassword(user.email, user.password).addOnCompleteListener {
            if (it.isSuccessful) {
                val firebaseUser = auth.currentUser
                val newUser = user
                newUser.user_id = firebaseUser?.uid.toString()
                newUser.password = createInvisiblePassword(user.password.length)
                FirebaseDatabase.getInstance().getReference(TABLE_USERS)
                    .child(firebaseUser?.uid.toString()).setValue(newUser).addOnCompleteListener {
                        if (it.isSuccessful){
                            isSuccess.value = it.isSuccessful
                        }else{
                            Log.d("abcc", "register: ${it.isSuccessful}")
                        }
                }
                Log.d("abcc", "create register: ${it.isSuccessful}")
            } else {
                isSuccess.value = false
            }
            isLoading.value = false
        }
    }
}