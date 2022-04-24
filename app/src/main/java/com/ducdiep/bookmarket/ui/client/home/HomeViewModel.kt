package com.ducdiep.bookmarket.ui.client.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ducdiep.bookmarket.base.TABLE_BOOKS
import com.ducdiep.bookmarket.models.Book
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeViewModel : ViewModel() {
    var listBooks = MutableLiveData<ArrayList<Book>>()
    var data = FirebaseDatabase.getInstance().getReference(TABLE_BOOKS)
    var isLoading = MutableLiveData<Boolean>()

    init {
        getAllBooks()
    }

    private fun getAllBooks() {
        isLoading.value = true
        data.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listTemp = arrayListOf<Book>()
                snapshot.children.forEach {
                    val book = it.getValue(Book::class.java)
                    Log.d("abcc", "onDataChange: $book")
                    if (book != null) {
                        listTemp.add(book)
                    }
                }
                Log.d("abcc", "onDataChange: ${listBooks.value}")
                listBooks.value = listTemp
                isLoading.value = false
            }

            override fun onCancelled(error: DatabaseError) {
                isLoading.value = false
            }
        })
    }
}