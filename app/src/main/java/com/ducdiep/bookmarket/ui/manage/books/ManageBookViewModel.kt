package com.ducdiep.bookmarket.ui.manage.books

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ducdiep.bookmarket.base.TABLE_BOOKS
import com.ducdiep.bookmarket.base.TABLE_USERS
import com.ducdiep.bookmarket.models.Book
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ManageBookViewModel : ViewModel() {
    var listBooks = MutableLiveData<ArrayList<Book>>()
    var data = FirebaseDatabase.getInstance().getReference(TABLE_BOOKS)
    var isLoading = MutableLiveData<Boolean>()
    var bookDetails = MutableLiveData<Book>()

    init {
        getAllBooks()
    }

    private fun getAllBooks() {
        isLoading.value = true
        data.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listBooks.value = ArrayList()
                snapshot.children.forEach {
                    val book = it.getValue(Book::class.java)
                    Log.d("userr", "onDataChange: $book")
                    if (book != null) {
                        listBooks.value?.add(book)
                    }
                }
                Log.d("userr", "onDataChange: ${listBooks.value}")
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

    fun getBookById(id: String) {
        data.child(id).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                bookDetails.value = snapshot.getValue(Book::class.java)
            }

            override fun onCancelled(error: DatabaseError) {
                bookDetails.value = null
            }
        })
    }

    fun removeById(id:String){
        data.child(id).removeValue()
    }
}