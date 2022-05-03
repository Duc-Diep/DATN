package com.ducdiep.bookmarket.ui.client.cart

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ducdiep.bookmarket.base.TABLE_BOOKS
import com.ducdiep.bookmarket.base.TABLE_ORDER
import com.ducdiep.bookmarket.base.TABLE_ORDER_DETAIL
import com.ducdiep.bookmarket.base.TABLE_USERS
import com.ducdiep.bookmarket.models.*
import com.ducdiep.bookmarket.sqlite.SQLHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CartViewModel(application: Application) : AndroidViewModel(application) {
    private var context = application.applicationContext
    private var sqlHelper: SQLHelper = SQLHelper(context)

    var isLoading = MutableLiveData<Boolean>()
    var dataBooks = FirebaseDatabase.getInstance().getReference(TABLE_BOOKS)
    var dataOrderDetail = FirebaseDatabase.getInstance().getReference(TABLE_ORDER_DETAIL)
    var dataOrder = FirebaseDatabase.getInstance().getReference(TABLE_ORDER)
    var dataUser = FirebaseDatabase.getInstance().getReference(TABLE_USERS)

    var listCartId = hashMapOf<String, Int>()

    var listBookInCart = MutableLiveData<ArrayList<BookInCart>>()

    var orderId = ""

    var firebaseUser = FirebaseAuth.getInstance().currentUser
    var user: User? = null

    init {
        getAllBooks()
        listCartId = sqlHelper.getAllBookId()
        getLastKey()
        updateKeyOrderDetails()
        getUserInfor()
    }

    private fun getUserInfor() {
        if (firebaseUser != null) {
            isLoading.value = true
            dataUser.child(firebaseUser?.uid.toString())
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        user = snapshot.getValue(User::class.java)
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })
        } else {
            user = null
        }
    }

    private fun getAllBooks() {
        isLoading.value = true
        dataBooks.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listTemp = arrayListOf<BookInCart>()
                snapshot.children.forEach {
                    val book = it.getValue(Book::class.java)
                    if (book != null) {
                        listCartId.forEach { data ->
                            if (book.book_id == data.key) {
                                listTemp.add(BookInCart(book, data.value))
                            }
                        }
                    }
                }
                listBookInCart.value = listTemp
                isLoading.value = false
            }

            override fun onCancelled(error: DatabaseError) {
                isLoading.value = false
            }
        })
    }

    fun minusNumber(id: String) {
        sqlHelper.minusBookInCart(id)
    }

    fun plusNumber(id: String) {
        sqlHelper.insertOrUpdateBookToCart(id)
    }

    fun deleteItem(id: String) {
        sqlHelper.deleteBook(id)
    }

    fun orderCart(order: Order, listOrder: List<OrderDetail>) {
        val hm = HashMap<String, Any>()
        hm["order_id"] = ""
        hm["created_at"] = order.created_at
        hm["updated_at"] = order.updated_at
        hm["discount"] = order.discount
        hm["total_amount"] = order.total_amount
        hm["user_id"] = order.user_id
        hm["status"] = order.status

        dataOrder.push().setValue(hm)

        viewModelScope.launch {
            delay(500)
            listOrder.forEach {
                val hm = HashMap<String, Any>()
                hm["book_id"] = it.book_id
                hm["created_at"] = it.created_at
                hm["order_id"] = orderId
                hm["order_detail_id"] = ""
                hm["quantity"] = it.quantity
                hm["updated_at"] = it.updated_at
                dataOrderDetail.push().setValue(hm)
            }
        }

        listBookInCart.value?.clear()
        sqlHelper.deleteAllBook()
    }

    fun getLastKey() {
        dataOrder.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lastKey = snapshot.children.last().key.toString()
                orderId = lastKey
                val newId = hashMapOf<String, Any>("order_id" to lastKey)
                dataOrder.child(lastKey).updateChildren(newId)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    fun updateKeyOrderDetails() {
        dataOrderDetail.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lastKey = snapshot.children.last().key.toString()
                val newId = hashMapOf<String, Any>("order_detail_id" to lastKey)
                dataOrderDetail.child(lastKey).updateChildren(newId)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

}