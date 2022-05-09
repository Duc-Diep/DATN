package com.ducdiep.bookmarket.ui.client.userinfor.order.listorder

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ducdiep.bookmarket.base.TABLE_ORDER
import com.ducdiep.bookmarket.models.Book
import com.ducdiep.bookmarket.models.BookInCart
import com.ducdiep.bookmarket.models.Order
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ListOrderViewModel:ViewModel() {
    var dataOrder = FirebaseDatabase.getInstance().getReference(TABLE_ORDER)
    var listOrder = MutableLiveData<ArrayList<Order>>()
    var uid = FirebaseAuth.getInstance().currentUser?.uid
    var isLoading = MutableLiveData<Boolean>()

    init {
        getAllOrder()
    }

    fun getAllOrder(){
        isLoading.value = true
        dataOrder.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listTemp = arrayListOf<Order>()
                snapshot.children.forEach {
                    val order = it.getValue(Order::class.java)
                    if (order != null && order.user_id==uid) {
                        listTemp.add(order)
                    }
                }
                listOrder.value = listTemp
                isLoading.value = false
            }

            override fun onCancelled(error: DatabaseError) {
                isLoading.value = false
            }
        })
    }

    fun updateStatus(orderId: String, position: Int) {
        val hm = hashMapOf<String, Any>("status" to position)
        dataOrder.child(orderId).updateChildren(hm)
    }
}