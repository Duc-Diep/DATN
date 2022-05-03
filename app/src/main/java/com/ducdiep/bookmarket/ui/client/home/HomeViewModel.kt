package com.ducdiep.bookmarket.ui.client.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ducdiep.bookmarket.base.TABLE_BOOKS
import com.ducdiep.bookmarket.models.Book
import com.ducdiep.bookmarket.models.Category
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeViewModel : ViewModel() {
    var listBooks = MutableLiveData<ArrayList<Book>>()
    var listRecommendBooks = MutableLiveData<ArrayList<Book>>()
    var data = FirebaseDatabase.getInstance().getReference(TABLE_BOOKS)
    var isLoading = MutableLiveData<Boolean>()

    var listSearchAllData = MutableLiveData<ArrayList<Book>>()
    var listSearchByCategory = MutableLiveData<ArrayList<Book>>()

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
                    if (book != null) {
                        listTemp.add(book)
                    }
                }
                listBooks.value = listTemp
                isLoading.value = false
            }

            override fun onCancelled(error: DatabaseError) {
                isLoading.value = false
            }
        })
    }

    fun getAllBooksByCategoryId(id: String, bid: String) {
        isLoading.value = true
        data.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listTemp = arrayListOf<Book>()
                snapshot.children.forEach {
                    val book = it.getValue(Book::class.java)
                    if (book != null && book.category_id == id && book.book_id != bid) {
                        listTemp.add(book)
                    }
                }
                listRecommendBooks.value = listTemp
                isLoading.value = false
            }

            override fun onCancelled(error: DatabaseError) {
                isLoading.value = false
            }
        })
    }

    fun searchAllData(query: String) {
        val lowerQuery = query.toLowerCase()
        if (lowerQuery == "") {
            listSearchAllData.value = listBooks.value
        } else {
            val listResult = listBooks.value?.filter {
                it.name.toLowerCase().contains(query) || it.author.toLowerCase().contains(query)
            }
            if (listResult != null) {
                listSearchAllData.value = listResult as ArrayList<Book>?
            } else {
                listSearchAllData.value = arrayListOf()
            }
        }
    }

    fun searchDataInCategory(query: String) {
        val lowerQuery = query.toLowerCase()
        if (lowerQuery == "") {
            listSearchByCategory.value = listRecommendBooks.value
        } else {
            val listResult = arrayListOf<Book>()
            listRecommendBooks.value?.forEach {
                if (it.name.toLowerCase().contains(query) || it.author.toLowerCase()
                        .contains(query)
                ) {
                    listResult.add(it)
                }
            }
            Log.d("abcc", "searchDataInCategory: ${listResult.size}")
            listSearchByCategory.value = listResult
        }
    }
}