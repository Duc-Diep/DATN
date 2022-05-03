package com.ducdiep.bookmarket.ui.manage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ducdiep.bookmarket.base.TABLE_USERS
import com.ducdiep.bookmarket.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ManageViewModel:ViewModel() {
    var stackFragment:ArrayList<String> = arrayListOf()
    var uid = FirebaseAuth.getInstance().currentUser?.uid

    var data = FirebaseDatabase.getInstance().getReference(TABLE_USERS)
    var userDetails = MutableLiveData<User>()
    init {
        getUserById()
    }

    fun getUserById(){
        uid?.let {
            data.child(it).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    userDetails.value = snapshot.getValue(User::class.java)
                }

                override fun onCancelled(error: DatabaseError) {
                    userDetails.value = null
                }

            })
        }
    }
}