package com.ducdiep.bookmarket.ui.client.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ducdiep.bookmarket.base.TABLE_USERS
import com.ducdiep.bookmarket.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginViewModel : ViewModel() {
    var isSuccess = MutableLiveData<Boolean>()
    var isLoading = MutableLiveData<Boolean>()
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var dataUser = FirebaseDatabase.getInstance().getReference(TABLE_USERS)
    var listUser = arrayListOf<User>()

    init {
        getAllUsers()
    }

    private fun getAllUsers() {
        dataUser.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    snapshot.getValue(User::class.java)?.let { it1 -> listUser.add(it1) }
                }
                isLoading.value = false
            }

            override fun onCancelled(error: DatabaseError) {
                isSuccess.value = false
                isLoading.value = false
            }
        })
    }

    fun logIn(email: String, password: String) {
        isLoading.value = true
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                isSuccess.value = checkBlock(email)
            } else {
                isSuccess.value = false
            }
            isLoading.value = false
        }
    }

    private fun checkBlock(email: String): Boolean {
        listUser.forEach {
            if (it.email == email && it.active == 1) {
                return true
            }
        }
        return false
    }
}