package com.ducdiep.bookmarket.ui.client.userinfor

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ducdiep.bookmarket.base.TABLE_USERS
import com.ducdiep.bookmarket.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class UserInforViewModel : ViewModel() {
    var userLiveData = MutableLiveData<User?>(null)
    var firebaseUser = MutableLiveData<FirebaseUser?>(null)
    var isSuccess = MutableLiveData<Boolean>()
    var isLoading = MutableLiveData<Boolean>()
    var auth = FirebaseAuth.getInstance()
    var databaseReference: DatabaseReference

    init {
        getFirebaseUser()
        getUserInfor()
        databaseReference = FirebaseDatabase.getInstance().getReference(TABLE_USERS).child(
            firebaseUser.value?.uid.toString()
        )
    }

    private fun getFirebaseUser() {
        firebaseUser.value = FirebaseAuth.getInstance().currentUser
    }

    private fun getUserInfor() {
        if (firebaseUser != null) {
            isLoading.value = true
            FirebaseDatabase.getInstance().getReference(TABLE_USERS)
                .child(firebaseUser.value?.uid.toString())
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        userLiveData.value = snapshot.getValue(User::class.java)
                        isLoading.value = false
                    }

                    override fun onCancelled(error: DatabaseError) {
                        isSuccess.value = false
                        isLoading.value = false
                    }
                })
        } else {
            userLiveData.value = null
            isLoading.value = false
        }
    }

    fun logout() {
        auth.signOut()
        firebaseUser.value = null
    }

    fun updatePassword(pass: String) {
        firebaseUser.value?.updatePassword(pass)?.addOnCompleteListener {
            isSuccess.value = it.isSuccessful
        }
    }

    fun updateField(hm: HashMap<String, Any>) {
        databaseReference.updateChildren(hm)
    }

}