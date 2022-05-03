package com.ducdiep.bookmarket.ui.client.userinfor.order.orderdetail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ducdiep.bookmarket.base.TABLE_BOOKS
import com.ducdiep.bookmarket.base.TABLE_ORDER
import com.ducdiep.bookmarket.base.TABLE_ORDER_DETAIL
import com.ducdiep.bookmarket.base.TABLE_USERS
import com.ducdiep.bookmarket.models.Book
import com.ducdiep.bookmarket.models.BookInCart
import com.ducdiep.bookmarket.models.OrderDetail
import com.ducdiep.bookmarket.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OrderDetailViewModel : ViewModel() {
    var isLoading = MutableLiveData<Boolean>()
    var dataBooks = FirebaseDatabase.getInstance().getReference(TABLE_BOOKS)
    var dataOrderDetail = FirebaseDatabase.getInstance().getReference(TABLE_ORDER_DETAIL)
    var dataOrder = FirebaseDatabase.getInstance().getReference(TABLE_ORDER)
    var dataUser = FirebaseDatabase.getInstance().getReference(TABLE_USERS)
    var listOrderDetails = MutableLiveData<List<OrderDetail>>()

    var listBookInCart = MutableLiveData<ArrayList<BookInCart>>()

    var firebaseUser = FirebaseAuth.getInstance().currentUser
    var user = MutableLiveData<User>()

    init {
        getUserInfor()
    }

    private fun getUserInfor() {
        if (firebaseUser != null) {
            isLoading.value = true
            dataUser.child(firebaseUser?.uid.toString())
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        user.value = snapshot.getValue(User::class.java)
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })
        } else {
            user.value = null
        }
    }

    fun getAllBookInOrder() {
        isLoading.value = true
        dataBooks.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listTemp = arrayListOf<BookInCart>()
                snapshot.children.forEach {
                    val book = it.getValue(Book::class.java)
                    if (book != null) {
                        listOrderDetails.value?.forEach { data ->
                            if (book.book_id == data.book_id) {
                                listTemp.add(BookInCart(book, data.quantity))
                            }
                        }
                    }
                }
                listBookInCart.value = listTemp
                Log.d("abcc", "listBook: ${listBookInCart.value}")
                isLoading.value = false
            }

            override fun onCancelled(error: DatabaseError) {
                isLoading.value = false
            }
        })
    }

    fun getOrderDetailById(id: String) {
        dataOrderDetail.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listTemp = arrayListOf<OrderDetail>()
                snapshot.children.forEach {
                    val orderDetail = it.getValue(OrderDetail::class.java)
                    if (orderDetail != null && orderDetail.order_id == id) {
                        listTemp.add(orderDetail)
                    }
                }
                listOrderDetails.value = listTemp
                Log.d("abcc", "onDataChange: ${listOrderDetails.value}")
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}