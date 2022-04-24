package com.ducdiep.bookmarket.ui.manage.accounts

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ducdiep.bookmarket.base.TABLE_USERS
import com.ducdiep.bookmarket.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ManageAccountViewModel : ViewModel() {
    var listUser = MutableLiveData<ArrayList<User>>()
    var data = FirebaseDatabase.getInstance().getReference(TABLE_USERS)
    var isLoading = MutableLiveData<Boolean>()
    var firebaseUser = FirebaseAuth.getInstance().currentUser
    var userDetails = MutableLiveData<User>()
    init {
        getAllUser()
    }

    private fun getAllUser() {
        isLoading.value = true
        data.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var listTemp = arrayListOf<User>()

                snapshot.children.forEach {
                    val user = it.getValue(User::class.java)
                    Log.d("userr", "onDataChange: $user")
                    if (user != null && user.user_id != firebaseUser?.uid) {
                        listTemp.add(user)
                    }
                }
                Log.d("userr", "onDataChange: ${listUser.value}")
                listUser.value = listTemp
                isLoading.value = false
            }

            override fun onCancelled(error: DatabaseError) {
                isLoading.value = false
            }
        })
    }

    fun updateField(id: String, hm: HashMap<String, Any>) {
        data.child(id).updateChildren(hm)
    }

    fun getUserById(id:String){
        data.child(id).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userDetails.value = snapshot.getValue(User::class.java)
            }

            override fun onCancelled(error: DatabaseError) {
                userDetails.value = null
            }

        })
    }
}